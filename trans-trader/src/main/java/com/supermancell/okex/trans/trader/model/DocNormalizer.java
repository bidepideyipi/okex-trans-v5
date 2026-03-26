package com.supermancell.okex.trans.trader.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Date;

@Document(collection = "normalizer")
@CompoundIndex(name = "unique_inst_id_bar_column", def = "{'inst_id': 1, 'bar': 1, 'column': 1}", unique = true)
public class DocNormalizer {
    
    @Id
    private String id;
    
    private String bar;
    
    private String column;
    
    @Field("inst_id")
    private String instId;
    
    @Field("created_at")
    private Date createdAt;
    
    private BigDecimal mean;
    
    private BigDecimal std;
    
    @Field("updated_at")
    private Date updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getMean() {
        return mean;
    }

    public void setMean(BigDecimal mean) {
        this.mean = mean;
    }

    public BigDecimal getStd() {
        return std;
    }

    public void setStd(BigDecimal std) {
        this.std = std;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "DocNormalizer{" +
                "id='" + id + '\'' +
                ", bar='" + bar + '\'' +
                ", column='" + column + '\'' +
                ", instId='" + instId + '\'' +
                ", createdAt=" + createdAt +
                ", mean=" + mean +
                ", std=" + std +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
