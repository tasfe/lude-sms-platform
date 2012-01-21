package com.lude.sms.tag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * 权限标签
 * @author island
 *
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class PermissionTag extends BodyTagSupport
{
    private String name;

    @Override    
    public int doEndTag() throws JspException
    {
        HttpSession session = pageContext.getSession();        
        Map permissions = ( Map ) session.getAttribute("permissions");
        if(null != permissions && permissions.containsKey(name))
        {
            try
            {
                pageContext.getOut().print(bodyContent.getString());
                return 0;
            }
            catch (IOException e)
            {
                throw new JspException(e);
            }
        }
        else
        {
            return SKIP_BODY;
        }        
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
