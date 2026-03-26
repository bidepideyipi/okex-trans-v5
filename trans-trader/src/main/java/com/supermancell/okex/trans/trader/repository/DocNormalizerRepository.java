package com.supermancell.okex.trans.trader.repository;

import com.supermancell.okex.trans.trader.model.DocNormalizer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocNormalizerRepository extends MongoRepository<DocNormalizer, String> {
    
    Optional<DocNormalizer> findByInstIdAndBarAndColumn(String instId, String bar, String column);
    
    List<DocNormalizer> findByInstId(String instId);
    
    List<DocNormalizer> findByBar(String bar);
    
    List<DocNormalizer> findByColumn(String column);
    
    List<DocNormalizer> findByInstIdAndBar(String instId, String bar);
}
