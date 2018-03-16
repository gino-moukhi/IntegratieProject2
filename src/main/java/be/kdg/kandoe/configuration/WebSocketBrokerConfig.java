package be.kdg.kandoe.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // For ChatRoom
        config.enableSimpleBroker("/chatroom");
        config.setApplicationDestinationPrefixes("/room");

        //For Playing
        config.enableSimpleBroker("/gamesession");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //For Chatroom /chat - For Playing - /play
        registry.addEndpoint("/chat", "/play").setAllowedOrigins("*").withSockJS();
    }

}