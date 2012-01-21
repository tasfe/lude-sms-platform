package com.lude.sms.action;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.lude.sms.config.Req;
import com.lude.sms.config.ReqManager;
import com.lude.sms.xml.read.ConfigReader;

/**
 * 请求总线
 * 
 * @author island
 * 
 */
@SuppressWarnings("serial")
public class RequestBus extends HttpServlet
{
    /**
     * 日志句柄
     */
    private static Logger logger;

    /**
     * 请求配置文件路径
     */
    private String reqConfigPath;

    /**
     * 请求配置文件的最后修改时间
     */
    private long reqConfigFileLastModifieDatetime = 0;

    /**
     * 初始化
     */
    public void init() throws ServletException
    {
        String rootPath = getServletContext().getRealPath(File.separator);

        // 配置日志参数
        System.out.println("开始加载日志配置文件");
        PropertyConfigurator.configure(rootPath + getServletConfig().getInitParameter("LOG4J_PATH"));
        logger = Logger.getLogger("RequestBus");
        System.out.println("加载日志配置文件成功");

        // 取得请求配置文件路径
        reqConfigPath = rootPath + getServletConfig().getInitParameter("REQUEST_CONFIG_PATH");
        System.out.println("请求配置文件路径: " + reqConfigPath);
        logger.info("请求配置文件路径: " + reqConfigPath);

        refleshConfig();
        System.out.println("加载请求配置文件完成，开始接受业务请求");
        logger.info("加载请求配置文件完成，开始接受业务请求");
    }

    /**
     * 业务请求转发
     */
    @SuppressWarnings("rawtypes")
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //设置编码
        request.setCharacterEncoding("utf-8");
        
        //浏览器提交表单方法
        String method = request.getMethod();
        request.setAttribute("isPost", "POST".equals(method));
        request.setAttribute("isGet", "GET".equals(method));
        
        
        //取得具体的请求并执行
        String requestURI = request.getRequestURI();
        int endIndex = requestURI.indexOf(".do");
        String requestName = requestURI.substring(1, endIndex);
        Req req = ReqManager.getReq(requestName);
        if (null == req)
        {
            request.setAttribute("msg", "请求的操作不存在.");
            req = ReqManager.getReq("error");
        }
        
        
        //检查用户权限
        String permission = req.getPermission();
        if(null != permission)
        {
            HttpSession session = request.getSession();
            Map map = ( Map ) session.getAttribute("permissions");
            if(null == map)
            {
                request.setAttribute("msg", "请您先登陆再进行相关操作！");
                req = ReqManager.getReq("error");   
            }            
            else if(!map.containsKey(permission))
            {
                request.setAttribute("msg", "目前您还没有[" + permission + "]操作的授权，请联系管理员！");
                req = ReqManager.getReq("error");               
            }
        }


        
        try
        {            
            //执行具体的JSP
            String execute = req.getExecute();
            request.getRequestDispatcher(execute).forward(request, response);
        }
        catch (Exception e)
        {
            logger.error("跳转到: " + req.getExecute() + " 页面异常", e);
            
            req = ReqManager.getReq("error");
            String execute = req.getExecute();
            request.setAttribute("msg", "页面异常，请联系管理员解决");
            request.getRequestDispatcher(execute).forward(request, response);
        }
    }

    /**
     * 定时刷新请求配置
     */
    private void refleshConfig()
    {
        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                File reqConfigFile = new File(reqConfigPath);

                while (true)
                {
                    if (reqConfigFile.lastModified() > reqConfigFileLastModifieDatetime)
                    {
                        reqConfigFileLastModifieDatetime = reqConfigFile.lastModified();
                        Map<String, Req> map = loadConfig(reqConfigFile);
                        if (map.size() > 0)
                        {
                            ReqManager.setRequestMap(map);
                            logger.info("刷新配置文件成功");
                        }
                    }

                    try
                    {
                        sleep(5000L);
                    }
                    catch (InterruptedException e)
                    {
                        logger.error(e);
                    }
                }
            }
        };

        thread.start();
    }

    /**
     * 加载请求配置文件
     * 
     * @param file 请求配置文件
     * @return
     */
    private Map<String, Req> loadConfig(File file)
    {

        SAXParser saxParser = null;
        ConfigReader configReader = new ConfigReader();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        try
        {
            saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(file, configReader);
        }
        catch (Exception e)
        {
            logger.error("解释请求配置文件: " + file.getAbsolutePath() + " 失败.", e);
        }

        return configReader.getRequestMap();
    }
}
