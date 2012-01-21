package com.lude.sms.interfaces.server.mtreceive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.List;

import com.lude.sms.interfaces.server.TextProcessor;
import com.lude.sms.object.Mt;

/**
 * 定长报文解释
 * 
 * @author island
 * 
 */
public class FixedTextProcessor extends TextProcessor
{    
    /**
     * 接收短信并解释入库
     */
    @Override
    public void getMtToQueue(Socket socket, List<Mt> mtQueue)
    {
        String lineStr = null;
        InputStream is = null;
        InputStreamReader isReader = null;
        BufferedReader bufferedReader = null;
        try
        {
            is = socket.getInputStream();
            isReader = new InputStreamReader(is);
            bufferedReader = new BufferedReader(isReader);

            while ((lineStr = bufferedReader.readLine()) != null)
            {
                //解释报文
                Mt mt = getMt(lineStr);

                if (mt != null)
                {
                    // 如果短信入库队列已满，测等待一段时间
                    while (getBufferSize() < mtQueue.size())
                    {
                        try
                        {
                            Thread.sleep(100);
                        }
                        catch (InterruptedException e)
                        {
                            getLogger().error(e);
                        }
                    }

                    mtQueue.add(mt);
                }
            }            
        }
        catch (IOException e)
        {
            getLogger().error("接收短信报文异常", e);
        }
        finally
        {
            //关闭各类资源
            try
            {
                if(null != bufferedReader)
                {
                    bufferedReader.close();
                    bufferedReader = null;
                }
                
                if(null != isReader)
                {
                    isReader.close();
                    isReader = null;
                }
                
                if(null != is)
                {
                    is.close();
                    is = null;
                }
                
                socket.close();
            }
            catch (IOException e)
            {
                getLogger().error(e);
            }
        }
    }

    /**
     * 解释单条报文
     * @param text 单条报文
     */
    private Mt getMt(String text)
    {
        if (text.length() < 600)
        {
            getLogger().error("报文长度不足600位：[" + text + "]");
            return null;
        }
        
        if(getLogger().isDebugEnabled())
        {
        	getLogger().debug("接收到的报文：[" + text + "]");
        }
        
        String msisdnStr = null;
        String orgNoStr = null;
        String chnlNoStr = null;
        String immedflagStr = null;
        String oDatetimeStr = null;
        String priorityStr = null;
        String content = null;
        String operatorIdStr = null;
        
        try
        {
            msisdnStr = text.substring(0, 20).trim();        //0——19位是手机号码
            orgNoStr = text.substring(20, 30).trim();        //20——29位是机构编码
            chnlNoStr = text.substring(30, 40).trim();       //30——39位是渠道编码
            immedflagStr = text.substring(40, 43).trim();    //40——42位是立即发送标志
            oDatetimeStr = text.substring(43, 62).trim();    //43——61位是定时发送时间
            priorityStr = text.substring(62, 65).trim();     //62——64位是发送优先级
            operatorIdStr = text.substring(65, 75).trim();   //65——74位是发送优先级
            content = text.substring(100, 600).trim();       //100——599位是发送类型
            
            long msisdn = Long.parseLong(msisdnStr);
            int immedflag = Integer.parseInt(immedflagStr);
            int priority = Integer.parseInt(priorityStr);
            
            int operatorId = 0;
            if(1 < operatorIdStr.length())
            {
                operatorId = Integer.parseInt(operatorIdStr);
            }
            
            Timestamp orderDatetime = null;
            if(19 == oDatetimeStr.length())
            {
                orderDatetime = Timestamp.valueOf(oDatetimeStr);
            }
            else
            {
                orderDatetime = Timestamp.valueOf("2012-01-01 00:00:01");
            }
            
            Mt mt = new Mt();
            mt.setMsisdn(msisdn);
            mt.setOrgNo(orgNoStr);
            mt.setChannelNo(chnlNoStr);
            mt.setImmedflag(immedflag);
            mt.setPriority(priority);
            mt.setOrderDatetime(orderDatetime);
            mt.setOperatorId(operatorId);
            mt.setContent(content.trim());

            return mt;
        }
        catch (Exception e)
        {
            StringBuffer sb = new StringBuffer(300);
            sb.append("报文解释异常：\n");
            sb.append(" [msisdn:").append(msisdnStr).append("]");
            sb.append(" [orgNo:").append(orgNoStr).append("]");
            sb.append(" [chnlNo:").append(chnlNoStr).append("]");
            sb.append(" [immedflag:").append(immedflagStr).append("]");
            sb.append(" [orderDatetime:").append(oDatetimeStr).append("]");
            sb.append(" [priority:").append(priorityStr).append("]");
            sb.append(" [content:").append(content).append("]");
            sb.append("\n").append("具体报文是：[").append(text).append("]\n");
            
            getLogger().error(sb.toString(), e);
        }
        
        return null;
    }
}
