package com.supermancell.okex.trans.trader.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class ExampleScheduledTask {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 每5秒执行一次
     */
    @Scheduled(fixedRate = 5000)
    public void fixedRateTask() {
        log.info("Fixed Rate Task executed at: {}", LocalDateTime.now().format(formatter));
    }

    /**
     * 每10秒执行一次，延迟2秒后开始
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 2000)
    public void fixedDelayTask() {
        log.info("Fixed Delay Task executed at: {}", LocalDateTime.now().format(formatter));
    }

    /**
     * 使用cron表达式，每分钟的第30秒执行
     */
    @Scheduled(cron = "30 * * * * ?")
    public void cronTask() {
        log.info("Cron Task executed at: {}", LocalDateTime.now().format(formatter));
    }

}
