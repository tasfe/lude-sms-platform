package com.lude.sms.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * 内容标签
 * @author island
 *
 */
@SuppressWarnings("serial")
public class ContextTag extends BodyTagSupport
{
    private String title;

    private String height="500px";

    @Override
    public int doEndTag() throws JspException
    {        
        JspWriter jw =  pageContext.getOut();
        try
        {
            jw.println("<html><head><link type=\"text/css\" rel=\"stylesheet\" href=\"images/main.css\"/></head><body>");
            jw.println("<table id=\"content\" cellpadding=\"0\" cellspacing=\"0\"><tr><td class=\"row1_left\"><span></span></td><td class=\"row1_middle\"><span>");
            jw.println(title);
            jw.println("</span></td><td class=\"row1_right\"><span></span></td></tr><tr><td class=\"row2_left\">&nbsp;</td><td class=\"row2_middle\" height=\"" + height + "\">");
            
            if(null != bodyContent)
            {
                jw.println(bodyContent.getString());
            }            
            
            jw.println("</td><td class=\"row2_right\">&nbsp;</td></tr><tr><td class=\"row3_left\"><span></span></td><td class=\"row3_middle\"><span></span></td><td class=\"row3_right\"><span></span></td></tr></table></body</html>");
            jw.flush();
        }
        catch (IOException e)
        {
            throw new JspException(e);
        }
        
        return 0;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setHeight(String height)
    {
        this.height = height;
    }
}
