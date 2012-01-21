package com.lude.sms.interfaces;

/**
 * 发送短信异常类
 * 
 * @author island
 * 
 */
public class ProcessMtException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误描述
     */
    private String errorMsg;

    /**
     * 异常对象
     */
    private Throwable throwable;

    public ProcessMtException(String errorCode, String errorMsg, Throwable throwable)
    {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.throwable = throwable;
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public Throwable getThrowable()
    {
        return throwable;
    }

}
