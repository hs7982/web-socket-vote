package com.hseok.vote.config;

import com.hseok.vote.service.VoteService;
import com.hseok.vote.user.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketHandler webSocketHandler;
    private final VoteService voteService;

    @Autowired
    public WebSocketConfig(WebSocketHandler webSocketHandler, VoteService voteService) {
        this.webSocketHandler = webSocketHandler;
        this.voteService = voteService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/api/ws/**")
                .addInterceptors(new AuthenticationHandshakeInterceptor())
                .setAllowedOrigins("*");
    }

    public class AuthenticationHandshakeInterceptor implements HandshakeInterceptor {
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       org.springframework.web.socket.WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String[] paths = request.getURI().getPath().split("/");
            long roomId = Long.parseLong(paths[paths.length - 1]);
            try {
                voteService.findById(roomId); //투표방이 있는지 찾기
            } catch (Exception e) {
                log.warn("WebSocket Handshake Error : {}", String.valueOf(e));
                return false;
            }

            if (authentication != null) {
                UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
                attributes.put("userId", principal.getUserId());
                attributes.put("userName", principal.getUsername());
            }
            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   org.springframework.web.socket.WebSocketHandler wsHandler, Exception exception) {

        }
    }
}
