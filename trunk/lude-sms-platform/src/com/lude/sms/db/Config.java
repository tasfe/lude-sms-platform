package com.lude.sms.db;

/**
 * 数据库配置
 * @author island
 */
class Config
{
    /**
     * 连接字符串
     */
    String connection;

    /**
     * 用户名
     */
    String username;

    /**
     * 密码
     */
    String password;

    public Config(String connection, String username, String password)
    {
        this.connection = connection;
        this.username = username;
        this.password = password;
    }

    public String getConnection()
    {
        return this.connection;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setConnection(String connection)
    {
        this.connection = connection;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
