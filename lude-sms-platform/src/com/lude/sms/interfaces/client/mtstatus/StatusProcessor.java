package com.lude.sms.interfaces.client.mtstatus;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.lude.sms.db.DBManager;
import com.lude.sms.db.Database;
import com.lude.sms.interfaces.Processor;
import com.lude.sms.interfaces.client.MtClient;
import com.lude.sms.object.Mt;
import com.lude.sms.util.LoggerManager;

/**
 * 短信状态报告入库
 * 
 * @author island
 * 
 */
public class StatusProcessor implements Processor
{
    /**
     * 短信状态报告单次短信入库时间(默认是10秒)
     */
    private long processTime = 10000L;

    /**
     * 数据库实例
     */
    private Database db;

    /**
     * 日志句柄
     */
    private Logger logger;

    /**
     * 注销该业务处理程序标志
     */
    private boolean isStop = false;
    
    /**
     * 短信出库线程
     */
    private Thread thread;    
    
    /**
     * 待入库的短信状态条数
     */
    private int statusNumber;
    
    /**
     * 取短信状态报告API
     */
    private MtClient mtClient;

    /**
     * 初始化
     */
    public void init(ResourceBundle rb) throws Exception
    {
        LoggerManager.configure(rb.getString("logger.properties").toString());

        logger = LoggerManager.getLogger("StatusProcessor");

        try
        {
            processTime = Integer.parseInt(rb.getString("limit.process.time")) * 1000L;
        }
        catch (Exception e)
        {
            logger.error("单次短信状态报告入库时间: " + rb.getString("limit.process.time") + " 不能转换为整型");
        }
        
        // 实例化发送短信客户端
        mtClient = ( MtClient ) Class.forName(rb.getString("application.mtClient.name")).newInstance();
        mtClient.init(rb);
    }

    /**
     * 待入库的短信状态条数
     */
    public int getWaitingTaskCount()
    {
        return statusNumber;
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
            logger.error("取消数据库连接事务自动提交打败", e);
            return false;
        }

        return true;
    }

    /**
     * 关闭数据库
     */
    public void releaseDatabase() throws SQLException
    {
        if (null != db)
        {
            db.close();
            db = null;
        }
    }

    /**
     * 判断数据库是否关闭
     */
    public boolean isDatabaseClosed()
    {
        if (null != db)
        {
            return db.isClosed();
        }
        return true;
    }

    @Override
    public boolean close()
    {
        isStop = true;
        return true;
    }

    @Override
    public void run()
    {
        logger.info("启动短信状态报告入库线程");
        
        if (!initDatabase())
        {
            return;
        }
        
        
        while (!isStop)
        {
            // 开始执行时间
            long timer = System.currentTimeMillis();          
            
            Statement stmt = null;
            List<String> sqlList = new ArrayList<String>();   
            
            try
            {
                statusNumber = 0;
                List<Mt> statusList = mtClient.getMtStatus();                
                statusNumber = statusList.size();
                
                if(statusNumber > 0)
                {
                    //组装SQL语句，并放到List                                     
                    for(int i = 0; i < statusNumber; i++)
                    {
                        Mt mt = statusList.remove(0);
                        if(0 != mt.getSpStatus())
                        {
                            sqlList.add("UPDATE mt_queue set sp_status=" + mt.getSpStatus() + " where trx_id=" + mt.getTrxId());
                        }     
                        else if(0 != mt.getSmscStatus())
                        {
                            sqlList.add("UPDATE mt_queue set smsc_status=" + mt.getSmscStatus() + " where trx_id=" + mt.getTrxId());
                        }
                        else if(0 != mt.getDnStatus())
                        {
                            sqlList.add("UPDATE mt_queue set dn_status=" + mt.getDnStatus() + ", final_status=" + mt.getFinalStatus() + ", done_date= now() where trx_id=" + mt.getTrxId());
                        }
                    }
                    
                    //将SQL放到stmt中
                    stmt = db.getConnection().createStatement();
                    for(int i = 0; i < statusNumber; i++)
                    {
                        stmt.addBatch(sqlList.get(i));
                    }
                    stmt.executeBatch();         
                    db.commit();
                }                
            }
            catch (SQLException e)
            {
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < statusNumber; i++)
                {
                    sb.append(sqlList.get(i)).append("\n");
                }
                
                logger.error("短信状态报告入库程序异常", e);
                logger.error("末成功入库的SQL语句：\n" + sb.toString());                
            }
            catch (Exception e)
            {
                logger.error("短信状态报告入库程序异常", e);
            }

            // 如果执行业务处理时间少于规定时间，则执行相应的等待时间
            long timeDiff = System.currentTimeMillis() - timer;
            if (timeDiff < processTime)
            {
                try
                {
                    Thread.sleep(processTime - timeDiff);
                }
                catch (InterruptedException e)
                {
                    logger.error(e);
                }
            }
            
            logger.debug("短信状态报告入库程序，入库状态报告条数：" + statusNumber + "，所花时间[毫秒]：" + timeDiff);
        }
        
        // 注销该业务处理程序
        try
        {
            releaseDatabase();
        }
        catch (SQLException e)
        {
            logger.error("短信状态报告入库程序关闭数据库失败", e);
        }
        
        thread = null;
        logger.info("短信状态报告入库程序已经关闭");        
    }

    @Override
    public boolean isClose()
    {
        return null == thread;
    }

    @Override
    public boolean start()
    {
        if(null == thread)
        {
            thread = new Thread(this);   
            thread.start();
        }
        
        return true;
    }
}
