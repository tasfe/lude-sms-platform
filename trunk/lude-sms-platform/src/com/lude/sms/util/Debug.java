package com.lude.sms.util;

public class Debug
{
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final String HTML_LINEBREAK = "<br>";

    public static final String HTML_TAB = "&nbsp;";

    public static String getStackTrace(Throwable t)
    {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] elements = t.getStackTrace();
        sb.append(t);
        sb.append(LINE_SEPARATOR);
        for (int i = 0; i < elements.length; i++)
        {
            sb.append("\tat " + elements[i]);
            if (i + 1 < elements.length)
            {
                sb.append(LINE_SEPARATOR);
            }
        }
        return sb.toString();
    }

    public static String getStackTraceInHTML(Throwable t)
    {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] elements = t.getStackTrace();
        sb.append(t);
        sb.append("<br>" + LINE_SEPARATOR);
        for (int i = 0; i < elements.length; i++)
        {
            sb.append("&nbsp;\tat " + elements[i]);
            if (i + 1 < elements.length)
            {
                sb.append("<br>" + LINE_SEPARATOR);
            }
        }
        return sb.toString();
    }
}
