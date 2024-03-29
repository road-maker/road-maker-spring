package com.roadmaker.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration @EnableAsync
public class TaskExecutorConfig {
    @Bean(name="executor")
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //사용할 스레드 설정 goes on here
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(30);
        executor.setThreadNamePrefix("executor-");
        executor.initialize();

        return executor;
    }
}
