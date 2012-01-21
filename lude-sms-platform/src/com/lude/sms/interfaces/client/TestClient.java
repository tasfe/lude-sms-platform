package com.lude.sms.interfaces.client;

import java.util.List;
import java.util.ResourceBundle;

import com.lude.sms.interfaces.ProcessMtException;
import com.lude.sms.object.Mt;

/**
 * 模拟短信发送速度
 * @author island
 *
 */
public class TestClient implements MtClient
{
    @Override
    public void init(ResourceBundle rb) throws Exception
    {

    }

    @Override
    public long send(Mt mt) throws ProcessMtException
    {
        //每条短信在发送过程中，都算30毫秒的延迟
        try
        {
            Thread.sleep(30L);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }  

        return mt.getMtId();
    }


    /**
     * 克隆实例
     */
    public MtClient clone()
    {
        return this;
    }

    /**
     * 启动发送客户端
     */
    @Override
    public boolean start()
    {
        return true;
    }
    
    

    @Override
    public boolean isClose()
    {
        return false;
    }

    @Override
    public void close()
    {

    }

    @Override
    public List<Mt> getMtStatus() throws ProcessMtException
    {
        return null;
    }
}
