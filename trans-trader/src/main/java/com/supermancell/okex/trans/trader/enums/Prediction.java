package com.supermancell.okex.trans.trader.enums;

import lombok.Getter;

public enum Prediction {
    CRASH("1", "暴跌",  "1",  "short"),
    DECLINE("2", "下跌",  "2",  "short"),
    MOVING_SIDEWAYS("3",  "横盘", "3",  "none"),
    ADVANCE("4", "上涨", "4", "long"),
    ROCKET("5", "暴涨", "5", "long"),

    LOW_CRASH("1", "暴跌(取最低价计算)",  "1", "short"),
    LOW_DECLINE("2", "下跌(取最低价计算)", "2", "short"),
    LOW_MOVING_SIDEWAYS("3",  "横盘(取最低价计算)",  "3", "none"),
    HIGH_MOVING_SIDEWAYS("1",  "横盘(取最高价计算)", "3", "none"),
    HIGH_ADVANCE("2", "上涨(取最高价计算)", "2", "long"),
    HIGH_ROCKET("3", "暴涨(取最高价计算)", "1", "long"),
    ;
    @Getter
    private String code;
    @Getter
    private String desc;
    @Getter
    private String value;
    @Getter
    private String direction;
    Prediction(String code, String desc,  String value, String  direction) {
        this.code = code;
        this.desc = desc;
        this.value = value;
        this.direction = direction;
    }

}
