package com.lude.sms.interfaces.client;

import java.util.List;
import java.util.ResourceBundle;

import com.lude.sms.interfaces.ProcessMtException;
import com.lude.sms.object.Mt;

/**
 * 短信平台向下一站发送短信的接口
 * @author island
 *
 */
public interface MtClient
{
    /**
     * 初始化
     * @param rb 初始化文件
     * @throws Exception
     */
    public void init(ResourceBundle rb) throws Exception;
    
    /**
     * 克隆实例方法
     * @return
     */
    public MtClient clone();
    
    /**
     * 发送短信
     * @param mt 短信
     * @return
     * @throws Exception
     */
    public long send(Mt mt) throws ProcessMtException;
    
    /**
     * 取得下行短信的状态报告，并将状态报告组装到MT中，以列表的形式返回，主要内容有:trxId, spStatus, smscStatus, dnStatus
     * @return 
     * @throws Exception
     */
    public List<Mt> getMtStatus() throws Exception;
    
    /**
     * 启动客户端
     * @return
     */
    public boolean start();
    
    /**
     * 判断客户端是否已关闭
     * @return
     */
    public boolean isClose();
    
    /**
     * 关闭MtClient
     */
    public void close();
}
