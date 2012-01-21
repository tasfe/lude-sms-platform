package com.lude.sms.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Vector;

/**
 * 数据库
 * @author island
 *
 */
public class Database
{
    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 数据库连接字符串
     */
    private String conString;

    /**
     * 数据库驱动字符串
     */
    private String sqlDriver;

    /**
     * 数据库连接
     */
    private Connection con;

    /**
     * 错误码
     */
    public static String error = "";

    /**
     * 事务是否自动提交
     */
    private boolean isAutoCommit = true;

    public Database(String userName, String passWord)
    {
        this.userName = userName;
        this.passWord = passWord;
    }

    public Database(String userName, String passWord, String conString)
    {
        this.sqlDriver = "com.mysql.jdbc.Driver";
        this.userName = userName;
        this.passWord = passWord;
        this.conString = conString;
    }

    public int open()
    {
        try
        {
            Class.forName(sqlDriver).newInstance();
            String connectionStr = conString + "&user=" + userName + "&password=" + passWord;
            con = DriverManager.getConnection(connectionStr);
        }
        catch (Exception e)
        {
            error = e.toString();
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public void close()
    {
        try
        {
            con.close();
        }
        catch (Exception e)
        {
            error = e.toString();
        }
    }

    public void setAutoCommit(boolean blnCommit) throws SQLException
    {
        con.setAutoCommit(blnCommit);
        this.isAutoCommit = blnCommit;
    }

    public void commit() throws SQLException
    {
        con.commit();
    }

    public void rollback() throws SQLException
    {
        con.rollback();
    }

    public Connection getConnection()
    {
        return con;
    }

    public boolean isClosed()
    {
        try
        {
            return con.isClosed();
        }
        catch (Exception e)
        {
        }
        return true;
    }

    public PreparedStatement getPreparedStatement(String query) throws SQLException
    {
        return con.prepareStatement(query);
    }

    public ResultSet query(String sql) throws SQLException
    {
        ResultSet resultSet = null;
        Statement stmt = con.createStatement();
        resultSet = stmt.executeQuery(sql);
        return resultSet;
    }

    public String[][] getRSArray(String sql) throws SQLException, Exception
    {
        ResultSet rs = null;
        int col = 0;
        Vector<String[]> allRecords = new Vector<String[]>();
        String[] oneRecord = null;
        Statement stmt = con.createStatement();
        rs = stmt.executeQuery(sql);
        if (rs.next())
            col = rs.getMetaData().getColumnCount();
        else
            return ( String[][] ) null;
        do
        {
            oneRecord = new String[col];
            for (int j = 0; j < col; j++)
                oneRecord[j] = rs.getString(j + 1);
            allRecords.add(oneRecord);
        }
        while (rs.next());
        
        rs.close();
        stmt.close();
        
        String[][] records = new String[allRecords.size()][col];
        allRecords.copyInto(records);
        return records;
    }

    public String[][] getRSArray(String sql, Object[] strs) throws SQLException, Exception
    {
        ResultSet rs = null;
        int col = 0;
        Vector<String[]> allRecords = new Vector<String[]>();
        String[] oneRecord = null;
        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < strs.length; i++)
        {
            ps.setObject(i + 1, strs[i]);
        }
        rs = ps.executeQuery();
        if (rs.next())
            col = rs.getMetaData().getColumnCount();
        else
            return ( String[][] ) null;
        do
        {
            oneRecord = new String[col];
            for (int j = 0; j < col; j++)
            {
                oneRecord[j] = rs.getString(j + 1);
            }
            allRecords.add(oneRecord);
        }
        while (rs.next());
        
        rs.close();
        ps.close();
        
        String[][] records = new String[allRecords.size()][col];
        allRecords.copyInto(records);
        return records;
    }

    public String[] getOneDimensionArray(String sql) throws SQLException
    {
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        rs.last();
        String[] arrResult = null;

        if (rs.getRow() > 0)
        {
            arrResult = new String[rs.getRow()];
            rs.beforeFirst();
            int i = 0;
            while (rs.next())
            {
                arrResult[i] = rs.getString(1);
                i++;
            }
        }

        rs.close();
        rs = null;
        statement.close();
        statement = null;
        return arrResult;
    }

    public int update(String sql) throws SQLException
    {
        Statement stmt = con.createStatement();
        int returnValue = stmt.executeUpdate(sql);
        stmt.close();
        
        return returnValue;
    }

    public int update(String sql, Object[] strs) throws SQLException
    {
        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < strs.length; i++)
        {
            ps.setObject(i + 1, strs[i]);
        }
        
        int returnValue = ps.executeUpdate();
        ps.close();
        return returnValue;
    }

    public long insert(String sql) throws SQLException
    {
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
        ResultSet rs = stmt.getGeneratedKeys();
        long id = 0L;
        if (rs.next())
        {
            id = rs.getLong(1);
        }
            
        stmt.close();
        return id;
    }

    public int reOpenDB() throws Exception
    {
        int result = -1;
        try
        {
            if (con == null || con.isClosed())
            {
                int i = 0;
                while (i < 5)
                {
                    result = open();
                    if (result == 0)
                    {
                        break;
                    }
                    i++;
                    if (i < 5)
                    {
                        Thread.sleep(1000 * i);
                    }
                }
                
                if (result == 0)
                {
                    try
                    {
                        setAutoCommit(isAutoCommit);
                    }
                    catch (SQLException sqle)
                    {
                        result = -2;
                        sqle.printStackTrace();
                    }                    
                }

            }
            else
            {
                result = 1;
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return result;
    }

    public String sql_date(java.util.Date d)
    {
        Time tt = new Time(d.getTime());
        java.sql.Date dd = new java.sql.Date(d.getTime());

        String str = dd.toString() + " " + tt.toString();
        return str;
    }

    public static String sql_esc_char(String source)
    {
        String ret = "";
        if (source == null)
        {
            return "";
        }

        for (int i = 0; i < source.length(); i++)
        {
            char buffer = source.charAt(i);
            if (buffer == '\\')
                ret = ret + "\\\\";
            else if (buffer == '\'')
                ret = ret + "\\'";
            else
            {
                ret = ret + String.valueOf(buffer);
            }
        }
        return ret;
    }
    
    public Date getSqlDate(java.util.Date d)
    {
        return new Date(d.getTime());
    }
    
    public Time getSqlTime(java.util.Date d)
    {
        return new Time(d.getTime());
    }
    
}
