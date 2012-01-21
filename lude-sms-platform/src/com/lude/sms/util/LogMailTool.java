package com.lude.sms.util;

import java.util.Date;
import java.util.Vector;

public final class LogMailTool
{
    private static Vector<String> expLogs = new Vector<String>();
    
    public static void putLog(String msg , Throwable throwable)
    {
        expLogs.add(msg + "[Time: " + new Date() + "]<br>" + Debug.getStackTrace(throwable));
    }
    
    public static String getLoggedExceptions()
    {
      StringBuffer sb = new StringBuffer();
      while (expLogs.size() > 0)
      {
        sb.append(expLogs.remove(0));
        sb.append("<br><hr><br>");
      }

      return sb.toString();
    }
}
