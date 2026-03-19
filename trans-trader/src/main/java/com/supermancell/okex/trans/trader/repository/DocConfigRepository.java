package com.supermancell.okex.trans.trader.repository;

import com.supermancell.okex.trans.trader.model.DocConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocConfigRepository extends MongoRepository<DocConfig, String> {
    
    Optional<DocConfig> findByItemAndKey(String item, String key);
    
    List<DocConfig> findByItem(String item);
    
    List<DocConfig> findByKey(String key);
}