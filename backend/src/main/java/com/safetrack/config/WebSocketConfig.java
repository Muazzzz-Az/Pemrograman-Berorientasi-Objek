package com.safetrack.config;

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
        // Prefix untuk pesan dari server ke klien (Command Center UI)
        config.enableSimpleBroker("/topic");
        // Prefix untuk pesan dari klien ke server
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint utama untuk koneksi handshake dari React
        registry.addEndpoint("/ws-safetrack")
                .setAllowedOrigins("http://localhost:5173") // Wajib sama dengan port Vite
                .withSockJS(); // Fallback protocol
    }
}