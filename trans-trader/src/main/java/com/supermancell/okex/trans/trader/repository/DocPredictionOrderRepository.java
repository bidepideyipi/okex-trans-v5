package com.supermancell.okex.trans.trader.repository;

import com.supermancell.okex.trans.trader.model.DocPredictionOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocPredictionOrderRepository extends MongoRepository<DocPredictionOrder, String> {
    
    Optional<DocPredictionOrder> findBySignalIdAndInstId(String signalId, String instId);
    
    List<DocPredictionOrder> findBySignalId(String signalId);
    
    List<DocPredictionOrder> findByInstId(String instId);
    
    List<DocPredictionOrder> findByOrderStatus(String orderStatus);
    
    List<DocPredictionOrder> findBySignalIdAndOrderStatus(String signalId, String orderStatus);
}