package com.supermancell.okex.trans.trader.handler;

import com.supermancell.okex.trans.trader.event.CreateOrderEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class RecordOrderHandler {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAfterCommit(CreateOrderEvent event) {
        System.out.println("事务提交后 " );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleAfterRollback(CreateOrderEvent event) {
        System.out.println("事务回滚 ");
    }
}
