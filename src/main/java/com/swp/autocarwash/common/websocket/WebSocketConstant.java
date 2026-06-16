package com.swp.autocarwash.common.websocket;



public final class WebSocketConstant {


    private WebSocketConstant() {
    }


    /**
     * Client connect endpoint
     *
     * Example:
     * /ws
     */
    public static final String ENDPOINT = "/ws";


    /**
     * Prefix client gửi message
     *
     * Example:
     * /app/queue/update
     */
    public static final String APP_PREFIX = "/app";


    /**
     * Prefix server broadcast
     *
     * Example:
     * /topic/queue/1
     */
    public static final String TOPIC_PREFIX = "/topic";
}
