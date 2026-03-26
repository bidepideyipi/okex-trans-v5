package com.supermancell.okex.trans.trader.handler;

import com.supermancell.okex.trans.trader.enums.TransStatusEnum;
import com.supermancell.okex.trans.trader.event.CreateOrderEvent;
import com.supermancell.okex.trans.trader.model.DocPredictionOrder;
import com.supermancell.okex.trans.trader.service.PredictionOrderService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
public class RecordOrderHandler {

    private final PredictionOrderService predictionOrderService;

    public RecordOrderHandler(PredictionOrderService predictionOrderService) {
        this.predictionOrderService = predictionOrderService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAfterCommit(CreateOrderEvent event) {
        DocPredictionOrder order = new DocPredictionOrder();

        order.setInstId(event.getSignal().getInstId());
        order.setOrderStatus(TransStatusEnum.RUNNING.v());
        order.setSignal(event.getSignal());
        order.setDifference(event.getDifferent()); //先用0
        order.setAlgoId(event.getAlgoId());
        order.setBeginTime(LocalDateTime.now());
        predictionOrderService.saveOrUpdate(order);

        System.out.println("事务提交后 " );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleAfterRollback(CreateOrderEvent event) {
        System.out.println("事务回滚 ");
    }
}
