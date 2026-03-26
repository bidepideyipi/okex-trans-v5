package com.supermancell.okex.trans.trader.service;

import com.supermancell.okex.trans.trader.model.DocPredictionOrder;
import com.supermancell.okex.trans.trader.repository.DocPredictionOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class PredictionOrderService {
    
    private static final Logger log = LoggerFactory.getLogger(PredictionOrderService.class);
    
    @Autowired
    private DocPredictionOrderRepository predictionOrderRepository;

    
    public DocPredictionOrder saveOrUpdate(DocPredictionOrder order) {
        Optional<DocPredictionOrder> existing = predictionOrderRepository
            .findById(order.getId());
        
        if (existing.isPresent()) {
            DocPredictionOrder existingOrder = existing.get();
            updateOrder(existingOrder, order);
            DocPredictionOrder saved = predictionOrderRepository.save(existingOrder);
            return saved;
        } else {
            DocPredictionOrder saved = predictionOrderRepository.save(order);
            return saved;
        }
    }
    
    private void updateOrder(DocPredictionOrder existing, DocPredictionOrder newOrder) {
       if (newOrder.getOrderStatus() != null) {
           existing.setOrderStatus(newOrder.getOrderStatus());
       }
    }
    
    public List<DocPredictionOrder> findByOrderStatus(String orderStatus) {
        List<DocPredictionOrder> orders = predictionOrderRepository.findByOrderStatus(orderStatus);
        log.info("Found {} prediction orders with status={}", orders.size(), orderStatus);
        return orders;
    }
    
    public List<DocPredictionOrder> findAll() {
        List<DocPredictionOrder> orders = predictionOrderRepository.findAll();
        log.info("Found all {} prediction orders", orders.size());
        return orders;
    }
    
    public void deleteById(String id) {
        predictionOrderRepository.deleteById(id);
        log.info("Deleted prediction order with id={}", id);
    }

}