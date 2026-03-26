package com.okex.open.api.bean.view;

import lombok.Data;

@Data
public class GridOrderView {

    private String algoId;
    private String algoClOrdId;
    private String instType;
    private String instId;
    private long uTime;
    private long cTime;
    private String algoOrdType;
    private String state;
    private String stopType;
    private String maxPx;
    private String minPx;
    private String gridNum;
    private String runType;
    private String tpTriggerPx;
    private String slTriggerPx;
    private String arbitrageNum;
    private String totalPnl;
    private String pnlRatio;
    private String investment;
    private String gridProfit;
    private String floatProfit;
    private String cancelType;
    private String quoteSz;
    private String baseSz;
    private String direction;
    private String basePos;
    private String sz;
    private String lever;
    private String actualLever;
    private String liqPx;
    private String uly;
    private String instFamily;
    private String ordFrozen;
    private String availEq;
    private String tag;
    private String profitSharingRatio;
    private String copyType;
    private String fee;
    private String fundingFee;
    private String tradeQuoteCcy;
}
