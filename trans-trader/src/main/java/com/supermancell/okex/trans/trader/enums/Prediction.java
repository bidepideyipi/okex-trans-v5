package com.supermancell.okex.trans.trader.enums;

import lombok.Getter;

public enum Prediction {
    CRASH("1", "暴跌",  "1"),
    DECLINE("2", "下跌",  "2"),
    MOVING_SIDEWAYS("3",  "横盘", "3"),
    ADVANCE("4", "上涨", "4"),
    ROCKET("5", "暴涨", "5"),

    LOW_CRASH("1", "暴跌(取最低价计算)",  "1"),
    LOW_DECLINE("2", "下跌(取最低价计算)", "2"),
    LOW_MOVING_SIDEWAYS("3",  "横盘(取最低价计算)",  "3"),
    HIGH_MOVING_SIDEWAYS("1",  "横盘(取最高价计算)", "3"),
    HIGH_ADVANCE("2", "上涨(取最高价计算)", "2"),
    HIGH_ROCKET("3", "暴涨(取最高价计算)", "1"),
    ;
    @Getter
    private String code;
    @Getter
    private String desc;
    @Getter
    private String value;
    Prediction(String code, String desc,  String value) {
        this.code = code;
        this.desc = desc;
        this.value = value;
    }

}
