package com.ruantang.commons.api;

import java.io.Serializable;
import java.util.Optional;

/**
 * REST API 返回结果
 */
public class ApiResult<T> implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6335359868843897723L;

    /**
     * 业务错误码
     */
    private long code;

    /**
     * 结构集
     */
    private T data;

    /**
     * 描述
     */
    private String message;

    public ApiResult() {
        //to do nothing
    }

    /**
     * 全参
     *
     * @param code    业务错误码
     * @param data    描述
     * @param message 结构集
     */
    public ApiResult(long code, String message, T data) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public ApiResult(IErrorCode iErrorCode) {
        iErrorCode = Optional.ofNullable(iErrorCode).orElse(ApiErrorCode.FAILED);
        this.code = iErrorCode.getCode();
        this.message = iErrorCode.getMessage();
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param
     * @return
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>(
                ApiErrorCode.SUCCESS.getCode(),
                ApiErrorCode.SUCCESS.getMessage(),
                data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取数据
     * @param message 提示信息
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> success(T data, String message) {
        return new ApiResult<T>(
                ApiErrorCode.SUCCESS.getCode(),
                message,
                data);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> failed(IErrorCode errorCode) {
        return new ApiResult<T>(
                errorCode.getCode(),
                errorCode.getMessage(),
                null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> failed(IErrorCode errorCode, String message) {
        return new ApiResult<T>(errorCode.getCode(),
                message,
                null);
    }

    /**
     * 失败返回结果
     *
     * @param message 错误信息
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> failed(String message) {
        return new ApiResult<T>(
                ApiErrorCode.FAILED.getCode(),
                message,
                null);
    }

    /**
     * 失败返回结果
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> failed() {
        return failed(ApiErrorCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> validateFailed() {
        return failed(ApiErrorCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     * @param message 提示信息
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> validateFailed(String message) {
        return new ApiResult<T>(
                ApiErrorCode.VALIDATE_FAILED.getCode(),
                message,
                null);
    }

    /**
     * 未授权登录
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> unauthorized(T data){
        return new ApiResult<T>(
                ApiErrorCode.UNAUTHORIZED.getCode(),
                ApiErrorCode.UNAUTHORIZED.getMessage(),
                data);
    }

    /**
     * 未授权返回结果
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResult<T> forbidden(T data){
        return new ApiResult<T>(
                ApiErrorCode.FORBIDDEN.getCode(),
                ApiErrorCode.FORBIDDEN.getMessage(),
                data);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
