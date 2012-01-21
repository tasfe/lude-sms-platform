package com.lude.sms.config;

import java.util.Map;


/**
 * 请求管理类
 * @author island
 *
 */
public final class ReqManager
{
    private static Map<String, Req> requestMap = null;
    
    /**
     * 设置请求配置
     * @param requestMap
     */
    public static void setRequestMap(Map<String, Req> requestMap)
    {
        ReqManager.requestMap = requestMap;
    }
    
    /**
     * 取得请求对象
     * @param name 请求名
     * @return
     */
    public static Req getReq(String name)
    {
        return requestMap.get(name);
    }
}
