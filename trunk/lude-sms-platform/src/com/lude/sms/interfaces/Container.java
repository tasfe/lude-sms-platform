package com.lude.sms.interfaces;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.lude.sms.util.LogMailTool;
import com.lude.sms.util.LoggerManager;
import com.lude.sms.util.MailClient;

/**
 * 进程初始化容器,适用于入库和出库进程
 * 
 * @author island
 * 
 */
public class Container extends Thread
{

    /**
     * 程序自检时间间隔
     */
    private static long CHECK_TIME_SPACE = 5000L;

    /**
     * 日志句柄
     */
    private static Logger logger;

    /**
     * 配置文件句柄
     */
    private static ResourceBundle rb;

    /**
     * 进程控件文件句柄
     */
    private File stopControlProperty;

    /**
     * 进程控件文件的最后修改时间
     */
    private long lastModifiedOfProperty = 0L;

    /**
     * 进程停止标志
     */
    private static boolean isServerStop = false;

    /**
     * 具体的业务处理程序
     */
    private Processor processor;

    /**
     * 发送日志邮件的时间间隔(默认是5分钟)
     */
    private int emailAlertTimeLimit = 300000;

    /**
     * 最后一次发送邮件的时间
     */
    private long lastAlertedTime = 0L;
    
    /**
     * 应用程序名称
     */
    private String applicationName = null;

    /**
     * 构造函数
     * 
     * @param configName 配置文件
     * @throws Exception
     */
    public Container(String configName) throws Exception
    {
        System.out.println("加载配置文件: " + configName);

        rb = ResourceBundle.getBundle(configName);

        // 停止进程的控件文件
        stopControlProperty = new File(rb.getString("application.control.filename"));

        // 配置日志参数
        LoggerManager.configure(rb.getString("logger.properties"));
        logger = LoggerManager.getLogger("Container");
        
        //应用程序名称
        applicationName = rb.getString("email.application");

        //业务处理类的全路径
        String className = rb.getString("application.processor.name");
        
        // 实例化出库程序
        try
        {
            logger.info("实例化业务处理类：" + className);
            processor = (( Processor ) Class.forName(className).newInstance());
        }
        catch (IllegalAccessException iae)
        {
            System.out.println(className + " 没有缺省的构造函数");
            throw iae;
        }
        catch (InstantiationException ie)
        {
            System.out.println(className + " 不是正确的类名或没有缺省的构造函数");
            throw ie;
        }
        catch (ExceptionInInitializerError eie)
        {
            System.out.println(className + " 执行构造函数失败");
            throw eie;
        }
        catch (SecurityException se)
        {
            System.out.println("用户没有实例化该业务处理类的权限：" + className);
            throw se;
        }
    }

    /**
     * 启动线程
     */
    public void start()
    {
        new Thread(this).start();
    }

    /**
     * 线程代码
     */
    public void run()
    {
        // 检查进程状态
        checkServerControlStatus();
        
        if(isServerStop)
        {
            return;
        }
        
        // 初始化并启动业务线程
        try
        {
            processor.init(rb);
        }
        catch (Exception e)
        {
            System.out.println("初始化业务线程失败：" + e.getMessage());
            logger.error("初始化业务线程失败", e);
            return;
        }
        
        processor.start();
        
        
        while (!isServerStop)
        {
            // 检查是否需要发送进程日志
            if (isSendAlertEmail())
            {
                String content = LogMailTool.getLoggedExceptions();
                if(null != content && content.length() > 0)
                {
                    sendMail(applicationName + "异常", content);
                }                
            }

            // 检查进程状态，是否需要stop
            checkServerControlStatus();

            try
            {
                sleep(CHECK_TIME_SPACE);
            }
            catch (Exception e)
            {
                logger.error(e);
            }
        }
        
        processor.close();

        // 判断是否还有任务没有处理完成，如有则等待一段时间
        int maxWaitTime = 30;
        int waitTimeCount = 0;
        while ((processor.getWaitingTaskCount() > 0) && (waitTimeCount <= maxWaitTime))
        {
            try
            {
                sleep(10000L);
            }
            catch (Exception e)
            {
                logger.error(e);
            }

            waitTimeCount++;
        }
        
        logger.info("成功关闭容器线程");
        logger.info("成功关闭本进程");
    }

    /**
     * 检查控制文件，判断是否需要关闭容器
     */
    public void checkServerControlStatus()
    {
        try
        {
            if (stopControlProperty.lastModified() > lastModifiedOfProperty)
            {
                lastModifiedOfProperty = stopControlProperty.lastModified();
                ResourceBundle prb = new PropertyResourceBundle(new FileInputStream(rb.getString("application.control.filename")));
                String status = prb.getString("status");
                prb = null;

                if ((status != null) && (status.equals("stop")))
                {
                    logger.info("接到控制文件通知，通知关闭本进程的各个线程...");
                    isServerStop = true;
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e);
        }
    }

    /**
     * 检查是否需要发送邮件
     * @return
     */
    public boolean isSendAlertEmail()
    {
        long nowTime = new Date().getTime();
        if (nowTime - lastAlertedTime >= emailAlertTimeLimit)
        {
            lastAlertedTime = nowTime;
            return true;
        }

        return false;
    }

    /**
     * 发送邮件
     * @param subject
     * @param content
     */
    public static void sendMail(String subject, String content)
    {
        try
        {
            String sender = rb.getString("email.sender");
            String receiver = rb.getString("email.receiver");
            String cc = rb.getString("email.cc");
            MailClient.sendmail(sender, receiver, cc, null, subject, content);
        }
        catch (Exception e)
        {
            logger.error("发送邮件失败", e);
        }
    }

    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("需要配置文件名称作为参数");
        }
        else
        {
            try
            {
                Container container = new Container(args[0]);
                container.start();
            }
            catch (Exception e)
            {
                System.out.println("启动失败");
                e.printStackTrace();
            }
        }
    }
}
