package com.lude.sms.interfaces.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.lude.sms.object.Mt;
import com.lude.sms.util.LoggerManager;

/**
 * 短信接收线程
 * 
 * @author island
 * 
 */
public class MtServer implements Runnable
{
    /**
     * 侦听端口
     */
    private int listenPort;

    /**
     * 日志句柄
     */
    private Logger logger;

    /**
     * 最大短信报文解释线程数
     */
    private int maxTextProcessorThreadSize = 5;

    /**
     * 最小短信报文解释线程数
     */
    private int minTextProcessorThreadSize = 2;

    /**
     * 短信队列
     */
    private List<Mt> mtQueue;

    /**
     * 短信队列的最大长度
     */
    private int bufferSize = 600;

    /**
     * Socket队列
     */
    private List<Socket> socketQueue = null;

    /**
     * Socket队列大小
     */
    private int socketQueueSize = 10;

    /**
     * 连接字
     */
    private ServerSocket serverSocket;
    
    /**
     * 线程停止标志
     */
    private boolean isStop;

    /**
     * 短信接收线程
     */
    private Thread thread;

    /**
     * 短信报文解释线程数组
     */
    private List<TextProcessor> textProList;
    
    public void setMtQueue(List<Mt> mtQueue)
    {
        this.mtQueue = mtQueue;
    }

    /**
     * 初始化
     * 
     * @param rb 初始化文件
     * @throws Exception
     */
    public void init(ResourceBundle rb) throws Exception
    {
        logger = LoggerManager.getLogger("MtServer");

        // 以下是从配置文件中取出配置参数
        try
        {
            minTextProcessorThreadSize = Integer.parseInt(rb.getString("limit.min.textprocessor.thread"));
        }
        catch (Exception e)
        {
            logger.error("最小线程数: " + rb.getString("limit.min.textprocessor.thread") + " 不能转换为整型");
        }

        try
        {
            maxTextProcessorThreadSize = Integer.parseInt(rb.getString("limit.max.textprocessor.thread"));
        }
        catch (Exception e)
        {
            logger.error("最大线程数: " + rb.getString("limit.max.textprocessor.thread") + " 不能转换为整型");
        }

        try
        {
            bufferSize = Integer.parseInt(rb.getString("limit.mt.buffer_size"));
        }
        catch (Exception e)
        {
            logger.error("待入库短信队列大小: " + rb.getString("limit.mt.buffer_size") + " 不能转换为整型");
        }

        try
        {
            listenPort = Integer.parseInt(rb.getString("mtserver.listen.port"));
        }
        catch (Exception e)
        {
            logger.error("侦听端口: " + rb.getString("mtserver.listen.port") + " 不能转换为整型");
        }
        
        // 实例接收短信Socket队列
        socketQueue = Collections.synchronizedList(new LinkedList<Socket>());

        // 实例化报文解释线程队列并初始化
        textProList = new ArrayList<TextProcessor>();
        
        //报文处理类的全路径
        String className = rb.getString("application.textprocessor.name");
        for (int i = 0; i < maxTextProcessorThreadSize; i++)
        {
            
            try
            {
                //实例化出库程序
                TextProcessor processor = ( TextProcessor ) Class.forName(className).newInstance();
                processor.init(mtQueue, bufferSize, socketQueue);
                
                // 启动最小报文处理线程
                if (i < minTextProcessorThreadSize)
                {
                    processor.start();
                }
                
                textProList.add(processor);
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

    }

    /**
     * 将socket压到队列中，由别的程序处理连接
     * 
     * @param socket
     */
    public void handleConnection(Socket socket)
    {
        //如果待处理的socket过多，则启动更多的报文处理线程
        while (socketQueueSize < socketQueue.size())
        {
            //启动更多的报文处理线程
            for(int i = 0; i < textProList.size(); i++)
            {
                TextProcessor processor = textProList.get(i);
                if(processor.isClose())
                {
                    processor.start();
                }
            }
            
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                logger.error(e);
            }
        }

        socketQueue.add(socket);
    }

    /**
     * 判断客户端是否已关闭
     * 
     * @return
     */
    public boolean isClose()
    {
        return null == serverSocket;
    }

    /**
     * 启动短信接收线程
     * @return
     */
    public boolean start()
    {
        if (thread == null)
        {
            thread = new Thread(this);
            thread.start();
        }
        
        return true;
    }

    /**
     * 接收客户端短信
     */
    @Override
    public void run()
    {
        logger.info("启动短信接收服务端线程");
        
        //设置侦听
        try
        {
            serverSocket = new ServerSocket(listenPort, 3);
            logger.info("短信接收服务端成功侦听端口：" + listenPort);
        }
        catch (IOException e)
        {
            logger.error(e);
        }

        //等待客户端连接，并将连接放到队列中
        Socket socket = null;
        while (!isStop)
        {
            try
            {
                socket = serverSocket.accept();
            }
            catch (SocketTimeoutException e) 
            {
                continue;
            }
            catch (IOException e)
            {
                logger.error(e);
                continue;
            }
            
            handleConnection(socket);
        }
    }
    
    public boolean close()
    {
        logger.info("正在关闭短信接收服务端线程...");
        
        //设置线程停止标志
        isStop = true;
        
        //关闭接收短信Socket
        if (null != serverSocket)
        {
            try
            {
                serverSocket.close();
            }
            catch (IOException e)
            {
                logger.error(e);
            }
            
            serverSocket = null;
        }
        logger.info("成功关闭短信接收服务端ServerSocket");
        
        //关闭待处理的socket
        Socket socket = null;
        try
        {
            while(socketQueue.size() > 0)
            {
                socket = socketQueue.remove(0);
                socket.close();
            }            
        }
        catch (IOException e)
        {
            logger.error(e);
        }
        logger.info("成功关闭短信接收服务端待处理Socket");
        
        //关闭报文解释线程和正在处理的Socket
        try
        {
            int length = textProList.size();
            for(int i = 0; i < length; i++)
            {
                TextProcessor textProcessor = textProList.get(i);
                textProcessor.close();
                
                socket = textProcessor.getSocket();
                if(null != socket)
                {
                    socket.close();
                }                
            }        
        }
        catch (IOException e)
        {
            logger.error(e);
        }
        
        logger.info("成功关闭报文解释线程和正在处理的Socket");
        
        return true;       
    }
}
