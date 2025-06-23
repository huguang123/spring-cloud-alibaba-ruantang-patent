package com.ruantang.commons.constants;

public class RabbitConstants {

    // ======================= 核心服务 (service-matter-core) =======================

    // ----------------------- 发给下游服务的交换机 ---------------------------------
    // 状态事件交换机
    public static final String STATUS_EXCHANGE = "status.exchange"; // 发送给下游服务的交换机


    // 核心服务专属死信队列
    public static final String CORE_DLQ_EXCHANGE = "core.dlq.exchange";
    public static final String CORE_DLQ_QUEUE = "core.dlq.queue";
    public static final String CORE_DLQ_ROUTING_KEY = "core.dlq.routing.key";




    // ======================= 工作流服务 (service-matter-workflow) =======================


    // ---------------------发送给下游服务的交换机-------------------
    // 任务事件配置交换机
    public static final String TASK_CREATE_EXCHANGE = "task.create.exchange";
    //通知事件配置交换机
    public static final String NOTIFICATION_SEND_EXCHANGE = "notification.send.exchange";
    //期限事件配置
    public static final String DEADLINE_SET_EXCHANGE = "deadline.set.exchange";


    //---------------------下游服务声明-------------------------
    //状态事件队列路由
    public static final String STATUS_QUEUE = "status.queue";
    public static final String STATUS_ROUTING_KEY = "status.routing.key";


    // 工作流服务专属死信队列
    public static final String WORKFLOW_DLQ_EXCHANGE = "workflow.dlq.exchange";
    public static final String WORKFLOW_DLQ_QUEUE = "workflow.dlq.queue";
    public static final String WORKFLOW_DLQ_ROUTING_KEY = "workflow.dlq.routing.key";




    // ======================= 任务服务 (service-task) =======================


    // ----------------------- 发送给下游服务的交换机 ----------------------------


    // ----------------------- 下游服务声明 -----------------------------
    //任务事件队列路由
    public static final String TASK_CREATE_ROUTING_KEY = "task.create.routing.key"; // 发送给下游服务的路由键（可选，根据实际路由需求）
    public static final String TASK_CREATE_QUEUE = "task.create.queue"; // 任务创建事件队列


    // 任务服务专属死信队列
    public static final String TASK_DLQ_EXCHANGE = "task.dlq.exchange";
    public static final String TASK_DLQ_QUEUE = "task.dlq.queue";
    public static final String TASK_DLQ_ROUTING_KEY = "task.dlq.routing.key";




    // ======================= 通知服务 (service-notification) =======================


    // ----------------------- 发送给下游服务的交换机 ----------------------------------


    // ----------------------- 下游服务声明 ---------------------------
    //通知发送事件队列
    public static final String NOTIFICATION_SEND_ROUTING_KEY = "notification.send.routing.key"; // 发送给下游服务的路由键（可选，根据实际路由需求）
    public static final String NOTIFICATION_SEND_QUEUE = "notification.send.queue"; // 通知发送事件队列


    // 通知服务专属死信队列
    public static final String NOTIFICATION_DLQ_EXCHANGE = "notification.dlq.exchange";
    public static final String NOTIFICATION_DLQ_QUEUE = "notification.dlq.queue";
    public static final String NOTIFICATION_DLQ_ROUTING_KEY = "notification.dlq.routing.key";




    // ======================= 期限服务 (service-deadline) =======================
    // ----------------------- 发送给下游服务的交换机 -------------------------



    // ----------------------- 下游服务声明 ----------------------------
    public static final String DEADLINE_SET_ROUTING_KEY = "deadline.set.routing"; // 发送给下游服务的路由键（可选，根据实际路由需求）
    public static final String DEADLINE_SET_QUEUE = "deadline.set.queue"; // 期限设置事件队列



    // 期限服务专属死信队列
    public static final String DEADLINE_DLQ_EXCHANGE = "deadline.dlq.exchange";
    public static final String DEADLINE_DLQ_QUEUE = "deadline.dlq.queue";
    public static final String DEADLINE_DLQ_ROUTING_KEY = "deadline.dlq.routing.key";




    // ======================= 通用配置 =======================
    // 消息头属性常量
    public static final String MESSAGE_ID_HEADER = "messageId";
    public static final String SOURCE_SERVICE_HEADER = "sourceService";
    public static final String MESSAGE_TYPE_HEADER = "messageType";
    public static final String VERSION_HEADER = "version";


    // 绑定监控
    public static final int MAX_BINDING_RETRIES = 10;
    public static final long BINDING_RETRY_INTERVAL = 5000;


    /**
     * ┌──────────────────────┐         ┌──────────────────────┐
     * │ service-matter-core  │         │ service-matter-      │
     * │ (生产者)              │         │ workflow (消费者&     │
     * ├──────────────────────┤         │ 生产者)               │
     * │ STATUS_EXCHANGE      │──STATUS─▶ STATUS_QUEUE         │
     * └──────────────────────┘         └─────────┬────────────┘
     *                                            │
     *              ┌───────────────────────┬─────┴─────┬───────────────────────┐
     *              │                       │           │                       │
     *              ▼                       ▼           ▼                       ▼
     * ┌──────────────────────┐┌──────────────────────┐┌──────────────────────┐
     * │ service-task         ││ service-notification ││ service-deadline     │
     * │ (消费者)              ││ (消费者)              ││ (消费者)              │
     * ├──────────────────────┤├──────────────────────┤├──────────────────────┤
     * │ TASK_CREATE_QUEUE    ││ NOTIFICATION_SEND_   ││ DEADLINE_SET_QUEUE   │
     * │                      ││ QUEUE                ││                      │
     * └──────────────────────┘└──────────────────────┘└──────────────────────┘
     */

}
