package com.supermancell.okex.trans.trader.repository;

import com.supermancell.okex.trans.trader.model.DocPredictionOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocPredictionOrderRepository extends MongoRepository<DocPredictionOrder, String> {
    
    Optional<DocPredictionOrder> findByPredictionKeyAndInstId(String predictionKey, String instId);
    
    List<DocPredictionOrder> findByPredictionKey(String predictionKey);
    
    List<DocPredictionOrder> findByInstId(String instId);
    
    List<DocPredictionOrder> findByOrderStatus(String orderStatus);
    
    List<DocPredictionOrder> findByPredictionKeyAndOrderStatus(String predictionKey, String orderStatus);
}