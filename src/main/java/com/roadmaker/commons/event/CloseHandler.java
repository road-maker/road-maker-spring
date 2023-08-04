package com.roadmaker.commons.event;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CloseHandler implements ApplicationListener<ContextClosedEvent> {
    //어플리케이션 종료시 종료되지 않은 스레드 관리: Gracefully shutdown
    @Autowired
    ThreadPoolTaskExecutor executor;

    @Override
    public void onApplicationEvent(@NotNull ContextClosedEvent event) {
        executor.shutdown();
        log.info("비정상적으로 종료된 작업 종료");
    }
}
