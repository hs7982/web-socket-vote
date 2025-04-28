package com.hseok.vote.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WebSocketSessionService {
    private final Map<Long, Set<org.springframework.web.socket.WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    public Map<Long, Set<WebSocketSession>> getRoomSessions() {
        return roomSessions;
    }

    public void addSession(Long roomId, org.springframework.web.socket.WebSocketSession session) {
        roomSessions.computeIfAbsent(roomId, k ->
                ConcurrentHashMap.newKeySet()).add(session);
    }

    public void removeSession(Long roomId, org.springframework.web.socket.WebSocketSession session) {
        Set<org.springframework.web.socket.WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
    }

    public void broadcast(Long roomId, String message) {
        Set<org.springframework.web.socket.WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            log.info("브로드케스트! roomId: " + roomId + ", message: " + message);
            sessions.forEach(session -> {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    removeSession(roomId, session);
                    log.error("발송실패: ", e);
                }
            });
        }
    }
}
