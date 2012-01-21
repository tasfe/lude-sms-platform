package com.lude.sms.mt;

/**
 * 下行短信
 * @author island
 *
 */
public class Mt
{
    /**
     * 手机号码
     */
    private String msisdn;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 机构号
     */
    private String organizationId;

    /**
     * 渠道号
     */
    private String channelId;

    /**
     * 是否实时发送标志
     */
    private String immedflag;

    /**
     * 预约发送时间
     */
    private String orderDatetime;

    /**
     * 发送优先级
     */
    private String priority;

    /**
     * 操作员
     */
    private String operatorId;

    public String getMsisdn()
    {
        return msisdn;
    }

    public void setMsisdn(String msisdn)
    {
        this.msisdn = msisdn;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getOrganizationId()
    {
        return organizationId;
    }

    public void setOrganizationId(String organizationId)
    {
        this.organizationId = organizationId;
    }

    public String getChannelId()
    {
        return channelId;
    }

    public void setChannelId(String channelId)
    {
        this.channelId = channelId;
    }

    public String getImmedflag()
    {
        return immedflag;
    }

    public void setImmedflag(String immedflag)
    {
        this.immedflag = immedflag;
    }

    public String getOrderDatetime()
    {
        return orderDatetime;
    }

    public void setOrderDatetime(String orderDatetime)
    {
        this.orderDatetime = orderDatetime;
    }

    public String getPriority()
    {
        return priority;
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }

    public String getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(String operatorId)
    {
        this.operatorId = operatorId;
    }

}
