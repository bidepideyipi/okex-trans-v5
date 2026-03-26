package com.supermancell.okex.trans.trader.event;

import com.supermancell.okex.trans.trader.model.Signal;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

public class CreateOrderEvent extends ApplicationEvent {

    @Getter
    private Signal signal;
    @Getter
    private String algoId;
    @Getter
    private BigDecimal different;

    public CreateOrderEvent(Object source, Signal signal, String algoId, BigDecimal different) {
        super(source);
        this.signal = signal;
        this.algoId = algoId;
        this.different = different;
    }
}
