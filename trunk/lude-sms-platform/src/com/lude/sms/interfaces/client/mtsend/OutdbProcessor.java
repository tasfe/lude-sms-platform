package com.lude.sms.interfaces.client.mtsend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;

import com.lude.sms.db.DBManager;
import com.lude.sms.db.Database;
import com.lude.sms.interfaces.Processor;
import com.lude.sms.interfaces.client.MtClient;
import com.lude.sms.object.Mt;
import com.lude.sms.util.LoggerManager;

/**
 * 下行短信出库程序
 * 
 * @author island
 * 
 */
public class OutdbProcessor implements Processor
{
    /**
     * 单次短信出库时间(默认是1秒)
     */
    private long processTime = 1000L;

    /**
     * 数据库实例
     */
    private Database db;

    /**
     * 短信队列
     */
    private List<Mt> queue;
    
    /**
     * 已发送的短信集合
     */
    private List<Mt> sentMts;

    /**
     * 队列中待发送的短信ID
     */
    private Set<String> bufferMtIds;

    /**
     * 短信发送线程数组
     */
    private MtSender[] mtSenders;

    /**
     * 日志句柄
     */
    private Logger logger;

    /**
     * 最大短信发送线程数
     */
    private int sendMtThreadSize = 5;

    /**
     * 每次从mt表中取出记录的最大条数
     */
    private int maxFetchSize = 500;

    /**
     * 短信队列的最大长度
     */
    private int bufferSize = 600;

    /**
     * 取待发送短信条件
     */
    private String fetchMtCondition;

    /**
     * 注销该业务处理程序标志
     */
    private boolean isStop = false;
    
    /**
     * 短信出库线程
     */
    private Thread thread;
    
    /**
     * 开始工作时间
     */
    private long beginWorkTime = 0;
    
    /**
     * 结束工作时间
     */
    private long endWorkTime = 2400;

    /**
     * 初始化
     */
    public void init(ResourceBundle rb) throws Exception
    {
        logger = LoggerManager.getLogger("OutdbProcessor");
        
        beginWorkTime = Long.valueOf(rb.getString("begin.work.time"));
        
        endWorkTime = Long.valueOf(rb.getString("end.work.time"));

        try
        {
            sendMtThreadSize = Integer.parseInt(rb.getString("limit.sendmt.thread"));
        }
        catch (Exception e)
        {
            logger.error("发送短信线程数: " + rb.getString("limit.sendmt.thread") + " 不能转换为整型");
        }

        try
        {
            maxFetchSize = Integer.parseInt(rb.getString("limit.fetch"));
        }
        catch (Exception e)
        {
            logger.error("每次从数据库取短信最大条数: " + rb.getString("limit.fetch") + " 不能转换为整型");
        }

        try
        {
            bufferSize = Integer.parseInt(rb.getString("limit.mt.buffer_size"));
        }
        catch (Exception e)
        {
            logger.error("待发送短信队列大小: " + rb.getString("limit.mt.buffer_size") + " 不能转换为整型");
        }

        try
        {
            processTime = Integer.parseInt(rb.getString("mtprocess.one.time")) * 1000;
        }
        catch (Exception e)
        {
            logger.error("每次短信出库时间[秒]: " + rb.getString("limit.reSendTimes") + " 不能转换为整型");
        }

        // 取待发送短信条件
        fetchMtCondition = rb.getString("fetch.query.where.string");

        // 实例化短信队列
        queue = Collections.synchronizedList(new LinkedList<Mt>());
        sentMts = Collections.synchronizedList(new LinkedList<Mt>());
        bufferMtIds = Collections.synchronizedSet(new HashSet<String>());

        // 实例化发送短信客户端
        MtClient mtClient = ( MtClient ) Class.forName(rb.getString("application.mtClient.name")).newInstance();
        mtClient.init(rb);

        // 实例化短信发送线程队列并初始化
        mtSenders = new MtSender[sendMtThreadSize];
        mtSenders[0] = new MtSender(mtClient, queue, sentMts);
        for (int i = 1; i < sendMtThreadSize; i++)
        {
            mtSenders[i] = new MtSender(mtClient.clone(), queue, sentMts);
            mtSenders[i].start();
        }

        logger.info(sendMtThreadSize + " 个短信发送线程被加载");

    }

    /**
     * 等待发送的短信条数和等待更新的短信状态条数
     */
    public int getWaitingTaskCount()
    {
        return bufferMtIds.size() + sentMts.size();
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
            logger.error("出库程序初始化数据错时出错", e);
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

    /**
     * 将短信压入待发送短信队列
     * 
     * @param rs
     * @throws SQLException 
     */
    private long putInQueue(ResultSet rs) throws SQLException
    {
        long rowCount = 0;

        while (rs.next())
        {
            Mt m = new Mt();
            m.setMtId(rs.getLong("mt_id"));
            m.setMsisdn(rs.getLong("msisdn"));
            m.setAgentId(rs.getLong("agent_id"));
            m.setContent(rs.getString("content"));
            m.setPriority(rs.getInt("priority"));
            m.setCommitCounter(rs.getInt("commit_counter"));
            m.setCommitSuccCounter(rs.getInt("commit_succ_counter"));
            m.setSentType(rs.getInt("send_type"));

            bufferMtIds.add("" + m.getMtId());
            queue.add(m);

            rowCount++;
        }

        //检查发送线程是否正常，如果已经关闭，则重发启动
        for (int i = 0; i < sendMtThreadSize && i < queue.size(); i++)
        {
            if (mtSenders[i].isClose())
            {
                mtSenders[i].start();
            }
        }
        
        return rowCount;
    }

    @Override
    public boolean close()
    {
        isStop = true;
        
        //检查发送线程是否正常，如果正常，则关闭
        for (int i = 0; i < sendMtThreadSize && i < queue.size(); i++)
        {
            if (!mtSenders[i].isClose())
            {
                mtSenders[i].close();
            }
        }
        
        return true;
    }

    /**
     * 拼装取待发送短信SQL
     * 
     * @param fetchSizePerRound 记录条数
     * @return
     */
    private String getFetchMtSQL(int fetchSizePerRound)
    {
        //取得当前日期时间
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String now = sdf.format(calendar.getTime());
        
        StringBuffer sb = new StringBuffer(800);
        sb.append("SELECT mt_id, msisdn, agent_id, content, priority, commit_counter, commit_succ_counter, send_type FROM mt_queue Where order_datetime < '").append(now).append("' ");
        sb.append(fetchMtCondition);
        
        //当前时间不在工作区间，则加上立即发送标志作为查询条件
        long hourse = calendar.get(Calendar.HOUR_OF_DAY);
        long minute = calendar.get(Calendar.MINUTE);
        long currentTime = hourse * 100 + minute;
        if(currentTime < beginWorkTime || currentTime > endWorkTime)
        {
            sb.append(" AND immedflag=1");
        }

        //排除已经在短信队列中的短信ID
        Object[] mtIds = bufferMtIds.toArray();
        if(0 < mtIds.length)
        {
          for (int i = 0; i < mtIds.length; i++)
          {
              sb.append(" AND mt_id<>").append(mtIds[i]);
          } 
        }

        //设置排序
        sb.append(" ORDER BY priority, mt_id");
        sb.append(" LIMIT 0, ").append(fetchSizePerRound);

        return sb.toString();
    }

    @Override
    public void run()
    {
        logger.info("启动短信出库线程");
        
        if (!initDatabase())
        {
            return;
        }
        
        ResultSet rs = null;
        String querySql = null;

        while (!isStop)
        {
            //每次出库短信条数
            long mtNumber = 0;  
            
            // 开始执行时间
            long timer = System.currentTimeMillis();

            // 判断需要从数据库取出多少条短信
            int fetchSizePerRound = bufferSize - queue.size();
            if (fetchSizePerRound > maxFetchSize)
            {
                fetchSizePerRound = maxFetchSize;
            }

            if (fetchSizePerRound > 0)
            {
                querySql = getFetchMtSQL(fetchSizePerRound);
                logger.debug("下行短信出库查询SQL: " + querySql);

                try
                {
                    rs = db.query(querySql);
                    mtNumber = putInQueue(rs);
                }
                catch (SQLException e)
                {
                    logger.error("执行下行短信出库查询SQL异常: " + querySql, e);
                }
                finally
                {
                    try
                    {
                        if(null !=  rs)
                        {
                            rs.close();
                            rs = null;                               
                        }
                        
                        if(1 > mtNumber)
                        {
                        	db.commit();
                        }
                    }
                    catch (SQLException e)
                    {
                        logger.error("释放结果集异常", e);
                    }
                }
            }
            
            long updateMtCount = updateMtStatus();

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
            
            logger.debug("出库线程，出库短信条数：" + mtNumber + "，更新短信状态条数：" + updateMtCount + "，所花时间[毫秒]：" + timeDiff);
        }
        
        while(queue.size() > 0 || sentMts.size() > 0)
        {
            long updateMtCount = updateMtStatus();
            logger.info("出库线程停止后，更新短信状态条数：" + updateMtCount);
            
            try
            {
                Thread.sleep(2000L);
            }
            catch (InterruptedException e)
            {
                logger.error(e);
            }
        }
        
        // 注销该业务处理程序
        try
        {
            releaseDatabase();
        }
        catch (SQLException e)
        {
            logger.error("出库程序关闭数据库失败", e);
        }
        
        thread = null;
        logger.info("成功出库程序已经关闭");        
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
    /**
     * 更新已发送短信状态
     * @return 返回更新短信条数
     */
    private long updateMtStatus()
    {
        int sentMtCount = 0;
        if(sentMts.size() > 0)
        {
            Mt mt = null;
            PreparedStatement ps = null;            
            String command = "UPDATE mt_queue SET send_type=?, mt_status =?, trx_id = ?, commit_counter=?, commit_succ_counter=?, last_modify_date = now(), last_modify_time = now() WHERE mt_id = ?";
            try
            {

                ps = db.getConnection().prepareStatement(command);
                int length = sentMts.size();
                for(int i = 0; i < length; i++)
                {
                    mt = sentMts.remove(0);
                    ps.setInt(1, mt.getSentType());
                    ps.setInt(2, mt.getMtStatus());
                    ps.setLong(3, mt.getTrxId());
                    ps.setInt(4, mt.getCommitCounter());
                    ps.setInt(5, mt.getCommitSuccCounter());
                    ps.setLong(6, mt.getMtId()); 
                    ps.addBatch();
                    
                    bufferMtIds.remove(""+mt.getMtId());
                }

                ps.executeBatch();
                db.getConnection().commit();
                ps.close();
                ps = null;
                
                sentMtCount = length;
            }
            catch (SQLException e)
            {
                logger.error("更新已发送短信状态失败", e);
            }
            finally
            {
                if(null != ps)
                {
                    try
                    {
                        ps.close();
                    }
                    catch (SQLException e)
                    {
                        logger.error(e);
                    }
                }
            }
        }
        
        return sentMtCount;
    }
}
