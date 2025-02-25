package com.ruantang.commons.api;

/**
 * REST API 错误码接口
 */
public interface IErrorCode {
    /**
     * 错误编码：-1失败；200成功
     */
    long getCode();

    /**
     * 错误描述
     */
    String getMessage();

}
