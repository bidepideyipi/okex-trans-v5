package com.supermancell.okex.trans.trader.service;

import com.okex.open.api.bean.view.PositionView;
import com.supermancell.okex.trans.trader.event.HasPosEvent;
import com.supermancell.okex.trans.trader.model.Signal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OkxGridTransService {

    private final PredictionOrderService predictionOrderService;
    private final ApplicationEventPublisher eventPublisher;

    private static final Logger logger = LoggerFactory.getLogger(OkxGridTransService.class);

    public OkxGridTransService(PredictionOrderService predictionOrderService, OkxPositionService okxPositionService, ApplicationEventPublisher eventPublisher) {
        this.predictionOrderService = predictionOrderService;
        this.eventPublisher = eventPublisher;
    }

    public boolean acceptSign(String sign, BigDecimal signalProbability, Signal signal){
        checkOrder(signal);
        return true;
    }

    private boolean checkOrder(Signal signal){

        predictionOrderService.findByOrderStatus();
        //Check position
//        List<PositionView>   positionList= okxPositionService.getSwapPositionList(signal.getInstId());
//        if(positionList.size() >0) {
//            //TODO  有仓位的处理机制
//            HasPosEvent hasPosEvent = new HasPosEvent(this, signal, positionList);
//            eventPublisher.publishEvent(hasPosEvent);
//            return true;
//        }
        return false;
    }


}
