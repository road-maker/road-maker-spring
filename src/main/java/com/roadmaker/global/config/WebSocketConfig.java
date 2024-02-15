package com.roadmaker.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 메시지 브로커가 처리하는 메시지의 prefix를 정의(해당 경로를 구독하면 클라는 메시지를 받을 수 있다)
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 메시지를 보낼 수 있는 prefix를 정의
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").withSockJS(); // 웹소켓 연결을 위한 엔드포인트
    }
}
