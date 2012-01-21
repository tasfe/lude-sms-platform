package com.lude.sms.db;

import java.util.Hashtable;
import java.util.ResourceBundle;

import com.lude.sms.util.LoggerManager;

public class DBManager
{
    /**
     * 默认的数据库名称
     */
    private static final String DEFAULT_DB_Name = "smsdb";
    
    public static Hashtable<String, Config> telcoDBConfig = new Hashtable<String, Config>();
    
    /**
     * 取数据库
     * @return
     */
    public static Database getDatabase()
    {
        return getDatabase(DEFAULT_DB_Name);
    }

    /**
     * 取数据库
     * @return
     */
    public static Database getDatabase(String dbKey)
    {
        Config config = ( Config ) telcoDBConfig.get(dbKey);
        if (config != null)
            return new Database(config.getUsername(), config.getPassword(), config.getConnection());
        
        System.out.println("查找不到数据库：" + dbKey);
        return null;
    }

   
    static
    {
        //加载数据库配置文件中配置的数据库信息
        ResourceBundle rb = ResourceBundle.getBundle("config/database");
        int configCount = 0;
        try
        {
            while (true)
            {   
                configCount++;
                
                String dbKey = rb.getString("Database.Key." + configCount);
                String connection = rb.getString("Database.ConnectionString." + configCount);
                String username = rb.getString("Database.Username." + configCount);
                String password = rb.getString("Database.Password." + configCount);
                Config config = new Config(connection, username, password);
                telcoDBConfig.put(dbKey, config);                
            }
        }
        catch (Exception e)
        {
            System.out.println(configCount - 1 + " 个数据库配置被加载");
            LoggerManager.getLogger().info(configCount - 1 + " 个数据库配置被加载");
        }
    }
}
