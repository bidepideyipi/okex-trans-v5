package com.supermancell.okex.trans.trader.model;

import com.okex.open.api.bean.view.GridOrderView;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "prediction_order")
public class DocPredictionOrder {
    @Id
    private String id;
    private String instId;
    private String orderStatus;//运行状态
    private String algoId;//网格策略ID
    private LocalDateTime beginTime;//开始时间
    private Signal signal;
    private BigDecimal difference;//差价
}