package com.lude.sms.config;


/**
 * 请求配置
 * 
 * @author island
 * 
 */
public class Req
{
    /**
     * 请求名称
     */
    private String name;

    /**
     * 具体的执行jsp路径或URL
     */
    private String execute;

    /**
     * 回退jsp路径或URL
     */
    private String goback;

    /**
     * 执行仅限
     */
    private String permission;
    
    /**
     * 构造函数
     * @param name 请求名称
     * @param execute 具体的执行jsp路径或URL
     * @param goback 回退jsp路径或URL
     * @param permission 执行仅限
     */
    public Req(String name, String execute, String goback, String permission)
    {
        this.name = name;
        this.execute = execute;
        this.goback = goback;
        this.permission = permission;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getExecute()
    {
        return execute;
    }

    public void setExecute(String execute)
    {
        this.execute = execute;
    }

    public String getGoback()
    {
        return goback;
    }

    public void setGoback(String goback)
    {
        this.goback = goback;
    }

    public String getPermission()
    {
        return permission;
    }

    public void setPermission(String permission)
    {
        this.permission = permission;
    }

}
