package com.supermancell.okex.trans.trader.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class Signal  implements Serializable {
    private long timestamp;
    private String instId;
    private String bar;
    private String prediction;
    private Map<String, BigDecimal> probability;
    private BigDecimal predictionLabelLeft;
    private BigDecimal predictionLabelRight;
    private String predictionHigh;
    private Map<String, BigDecimal> probabilityHigh;
    private BigDecimal predictionHighLabelLeft;
    private BigDecimal predictionHighLabelRight;
    private String predictionLow;
    private Map<String, BigDecimal> probabilityLow;
    private BigDecimal predictionLowLabelLeft;
    private BigDecimal predictionLowLabelRight;

}
