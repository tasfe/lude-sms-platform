package com.lude.sms.interfaces.client.mtsend;

import java.util.List;

import org.apache.log4j.Logger;

import com.lude.sms.interfaces.ProcessMtException;
import com.lude.sms.interfaces.client.MtClient;
import com.lude.sms.object.Mt;
import com.lude.sms.util.LoggerManager;

/**
 * 发送短信线程
 * 
 * @author island
 * 
 */
class MtSender implements Runnable
{
    /**
     * 线程类加器
     */
    private static int senderId = 0;

    /**
     * 线程ID
     */
    private int id;

    /**
     * 发送短信客户端
     */
    private MtClient mtClient;

    /**
     * 待发送短信队列
     */
    private List<Mt> queue;
    
    /**
     * 已发送短信队列
     */
    private List<Mt> sentMts;

    /**
     * 发送短信线程
     */
    private Thread thread;

    /**
     * 日志句柄
     */
    private Logger logger;
    
    /**
     * 线程停止标志
     */
    private boolean isStop;

    /**
     * 发送短信线程构造函数
     * @param mtClient 发送短信客户端
     * @param queue 待发送短信队列
     * @param reSendTimes 短信重发次数
     * @param sentMts 已发送短信队列
     */
    public MtSender(MtClient mtClient, List<Mt> queue, List<Mt> sentMts)
    {
        this.id = ++senderId;
        this.queue = queue;
        this.sentMts = sentMts;
        this.mtClient = mtClient;
        this.logger = LoggerManager.getLogger("MtSender");
    }

    /**
     * 启动发送进程
     * 
     * @return
     */
    public boolean start()
    {
        if (thread == null)
        {
            logger.info("启动发送进程 " + id);

            // 如果mtClient已注销，需要重启mtClient
            if (mtClient.isClose())
            {
                if (!mtClient.start())
                {
                    return false;
                }
            }

            thread = new Thread(this);
            thread.start();
            return true;
        }

        return false;
    }

    public void run()
    {        
        // 启动mtClient
        if (mtClient.isClose())
        {
            if (!mtClient.start())
            {
                logger.error("短信发送客户端启动失败");
                return;
            }
        }

        //下面是发送短信代码，并统计该线程在一段时间里，发了多少条短信
        long sendMtCount = 0;        
        long timer = System.currentTimeMillis();
        while(!isStop)
        {
            while (!queue.isEmpty())
            {
                Mt mt = null;
                synchronized (queue)
                {
                    if(!queue.isEmpty())
                    {
                        mt = queue.remove(0);
                    } 
                    else
                    {
                        try
                        {
                            Thread.sleep(1000L);
                        }
                        catch (InterruptedException e)
                        {
                            logger.error("暂停线程失败", e);
                        }
                        
                        continue;
                    }
                } 

                try
                {
                    sendMt(mt);
                }
                catch (Exception e)
                {
                    logger.error("短信发送线程" + id + " 出现了重大异常，需要关闭该进程", e);
                    break;
                }            
                sendMtCount++;
            }            
        }
        
        mtClient.close();     
        thread = null;
        long timeDiff = System.currentTimeMillis() - timer;             
        logger.info("关闭发送线程 " + id + "，该线程共发了短信条数：" + sendMtCount + "，所花时间：" + timeDiff+ "，待发送短信队列中还有短信条数：" + queue.size());
    }

    public boolean isClose()
    {
        return null == thread;
    }
    
    public boolean close()
    {
    	isStop = true;
    	return true;
    }
    
    /**
     * 发送短信，发送成功或失败都在mt_queue表里做标志
     * @param mt
     * @throws Exception 
     */
    private void sendMt(Mt mt) throws Exception
    {
        long mtId = mt.getMtId();
        int commitCounter = mt.getCommitCounter() + 1;
        mt.setCommitCounter(commitCounter);

        try
        {
            long trxId = mtClient.send(mt);
            int commitSuccCounter = mt.getCommitSuccCounter() + 1;
            mt.setCommitSuccCounter(commitSuccCounter);
            mt.setTrxId(trxId);
            mt.setMtStatus(1);
        }
        catch (ProcessMtException e)
        {
            mt.setMtStatus(0);
            mt.setSentType(2);
            mt.setErrorMsg("短信平台发送下一站失败: " + e.getMessage());
            
            logger.error("发送短信失败，mtId: " + mtId + "，commitCounter：" + commitCounter + "，错误码：" + e.getErrorCode() + "，错误描述：" + e.getErrorMsg(), e);
        }    
        
        sentMts.add(mt);
    }
}
