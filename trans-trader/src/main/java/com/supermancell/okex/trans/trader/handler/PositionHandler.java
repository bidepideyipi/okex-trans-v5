package com.supermancell.okex.trans.trader.handler;

import com.supermancell.okex.trans.trader.event.HasPosEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PositionHandler {

    @EventListener
    @Async  // 异步处理
    public void handleUserRegisteredAsync(HasPosEvent event) {
        System.out.println("异步处理带仓位的情况");
        // 这里可以执行耗时操作
    }

}
