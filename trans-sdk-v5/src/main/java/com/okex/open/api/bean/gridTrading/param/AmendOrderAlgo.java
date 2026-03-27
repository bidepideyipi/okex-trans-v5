package com.okex.open.api.bean.gridTrading.param;

public class AmendOrderAlgo {
    private String algoId;
    private String instId;
    private String slTriggerPx;//止损触发价
    private String tpTriggerPx;//止盈触发价
    private String tpRatio;//止盈比例
    private String slRatio;//止损比例
    private String triggerAction;
    private String triggerStrategy;
    private String stopType;

    public String getAlgoId() {
        return algoId;
    }

    public void setAlgoId(String algoId) {
        this.algoId = algoId;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getSlTriggerPx() {
        return slTriggerPx;
    }

    public void setSlTriggerPx(String slTriggerPx) {
        this.slTriggerPx = slTriggerPx;
    }

    public String getTpTriggerPx() {
        return tpTriggerPx;
    }

    public void setTpTriggerPx(String tpTriggerPx) {
        this.tpTriggerPx = tpTriggerPx;
    }

    public String getTpRatio() {
        return tpRatio;
    }

    public void setTpRatio(String tpRatio) {
        this.tpRatio = tpRatio;
    }

    public String getSlRatio() {
        return slRatio;
    }

    public void setSlRatio(String slRatio) {
        this.slRatio = slRatio;
    }

    public String getTriggerAction() {
        return triggerAction;
    }

    public void setTriggerAction(String triggerAction) {
        this.triggerAction = triggerAction;
    }

    public String getStopType() {
        return stopType;
    }

    public void setStopType(String stopType) {
        this.stopType = stopType;
    }

    public String getTriggerStrategy() {
        return triggerStrategy;
    }

    public void setTriggerStrategy(String triggerStrategy) {
        this.triggerStrategy = triggerStrategy;
    }

}
