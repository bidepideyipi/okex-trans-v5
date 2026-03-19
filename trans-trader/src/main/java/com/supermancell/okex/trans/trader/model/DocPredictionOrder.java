package com.supermancell.okex.trans.trader.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "prediction_order")
@CompoundIndex(name = "idx_predictionKey_instId", def = "{'predictionKey': 1, 'instId': 1}", unique = true)
public class DocPredictionOrder {
    
    @Id
    private String id;
    
    private String predictionKey;

    private BigDecimal probability;
    
    private Long predictionTs;
    
    private String instId;

    //张数
    private String sz;

    //订单发起时间
    private Long createTs;

    //订单发起价格
    private BigDecimal createPrice;

    //订单编号
    private String orderId;

    //订单状态
    private String orderStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPredictionKey() {
        return predictionKey;
    }

    public void setPredictionKey(String predictionKey) {
        this.predictionKey = predictionKey;
    }

    public Long getPredictionTs() {
        return predictionTs;
    }

    public void setPredictionTs(Long predictionTs) {
        this.predictionTs = predictionTs;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getSz() {
        return sz;
    }

    public void setSz(String sz) {
        this.sz = sz;
    }

    @Override
    public String toString() {
        return "Config{" +
                "id='" + id + '\'' +
                ", item='" + predictionKey + '\'' +
                ", key='" + predictionTs + '\'' +
                ", instId='" + instId + '\'' +
                ", desc='" + sz + '\'' +
                '}';
    }

    public BigDecimal getProbability() {
        return probability;
    }

    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }

    public Long getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Long createTs) {
        this.createTs = createTs;
    }

    public BigDecimal getCreatePrice() {
        return createPrice;
    }

    public void setCreatePrice(BigDecimal createPrice) {
        this.createPrice = createPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}