package com.lude.sms.interfaces.server;

import java.net.Socket;
import java.util.List;
import org.apache.log4j.Logger;

import com.lude.sms.object.Mt;
import com.lude.sms.util.LoggerManager;

/**
 * 短信报文解释线程
 * 
 * @author island
 * 
 */
public abstract class TextProcessor implements Runnable
{
    /**
     * 线程累加器
     */
    private static int processorId = 0;
    
    /**
     * 线程ID
     */
    private int id;
    
    /**
     * 等待入库的短信
     */
    private List<Mt> mtQueue;

    /**
     * 短信队列的最大长度
     */
    private int bufferSize = 100;

    /**
     * 等待处理的socket
     */
    private List<Socket> socketQueue;

    /**
     * 短信报文解释线程
     */
    private Thread thread;

    /**
     * 短信报文解释线程注销标记
     */
    protected boolean isStop;
    
    /**
     * 本报文解释线程所待有的Socket
     */
    private Socket socket;

    /**
     * 日志句柄
     */
    private Logger logger;
    
    public TextProcessor()
    {
        id = ++processorId;
    }

    public void init(List<Mt> mtQueue, int bufferSize, List<Socket> socketQueue)
    {
        this.mtQueue = mtQueue;
        this.bufferSize = bufferSize;
        this.socketQueue = socketQueue;        
        this.logger = LoggerManager.getLogger("TextProcessor");
    }

    /**
     * 判断该线程是否已关闭
     * 
     * @return
     */
    public boolean isClose()
    {
        return null == thread;
    }

    /**
     * 关闭本线程
     */
    public void close()
    {
        isStop = true;        
        thread = null;
    }

    /**
     * 启动短信报文解释线程
     * 
     * @return
     */
    public boolean start()
    {
        if (thread == null)
        {
            thread = new Thread(this);
            thread.start();
            return true;
        }

        return false;
    }

    public void run()
    {
        logger.info("启动报文解释线程" + id);
        
        while (!isStop)
        {
            // 如果没有收到连接或短信入库队列已满，则等待一段时间
            while (socketQueue.size() > 0 && mtQueue.size() < bufferSize)
            {
                synchronized (socketQueue)
                {
                    if(socketQueue.size() > 0)
                    {
                        socket = socketQueue.remove(0);       
                    } 
                    else
                    {
                        continue;
                    }
                }
                
                getMtToQueue(socket, mtQueue);  
                
                logger.info("报文解释线程已成功解释完来自 " + socket.getInetAddress().toString() + " Socket的报文");
            }

            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                logger.error(e);
            }
        }
        
        logger.info("成功关闭报文解释线程" + id);
    }

    public Logger getLogger()
    {
        return logger;
    }
    
    public Socket getSocket()
    {
        return socket;
    }

    public int getBufferSize()
    {
        return bufferSize;
    }

    public abstract void getMtToQueue(Socket socket, List<Mt> mtQueue);
}
