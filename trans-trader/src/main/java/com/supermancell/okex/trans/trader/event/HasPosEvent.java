package com.supermancell.okex.trans.trader.event;

import com.okex.open.api.bean.view.PositionView;
import com.supermancell.okex.trans.trader.model.Signal;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class HasPosEvent  extends  ApplicationEvent{
    private Signal signal;
    private List<PositionView> positionViews;
    public HasPosEvent(Object source, Signal signal, List<PositionView> positionViews) {
        super(source);
        this.signal = signal;
        this.positionViews = positionViews;
    }
}
