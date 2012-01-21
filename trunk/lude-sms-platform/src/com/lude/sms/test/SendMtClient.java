package com.lude.sms.test;

import java.io.PrintWriter;
import java.net.Socket;

/**
 * 测试往短信服务器发送定长报文短信
 * @author island
 *
 */
public class SendMtClient
{

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {    
        
        char[] msisdn = "13633071111".toCharArray();
        char[] orgId = "1026".toCharArray();
        char[] chnlId = "239".toCharArray();
        char[] immedflag="1".toCharArray();
        char[] oDatetime="2011-12-22 17:05:20".toCharArray();
        char[] priority = "5".toCharArray();
        char[] sendType = "1".toCharArray();
        char[] content = "我们都是中国人！".toCharArray();
        
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
        append(ch, 65, sendType);
        append(ch, 100, content);
        
        String mt = new String(ch);
        
        Socket client = new Socket("127.0.0.1", 9011); 
        PrintWriter writer= new PrintWriter(client.getOutputStream());
        
        int count = 0;
        while(true)
        {
            count++;
            writer.println(mt); 
            if(count > 50)
            {
                writer.flush(); 
                count = 0;                
                //break;
            }            
        }
        
//        writer.close();
//        client.close();        
//        Thread.sleep(3000L);
    }

    private static void append(char[] ch, int index, char[] temp)
    {
       for(int i = 0; i < temp.length; i++)
       {
           ch[index + i] = temp[i];
       }
    }
}
