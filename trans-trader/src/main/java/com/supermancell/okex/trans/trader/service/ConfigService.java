package com.supermancell.okex.trans.trader.service;

import com.supermancell.okex.trans.trader.model.DocConfig;
import com.supermancell.okex.trans.trader.repository.DocConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigService {
    
    private static final Logger log = LoggerFactory.getLogger(ConfigService.class);
    
    @Autowired
    private DocConfigRepository docConfigRepository;
    
    public Optional<DocConfig> getConfig(String item, String key) {
        Optional<DocConfig> config = docConfigRepository.findByItemAndKey(item, key);
        if (!config.isPresent()) {
            log.warn("Config not found: item={}, key={}", item, key);
        }
        return config;
    }
    
    public List<DocConfig> getConfigsByItem(String item) {
        List<DocConfig> docConfigs = docConfigRepository.findByItem(item);
        log.info("Retrieved {} configs for item={}", docConfigs.size(), item);
        return docConfigs;
    }
    
    public List<DocConfig> getAllConfigs() {
        List<DocConfig> docConfigs = docConfigRepository.findAll();
        log.info("Retrieved all {} configs", docConfigs.size());
        return docConfigs;
    }
    
    public DocConfig saveConfig(DocConfig docConfig) {
        DocConfig saved = docConfigRepository.save(docConfig);
        log.info("Saved config: {}", saved);
        return saved;
    }
    
    public void deleteConfig(String id) {
        docConfigRepository.deleteById(id);
        log.info("Deleted config with id={}", id);
    }
}