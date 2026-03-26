package com.supermancell.okex.trans.trader.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class Signal  implements Serializable {
    private String messageId;
    private long timestamp;
    private String instId;
    private String bar;
    private String prediction;
    private String label;
    private Map<String, BigDecimal> probability;
    private String predictionHigh;
    private String labelHigh;
    private Map<String, BigDecimal> probabilityHigh;
    private String predictionLow;
    private String labelLow;
    private Map<String, BigDecimal> probabilityLow;
    private BigDecimal price;
    private BigDecimal line1;
    private BigDecimal line2;
}
