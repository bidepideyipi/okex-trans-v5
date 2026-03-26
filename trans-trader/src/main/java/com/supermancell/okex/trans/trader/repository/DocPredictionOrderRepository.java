package com.supermancell.okex.trans.trader.repository;

import com.supermancell.okex.trans.trader.model.DocPredictionOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocPredictionOrderRepository extends MongoRepository<DocPredictionOrder, String> {
    
    List<DocPredictionOrder> findByOrderStatus(String orderStatus);

}