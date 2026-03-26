package com.supermancell.okex.trans.trader.service;

import com.supermancell.okex.trans.trader.model.DocNormalizer;
import com.supermancell.okex.trans.trader.repository.DocNormalizerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import org.bson.Document;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NormalizerService {
    
    private static final Logger log = LoggerFactory.getLogger(NormalizerService.class);
    
    @Autowired
    private DocNormalizerRepository normalizerRepository;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @PostConstruct
    public void init() {
        try {
            mongoTemplate.indexOps(DocNormalizer.class).ensureIndex(
                new CompoundIndexDefinition(
                    Document.parse("{ \"inst_id\": 1, \"bar\": 1, \"column\": 1 }")
                ).unique().named("unique_inst_id_bar_column")
            );
            log.info("Ensured compound unique index: unique_inst_id_bar_column");
        } catch (Exception e) {
            log.warn("Index creation warning: {}", e.getMessage());
        }
    }
    
    public DocNormalizer saveOrUpdate(DocNormalizer normalizer) {
        Optional<DocNormalizer> existing = normalizerRepository
            .findByInstIdAndBarAndColumn(normalizer.getInstId(), normalizer.getBar(), normalizer.getColumn());
        
        if (existing.isPresent()) {
            DocNormalizer existingNormalizer = existing.get();
            updateNormalizer(existingNormalizer, normalizer);
            existingNormalizer.setUpdatedAt(new Date());
            DocNormalizer saved = normalizerRepository.save(existingNormalizer);
            log.info("Updated normalizer: instId={}, bar={}, column={}", 
                saved.getInstId(), saved.getBar(), saved.getColumn());
            return saved;
        } else {
            normalizer.setCreatedAt(new Date());
            normalizer.setUpdatedAt(new Date());
            DocNormalizer saved = normalizerRepository.save(normalizer);
            log.info("Created new normalizer: instId={}, bar={}, column={}", 
                saved.getInstId(), saved.getBar(), saved.getColumn());
            return saved;
        }
    }
    
    private void updateNormalizer(DocNormalizer existing, DocNormalizer newNormalizer) {
        if (newNormalizer.getMean() != null) {
            existing.setMean(newNormalizer.getMean());
        }
        if (newNormalizer.getStd() != null) {
            existing.setStd(newNormalizer.getStd());
        }
    }
    
    public Optional<DocNormalizer> findByInstIdAndBarAndColumn(String instId, String bar, String column) {
        Optional<DocNormalizer> normalizer = normalizerRepository
            .findByInstIdAndBarAndColumn(instId, bar, column);
        if (normalizer.isPresent()) {
            log.info("Found normalizer: instId={}, bar={}, column={}", instId, bar, column);
        } else {
            log.info("Normalizer not found: instId={}, bar={}, column={}", instId, bar, column);
        }
        return normalizer;
    }
    
    public List<DocNormalizer> findByInstId(String instId) {
        List<DocNormalizer> normalizers = normalizerRepository.findByInstId(instId);
        log.info("Found {} normalizers for instId={}", normalizers.size(), instId);
        return normalizers;
    }
    
    public List<DocNormalizer> findByBar(String bar) {
        List<DocNormalizer> normalizers = normalizerRepository.findByBar(bar);
        log.info("Found {} normalizers for bar={}", normalizers.size(), bar);
        return normalizers;
    }
    
    public List<DocNormalizer> findByColumn(String column) {
        List<DocNormalizer> normalizers = normalizerRepository.findByColumn(column);
        log.info("Found {} normalizers for column={}", normalizers.size(), column);
        return normalizers;
    }
    
    public List<DocNormalizer> findByInstIdAndBar(String instId, String bar) {
        List<DocNormalizer> normalizers = normalizerRepository.findByInstIdAndBar(instId, bar);
        log.info("Found {} normalizers for instId={}, bar={}", normalizers.size(), instId, bar);
        return normalizers;
    }
    
    public List<DocNormalizer> findAll() {
        List<DocNormalizer> normalizers = normalizerRepository.findAll();
        log.info("Found all {} normalizers", normalizers.size());
        return normalizers;
    }
    
    public void deleteById(String id) {
        normalizerRepository.deleteById(id);
        log.info("Deleted normalizer with id={}", id);
    }
    
    public void deleteByInstIdAndBarAndColumn(String instId, String bar, String column) {
        Optional<DocNormalizer> normalizer = normalizerRepository
            .findByInstIdAndBarAndColumn(instId, bar, column);
        normalizer.ifPresent(n -> {
            normalizerRepository.delete(n);
            log.info("Deleted normalizer: instId={}, bar={}, column={}", instId, bar, column);
        });
    }
}
