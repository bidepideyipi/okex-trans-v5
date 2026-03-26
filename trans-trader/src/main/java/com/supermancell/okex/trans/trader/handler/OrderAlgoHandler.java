package com.supermancell.okex.trans.trader.handler;

import com.supermancell.okex.trans.trader.event.CreateOrderEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderAlgoHandler {

    @EventListener
    @Async  // 异步处理
    public void handle(CreateOrderEvent event) {
        System.out.println("处理订单创建流程");
    }
}
