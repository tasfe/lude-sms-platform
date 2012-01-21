package com.lude.sms.interfaces.server.mtreceive;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.lude.sms.db.DBManager;
import com.lude.sms.db.Database;
import com.lude.sms.interfaces.Processor;
import com.lude.sms.interfaces.server.MtServer;
import com.lude.sms.object.Mt;
import com.lude.sms.util.LoggerManager;

/**
 * 短信入库
 * 
 * @author island
 * 
 */
public class IndbProcessor implements Processor
{
    
    /**
     * 短信入库的SQL
     */
    private static final String MT_IN_DB_COMMAND = "INSERT INTO mt_queue(msisdn, mt_type, mt_template_id, agent_id, org_no, channel_no, content, send_type, immedflag, order_datetime, priority, sms_server_id, operator_id, bank_no, cust_no, create_date, create_time, last_modify_date, last_modify_time) values(?, ?, ?, ?, ?, ?, ?, 1, ?, ?, ?, ?, ?, ?, ?, now(), now(), now(), now())";

    /**
     * 数据库实例
     */
    private Database db;

    /**
     * 等待入库的短信队列
     */
    private List<Mt> mtQueue;

    /**
     * 正在入库的短信队列
     */
    private List<Mt> processingMt;

    /**
     * 日志句柄
     */
    private Logger logger;

    /**
     * 每次入库短信的最大条数
     */
    private int maxFetchSize = 100;
    
    /**
     * 短信平台服务器ID
     */
    private long smsServerId = 0;

    /**
     * 注销该业务处理程序标志
     */
    private boolean isStop = false;

    /**
     * 接收短信的服务端
     */
    private MtServer mtServer;

    /**
     * 短信入库线程
     */
    private Thread thread;

    /**
     * 初始化
     */
    public void init(ResourceBundle rb) throws Exception
    {
        logger = LoggerManager.getLogger("IndbProcessor");
        
        smsServerId = Long.valueOf(rb.getString("receiver.sms.serverid"));

        try
        {
            maxFetchSize = Integer.parseInt(rb.getString("limit.fetch"));
        }
        catch (Exception e)
        {
            logger.error("每次入库短信最大条数: " + rb.getString("limit.fetch") + " 不能转换为整型");
        }

        // 实例化短信队列
        mtQueue = Collections.synchronizedList(new LinkedList<Mt>());
        processingMt = new LinkedList<Mt>();

        // 实例化接收短信服务端
        String mtServerName = rb.getString("application.mtServer.name");
        try
        {
            mtServer = ( MtServer ) Class.forName(mtServerName).newInstance();
        }
        catch (Exception e)
        {
            logger.error("实例化类失败：" + mtServerName, e);
        }
        mtServer.setMtQueue(mtQueue);
        mtServer.init(rb);

    }

    /**
     * 等待发送的短信条数
     */
    public int getWaitingTaskCount()
    {
        return mtQueue.size();
    }

    /**
     * 初始化数据库
     */
    private boolean initDatabase()
    {
        db = DBManager.getDatabase();
        db.open();

        try
        {
            db.setAutoCommit(false);
        }
        catch (SQLException e)
        {
            logger.error("入库程序初始化数据错时出错", e);
            return false;
        }

        return true;
    }

    /**
     * 关闭数据库
     */
    public void releaseDatabase()
    {
        if (null != db)
        {
            db.close();
            db = null;
        }
    }

    /**
     * 将短信入库
     * 
     * @param rs
     */
    private void getMtToDb(List<Mt> mtQueue)
    {
        // 判断入库取多少条短信
        int fetchSizePerRound = mtQueue.size();
        if (fetchSizePerRound > maxFetchSize)
        {
            fetchSizePerRound = maxFetchSize;
        }

        Mt mt = null;
        PreparedStatement ps = null;
        try
        {
            ps = db.getConnection().prepareStatement(MT_IN_DB_COMMAND);
            for (int i = 0; i < fetchSizePerRound; i++)
            {
                mt = mtQueue.remove(0);
                processingMt.add(mt);

                ps.setLong(1, mt.getMsisdn());
                ps.setLong(2, mt.getMtType());
                ps.setLong(3, mt.getMtTemplateId());
                ps.setLong(4, mt.getAgentId());
                ps.setString(5, mt.getOrgNo());
                ps.setString(6, mt.getChannelNo());
                ps.setString(7, mt.getContent());
                ps.setInt(8, mt.getImmedflag());
                ps.setTimestamp(9, mt.getOrderDatetime());
                ps.setLong(10, mt.getPriority());
                ps.setLong(11, smsServerId);
                ps.setLong(12, mt.getOperatorId());
                ps.setString(13, mt.getBankNo());
                ps.setString(14, mt.getCustNo());
                ps.addBatch();
            }
            ps.executeBatch();
            db.getConnection().commit();
        }
        catch (SQLException sqle)
        {
            logger.error("执行短信入库SQL失败：" + MT_IN_DB_COMMAND, sqle);

            int length = processingMt.size();
            for (int i = 0; i < length; i++)
            {
                mt = processingMt.remove(0);
                logger.error("入库失败的短信内容: " + mt.toString());
            }
        }
        finally
        {
            try
            {
                ps.close();
            }
            catch (SQLException e)
            {
                logger.error(e);
            }

            ps = null;
            processingMt.clear();
        }
    }

    /**
     * 从短信队列中取出短信并入库
     */
    @Override
    public void run()
    {
        logger.info("启动入库线程");
        
        if (!initDatabase())
        {
            return;
        }
        
        //如果mtServer已关闭，需要重启mtServer
        if(mtServer.isClose())
        {
            mtServer.start();
        }

        while (!isStop)
        {
            while (mtQueue.size() > 0)
            {
                getMtToDb(mtQueue);
            }

            // 如果待入库短信条数为0，则等待一段时间
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                logger.error(e);
            }
        }
        
        //等待一段时间，查检待入库MT是否已经入库完全
        try
        {
            Thread.sleep(1000L);
        }
        catch (InterruptedException e)
        {
            logger.error(e);
        }

        while (mtQueue.size() > 0)
        {
            getMtToDb(mtQueue);
            
            try
            {
                Thread.sleep(1000L);
            }
            catch (InterruptedException e)
            {
                logger.error(e);
            }
        }
        
        // 注销数据库连接
        releaseDatabase(); 
        logger.info("成功关闭入库线程");
    }

    @Override
    public boolean isClose()
    {
        return null == thread;
    }

    @Override
    public boolean close()
    {
        if(mtServer.close())
        {
            isStop = true;
            return true;
        }

        return false;
    }

    @Override
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

}
