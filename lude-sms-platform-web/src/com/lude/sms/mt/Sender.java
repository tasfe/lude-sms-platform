package com.lude.sms.mt;

import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 发送短信
 * @author island
 *
 */
public class Sender
{
    /**
     * 日志句柄
     */
    private static Logger logger = Logger.getLogger("SentMt");;
    
    /**
     * 发送简单短信
     * @param msisdns 手机号码集
     * @param content 短信内容
     * @param priority 发送优先级
     * @param orderTime 预约时间
     * @param organizationId 机构号
     * @param operatorId 操作员
     * @return
     * @throws Exception 
     */
    public boolean simpleMt(String msisdns, String content, String priority, String orderTime, String organizationId, String channelId, String operatorId) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try
        {
            sdf.parse(orderTime);
        }
        catch (Exception e)
        {
            throw new Exception("日期格式不正确：" + orderTime);
        }

        Mt mt = null;
        String msisdn = null;
        List<Mt> mtList = new ArrayList<Mt>();
        String[] msisdnArray = msisdns.split(";"); 
        for(int i =0; i < msisdnArray.length; i++)
        {

            msisdn = msisdnArray[i];
            if(null != msisdn)
            {
                msisdn = msisdn.trim();
                if(11 == msisdn.length())
                {
                    mt = new Mt();
                    mt.setPriority(priority);
                    mt.setImmedflag("1");                    
                    mt.setMsisdn(msisdn);
                    mt.setContent(content);                    
                    mt.setChannelId(channelId);
                    mt.setOperatorId(operatorId);
                    mt.setOrderDatetime(orderTime);
                    mt.setOrganizationId(organizationId);            
                    
                    mtList.add(mt);                    
                }
                else
                {
                    throw new Exception("手机号码不正确：" + msisdn);
                }
            }
        }
        
        try
        {
            send(mtList);
        }
        catch (Exception e)
        {
            logger.error(e);
            throw new Exception("发送短信失败：" + e.getMessage());
        }
        
        return true;
    }
    
    private static void send(List<Mt> list) throws Exception
    {    
        
        Socket client = new Socket("127.0.0.1", 9011); 
        PrintWriter writer= new PrintWriter(client.getOutputStream());
        
        Mt mt = null;
        String msg = null;
        int length = list.size();
        for(int i = 0; i < length; i++)
        {
            mt = list.get(i);            
            msg = new String(parseMt(mt));
            
            if(logger.isDebugEnabled())
            {
                logger.debug("短信内容：[" + msg + "]");
            }
            
            writer.println(msg); 
        }
        
        writer.flush(); 
        Thread.sleep(1000L);
        writer.close();
        client.close();
    }
    
    /**
     * 将短信解释成字符串
     * @param mt 短信对象
     * @return
     */
    private static char[] parseMt(Mt mt)
    {
        char[] msisdn = mt.getMsisdn().toCharArray();
        char[] orgId = mt.getOrganizationId().toCharArray();
        char[] chnlId = mt.getChannelId().toCharArray();
        char[] immedflag= mt.getImmedflag().toCharArray();
        char[] oDatetime= mt.getOrderDatetime().toCharArray();
        char[] priority = mt.getPriority().toCharArray();
        char[] operatorId = mt.getOperatorId().toCharArray();
        char[] content = mt.getContent().toCharArray();
        
        char[] ch = new char[600];
        
        for(int i = 0; i < 600; i++)
        {
            ch[i] = ' ';
        }
        
        append(ch, 0, msisdn);
        append(ch, 20, orgId);
        append(ch, 30, chnlId);
        append(ch, 40, immedflag);
        append(ch, 43, oDatetime);
        append(ch, 62, priority);
        append(ch, 65, operatorId);
        append(ch, 100, content);
        
        return ch;
    }
    
    /**
     * 组装字符数据
     * @param ch 字符数组
     * @param index 开始位置
     * @param temp 被组装的字符数组
     */
    private static void append(char[] ch, int index, char[] temp)
    {
       for(int i = 0; i < temp.length; i++)
       {
           ch[index + i] = temp[i];
       }
    }
}
