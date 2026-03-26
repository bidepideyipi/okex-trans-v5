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
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @PostConstruct
    public void init() {
        // 确保复合唯一索引存在
        try {
            mongoTemplate.indexOps(DocPredictionOrder.class).ensureIndex(
                new org.springframework.data.mongodb.core.index.CompoundIndexDefinition(
                    org.bson.Document.parse("{ \"predictionKey\": 1, \"instId\": 1 }")
                ).unique()
            );
            log.info("Created compound unique index on predictionKey + instId");
        } catch (Exception e) {
            log.warn("Index creation warning: {}", e.getMessage());
        }
    }
    
    public DocPredictionOrder saveOrUpdate(DocPredictionOrder order) {
        Optional<DocPredictionOrder> existing = predictionOrderRepository
            .findBySignalIdAndInstId(order.getSignalId(), order.getInstId());
        
        if (existing.isPresent()) {
            DocPredictionOrder existingOrder = existing.get();
            updateOrder(existingOrder, order);
            DocPredictionOrder saved = predictionOrderRepository.save(existingOrder);
            log.info("Updated prediction order: predictionKey={}, instId={}", 
                saved.getSignalId(), saved.getInstId());
            return saved;
        } else {
            DocPredictionOrder saved = predictionOrderRepository.save(order);
            log.info("Created new prediction order: predictionKey={}, instId={}", 
                saved.getSignalId(), saved.getInstId());
            return saved;
        }
    }
    
    private void updateOrder(DocPredictionOrder existing, DocPredictionOrder newOrder) {
//        if (newOrder.getProbability() != null) {
//            existing.setProbability(newOrder.getProbability());
//        }
//        if (newOrder.getPredictionTs() != null) {
//            existing.setPredictionTs(newOrder.getPredictionTs());
//        }
//        if (newOrder.getSz() != null) {
//            existing.setSz(newOrder.getSz());
//        }
//        if (newOrder.getCreateTs() != null) {
//            existing.setCreateTs(newOrder.getCreateTs());
//        }
//        if (newOrder.getCreatePrice() != null) {
//            existing.setCreatePrice(newOrder.getCreatePrice());
//        }
//        if (newOrder.getOrderId() != null) {
//            existing.setOrderId(newOrder.getOrderId());
//        }
//        if (newOrder.getOrderStatus() != null) {
//            existing.setOrderStatus(newOrder.getOrderStatus());
//        }
    }
    
    public Optional<DocPredictionOrder> findByPredictionKeyAndInstId(String predictionKey, String instId) {
        Optional<DocPredictionOrder> order = predictionOrderRepository
            .findBySignalIdAndInstId(predictionKey, instId);
        if (order.isPresent()) {
            log.info("Found prediction order: predictionKey={}, instId={}", predictionKey, instId);
        } else {
            log.info("Prediction order not found: predictionKey={}, instId={}", predictionKey, instId);
        }
        return order;
    }
    
    public List<DocPredictionOrder> findBySignalId(String signalId) {
        List<DocPredictionOrder> orders = predictionOrderRepository.findBySignalId(signalId);
        log.info("Found {} prediction orders for signalId={}", orders.size(), signalId);
        return orders;
    }
    
    public List<DocPredictionOrder> findByInstId(String instId) {
        List<DocPredictionOrder> orders = predictionOrderRepository.findByInstId(instId);
        log.info("Found {} prediction orders for instId={}", orders.size(), instId);
        return orders;
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
    
    public void deleteByPredictionKeyAndInstId(String signalId, String instId) {
        Optional<DocPredictionOrder> order = predictionOrderRepository
            .findBySignalIdAndInstId(signalId, instId);
        order.ifPresent(o -> {
            predictionOrderRepository.delete(o);
            log.info("Deleted prediction order: signalId={}, instId={}", signalId, instId);
        });
    }
}