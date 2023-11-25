package com.v1.matripserver.auth;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        LocalDateTime createdTime = LocalDateTime.now();
        log.info("세션이 정상적으로 생성되었습니다. 세션 ID: {}, 생성 시간: {}", se.getSession().getId(), createdTime);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        LocalDateTime destroyedTime = LocalDateTime.now();
        log.info("세션이 정상적으로 소멸되었습니다. 세션 ID: {}, 소멸 시간: {}", se.getSession().getId(), destroyedTime);
    }
}
