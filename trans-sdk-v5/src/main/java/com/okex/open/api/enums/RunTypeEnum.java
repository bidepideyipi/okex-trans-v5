package com.okex.open.api.enums;


public enum RunTypeEnum {
    /**
     * ✅ 优势：在区间内成交次数多，资金利用率高。
     * ❌ 劣势：如果价格暴涨暴跌，容易“卖飞”或“套牢”，网格失效快。
     * 等差：Arithmetic Progression (AP)
     */
    AP("1"),
    /**
     * ✅ 优势：适应性更强，不容易因价格翻倍而网格中断。
     * ❌ 劣势：低价区网格太密（成交频繁），高价区网格太疏（资金占用大）。
     * 等比：Geometric Progression (GP)
     */
    GP("2"),

    /**
     * 如果你预测未来是“横盘”，选等差；如果预测是“大波动/趋势”，选等比。
     */
    ;

    private String v;

    RunTypeEnum(String v) {
        this.v = v;
    }

    public String v() {
        return v;
    }
}
