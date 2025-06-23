package com.ruantang.commons.service;

/**
 * 幂等服务
 */
public interface IdempotencyService {

    /**
     * 幂等处理
     * @param messageId
     * @return
     */
    boolean isProcessed(String messageId);

    /**
     * 标记已处理
     * @param messageId
     */
    void markAsProcessed(String messageId);

}
