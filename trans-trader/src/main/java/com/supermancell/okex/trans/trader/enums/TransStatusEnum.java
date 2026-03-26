package com.supermancell.okex.trans.trader.enums;


public enum TransStatusEnum {

    STARING("starting"),
    RUNNING("running"),
    STOPPING("stopping"),
    PENDING_SIGNAL("pending_signal"),
    NO_CLOSE_POSITION("no_close_position"),
    CLOSED("closed"),
    ;

    private String v;

    TransStatusEnum(String v) {
        this.v = v;
    }

    public String v() {
        return v;
    }
}
