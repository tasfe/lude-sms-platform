package com.lude.sms.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerManager
{
    public static void configure(String log4jProperties)
    {
        PropertyConfigurator.configure(log4jProperties);
    }

    public static Logger getLogger()
    {
        return Logger.getRootLogger();
    }

    public static Logger getLogger(String loggerName)
    {
        Logger logger;
        try
        {
            logger = Logger.getLogger(loggerName);
            if (logger == null)
            {
                logger = Logger.getRootLogger();
            }                
        }
        catch (Exception e)
        {
            logger = Logger.getRootLogger();
        }
        
        return logger;
    }
}
