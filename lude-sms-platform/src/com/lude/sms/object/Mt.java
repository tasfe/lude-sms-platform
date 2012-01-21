package com.lude.sms.object;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * 下行短信
 * 
 * @author island
 * 
 */
public class Mt
{
    /**
     * 短信序列号
     */
    private long mtId;

    /**
     * 手机号码
     */
    private long msisdn;

    /**
     * 银行卡号
     */
    private String bankNo;

    /**
     * 客户号
     */
    private String custNo;

    /**
     * 短信的组织类型[1：转发类；2：组包类等]
     */
    private int mtType;

    /**
     * 短信模板ID
     */
    private long mtTemplateId;

    /**
     * 运营商编号
     */
    private long agentId;

    /**
     * 机构号
     */
    private String orgNo;

    /**
     * 渠道号
     */
    private String channelNo;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 是否立即发送[1：立即发送；2：预约发送]
     */
    private int immedflag;

    /**
     * 预约发送的日期和时间
     */
    private Timestamp orderDatetime;

    /**
     * 短信优先级
     */
    private int priority;

    /**
     * 处理该短信的服务器ID
     */
    private long smsServerId;

    /**
     * 发送类型
     */
    private int sentType;

    /**
     * 交易ID，由短信平台的下一站提供
     */
    private long trxId;

    /**
     * 短信的状态标志[0：待发送；1：发送到下一站成功；2：发送中止；-1：发送到下一站失败]
     */
    private int mtStatus;

    /**
     * 服务提供商将短信发到下一站的状态标记[1：发送到下一站成功；-1：为发送到下一站失败]
     */
    private int spStatus;

    /**
     * 短信中心将短信发到下一站的状态标记[1：发送到下一站成功；-1：为发送到下一站失败]
     */
    private int smscStatus;

    /**
     * 运营商将短信发到手机的状态标记[1：发送到手机成功；-1：为发送到手机失败]
     */
    private int dnStatus;

    /**
     * 最后确认短信发送成功或失败的状态标记[1：短信发送成功；-1：短信发送失败]
     */
    private int finalStatus;

    /**
     * 短信发送不成功的异常短信
     */
    private String errorMsg;

    /**
     * 短信平台将短信发往下一站的总次数
     */
    private int commitCounter;

    /**
     * 短信平台成功将短信发往下一站的次数
     */
    private int commitSuccCounter;

    /**
     * 最后确认消息发送成功（或失败）的日期
     */
    private Date doneDate;

    /**
     * 操作员用户ID
     */
    protected long operatorId;

    /**
     * 创建日期
     */
    protected Date createDate;

    /**
     * 创建时间
     */
    protected Time createTime;

    /**
     * 最后修改日期
     */
    protected Date lastModifyDate;

    /**
     * 最后修改时间
     */
    protected Time lastModifyTime;

    public Mt()
    {
        this.mtId = 0L;
        this.msisdn = 0L;
        this.bankNo = "";
        this.custNo = "";
        this.mtType = 0;
        this.mtTemplateId = 0L;
        this.agentId = 0L;
        this.orgNo = "";
        this.channelNo = "";
        this.content = null;
        this.immedflag = 0;
        this.orderDatetime = null;
        this.priority = 0;
        this.smsServerId = 0L;
        this.trxId = 0L;
        this.mtStatus = 0;
        this.spStatus = 0;
        this.smscStatus = 0;
        this.dnStatus = 0;
        this.finalStatus = 0;
        this.commitCounter = 0;
        this.commitSuccCounter = 0;
        this.doneDate = null;
        this.operatorId = 0L;
        this.createDate = null;
        this.createTime = null;
        this.lastModifyDate = null;
        this.lastModifyTime = null;
    }

    public long getMtId()
    {
        return mtId;
    }

    public void setMtId(long mtId)
    {
        this.mtId = mtId;
    }

    public long getMsisdn()
    {
        return msisdn;
    }

    public void setMsisdn(long msisdn)
    {
        this.msisdn = msisdn;
    }

    public String getBankNo()
    {
        return bankNo;
    }

    public void setBankNo(String bankNo)
    {
        this.bankNo = bankNo;
    }

    public String getCustNo()
    {
        return custNo;
    }

    public void setCustNo(String custNo)
    {
        this.custNo = custNo;
    }

    public int getMtType()
    {
        return mtType;
    }

    public void setMtType(int mtType)
    {
        this.mtType = mtType;
    }

    public long getMtTemplateId()
    {
        return mtTemplateId;
    }

    public void setMtTemplateId(long mtTemplateId)
    {
        this.mtTemplateId = mtTemplateId;
    }

    public long getAgentId()
    {
        return agentId;
    }

    public void setAgentId(long agentId)
    {
        this.agentId = agentId;
    }

    public String getOrgNo()
    {
        return orgNo;
    }

    public void setOrgNo(String orgNo)
    {
        this.orgNo = orgNo;
    }

    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getImmedflag()
    {
        return immedflag;
    }

    public void setImmedflag(int immedflag)
    {
        this.immedflag = immedflag;
    }

    public Timestamp getOrderDatetime()
    {
        return orderDatetime;
    }

    public void setOrderDatetime(Timestamp orderDatetime)
    {
        this.orderDatetime = orderDatetime;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public long getSmsServerId()
    {
        return smsServerId;
    }

    public void setSmsServerId(long smsServerId)
    {
        this.smsServerId = smsServerId;
    }

    public int getSentType()
    {
        return sentType;
    }

    public void setSentType(int sentType)
    {
        this.sentType = sentType;
    }

    public long getTrxId()
    {
        return trxId;
    }

    public void setTrxId(long trxId)
    {
        this.trxId = trxId;
    }

    public int getMtStatus()
    {
        return mtStatus;
    }

    public void setMtStatus(int mtStatus)
    {
        this.mtStatus = mtStatus;
    }

    public int getSpStatus()
    {
        return spStatus;
    }

    public void setSpStatus(int spStatus)
    {
        this.spStatus = spStatus;
    }

    public int getSmscStatus()
    {
        return smscStatus;
    }

    public void setSmscStatus(int smscStatus)
    {
        this.smscStatus = smscStatus;
    }

    public int getDnStatus()
    {
        return dnStatus;
    }

    public void setDnStatus(int dnStatus)
    {
        this.dnStatus = dnStatus;
    }

    public int getFinalStatus()
    {
        return finalStatus;
    }

    public void setFinalStatus(int finalStatus)
    {
        this.finalStatus = finalStatus;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public int getCommitCounter()
    {
        return commitCounter;
    }

    public void setCommitCounter(int commitCounter)
    {
        this.commitCounter = commitCounter;
    }

    public int getCommitSuccCounter()
    {
        return commitSuccCounter;
    }

    public void setCommitSuccCounter(int commitSuccCounter)
    {
        this.commitSuccCounter = commitSuccCounter;
    }

    public Date getDoneDate()
    {
        return doneDate;
    }

    public void setDoneDate(Date doneDate)
    {
        this.doneDate = doneDate;
    }

    public long getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(long operatorId)
    {
        this.operatorId = operatorId;
    }

    public Date getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }

    public Time getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Time createTime)
    {
        this.createTime = createTime;
    }

    public Date getLastModifyDate()
    {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate)
    {
        this.lastModifyDate = lastModifyDate;
    }

    public Time getLastModifyTime()
    {
        return lastModifyTime;
    }

    public void setLastModifyTime(Time lastModifyTime)
    {
        this.lastModifyTime = lastModifyTime;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(300);
        sb.append("mtId:").append(mtId).append(", ");
        sb.append("msisdn:").append(msisdn).append(", ");
        sb.append("bankNo:").append(bankNo).append(", ");
        sb.append("custNo:").append(custNo).append(", ");
        sb.append("mtType:").append(mtType).append(", ");
        sb.append("mtTemplateId:").append(mtTemplateId).append(", ");
        sb.append("agentId:").append(agentId).append(", ");
        sb.append("orgNo:").append(orgNo).append(", ");
        sb.append("channelNo:").append(channelNo).append(", ");
        sb.append("content:").append(content).append(", ");
        sb.append("immedflag:").append(immedflag).append(", ");
        sb.append("orderDatetime:").append(orderDatetime).append(", ");
        sb.append("priority:").append(priority).append(", ");
        sb.append("smsServerId:").append(smsServerId).append(", ");
        sb.append("trxId:").append(trxId).append(", ");
        sb.append("mtStatus:").append(mtStatus).append(", ");
        sb.append("spStatus:").append(spStatus).append(", ");
        sb.append("smscStatus:").append(smscStatus).append(", ");
        sb.append("dnStatus:").append(dnStatus).append(", ");
        sb.append("finalStatus:").append(finalStatus).append(", ");
        sb.append("commitCounter:").append(commitCounter).append(", ");
        sb.append("commitSuccCounter:").append(commitSuccCounter).append(", ");

        return sb.toString();
    }
}
