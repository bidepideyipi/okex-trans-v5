package com.supermancell.okex.trans.trader.event;

import com.supermancell.okex.trans.trader.model.Signal;
import org.springframework.context.ApplicationEvent;

public class CreateOrderEvent extends ApplicationEvent {

    private Signal signal;
    public CreateOrderEvent(Object source, Signal signal) {
        super(source);
        this.signal = signal;
    }
}
