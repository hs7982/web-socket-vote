package com.hseok.vote.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {
    private final WebSocketSessionService sessionService;

    public WebSocketHandler(WebSocketSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        //투표방 id를 받아옴
        String[] paths = session.getUri().getPath().split("/");
        Long roomId = Long.parseLong(paths[paths.length - 1]);

        log.info("[WebSocketHandler] room: " + roomId + " message: " + payload);

        switch (payload) {
            case "hello":
                Map<String, Object> attr = session.getAttributes();
                attr.put("roomId", roomId);
                sessionService.addSession(roomId, session);
                break;
        }

        session.sendMessage(new TextMessage("ok!"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Map<String, Object> attr = session.getAttributes();
        String userName = (String) attr.get("userName");
        Long roomId = (Long) attr.get("roomId");
        log.info("[WebSocketHandler] user socket close: " + userName);

        sessionService.removeSession(roomId, session);
    }


}
