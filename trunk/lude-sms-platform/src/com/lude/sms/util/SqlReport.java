package com.lude.sms.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

import com.lude.sms.db.Database;

/**
 * 报表
 * @author island
 *
 */
public class SqlReport
{
    public static Logger logger = LoggerManager.getLogger("SqlReport");

    String query;

    String title;

    String description;

    String[][] reportData;

    boolean showQuery;

    boolean showTitle;

    boolean showDescription;

    public SqlReport(String query, String title, String description, String[][] reportData)
    {
        this.query = query;
        this.title = title;
        this.description = description;
        this.reportData = reportData;

        this.showQuery = true;
        this.showTitle = true;
        this.showDescription = true;
    }

    public void showDescription(boolean showDescription)
    {
        this.showDescription = showDescription;
    }

    public void showQuery(boolean showQuery)
    {
        this.showQuery = showQuery;
    }

    public void showTitle(boolean showTitle)
    {
        this.showTitle = showTitle;
    }

    public String toString()
    {
        StringBuffer report = new StringBuffer();
        report.append("<br><hr>");

        if ((this.showTitle) && (this.title != null) && (!this.title.equals("")))
        {
            report.append("Report title: ");
            report.append(this.title);
            report.append("<br>");
        }

        if ((this.showDescription) && (this.description != null) && (!this.description.equals("")))
        {
            report.append("Report description: ");
            report.append(this.description);
            report.append("<br>");
        }

        if (this.showQuery)
        {
            report.append("Processed Query<br>  ");
            report.append(this.query);
            report.append("<br>");
        }

        report.append("<table border=1>");
        for (int i = 0; i < this.reportData.length; i++)
        {
            report.append("<tr>");
            for (int j = 0; j < this.reportData[i].length; j++)
            {
                report.append("<td>");
                report.append(this.reportData[i][j]);
                report.append("</td>");
            }
            report.append("</tr>");
        }
        report.append("</table><hr>");

        return report.toString();
    }

    public static SqlReport generateReport(Database db, String query, String title, String description) throws SQLException, Exception
    {
        logger.debug("[QUERY]generate report: " + query);
        ResultSet rs = db.query(query);

        ResultSetMetaData rsm = rs.getMetaData();
        rs.last();

        int countColumn = rsm.getColumnCount();
        int countRow = rs.getRow();
        String[][] reportData = new String[countRow + 1][countColumn];
        rs.beforeFirst();

        int index = 0;

        for (int i = 0; i < countColumn; i++)
        {
            reportData[index][i] = rsm.getColumnName(i + 1);
        }
            
        index++;

        while (rs.next())
        {
            for (int i = 0; i < countColumn; i++)
                reportData[index][i] = rs.getString(i + 1);
            index++;
        }
        rs.close();
        rsm = null;
        rs = null;

        return new SqlReport(query, title, description, reportData);
    }

    public static List<SqlReport> generateReportList(Database db, ResourceBundle rb) throws SQLException, Exception
    {
        List<SqlReport> list = new ArrayList<SqlReport>();
        try
        {
            for (int i = 1; i <= 200; i++)
            {
                String query = rb.getString("sql_report." + i + ".query");
                String title = rb.getString("sql_report." + i + ".title");
                String desc = rb.getString("sql_report." + i + ".description");
                SqlReport r = generateReport(db, query, title, desc);
                list.add(r);
            }

            logger.warn("Only 200 reports are allowed");
        }
        catch (MissingResourceException mre)
        {
            
        }
        catch (Exception e)
        {
            logger.error("[EXCEPTION]generate list of report fail: " + Debug.getStackTrace(e));
        }
        
        return list;
    }
}
