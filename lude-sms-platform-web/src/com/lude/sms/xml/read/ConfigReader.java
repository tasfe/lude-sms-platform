package com.lude.sms.xml.read;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.lude.sms.config.Req;

/**
 * 记取请求配置
 * @author island
 *
 */
public class ConfigReader extends DefaultHandler
{
    /**
     * 请求Map
     */
    private Map<String, Req> requestMap = new HashMap<String, Req>();

    /**
     * 解释具体的Req请求
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        //如果不是请求的配置，直接返回
        if(!"req".equalsIgnoreCase(qName))
        {
            return;
        }
        
        String name = attributes.getValue("name");
        String execute = attributes.getValue("execute");
        String goback = attributes.getValue("goback");
        String permission = attributes.getValue("permission");
        
        if(null == name || name.length() < 1)
        {
            requestMap.clear();
            throw new SAXException("请求名[name]属性不能为空");
        }
        
        if(requestMap.containsKey(name))
        {
            requestMap.clear();
            throw new SAXException("请求名[" + name + "]有重复");
        }
        
        if(execute == name || execute.length() < 1)
        {
            requestMap.clear();
            throw new SAXException("请求中" + name + "的[execute]属性不能为空");
        }
        
        requestMap.put(name, new Req(name, execute, goback, permission));
    }
    
    /**
     * 取得请求配置
     * @return
     */
    public Map<String, Req> getRequestMap()
    {
        return requestMap;
    }
}
