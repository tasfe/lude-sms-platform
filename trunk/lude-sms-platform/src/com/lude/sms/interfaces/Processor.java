package com.lude.sms.interfaces;

import java.util.ResourceBundle;

/**
 * 业务处理程序，具体的业务请放在run()中
 * @author island
 *
 */
public interface Processor extends Runnable
{
    /**
     * 初始化
     * @param rb 初始化文件
     * @throws Exception
     */
    public void init(ResourceBundle rb) throws Exception;

    /**
     * 等待任务数量
     * @return
     */
    public int getWaitingTaskCount();
    
    /**
     * 关闭该业务处理程序
     */
    public boolean close();
    
    /**
     * 判断该业务处理线程是否已关闭
     * @return
     */
    public boolean isClose();
    
    /**
     * 启动该业务处理线程
     * @return
     */
    public boolean start();
}
