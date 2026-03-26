package com.okex.open.api.enums;


public enum AlgOrdTypeEnum {

    GRID("grid"), //现货网格
    CONTRACT_GRID("contract_grid"),//合约网格委托
    ;

    private String v;

    AlgOrdTypeEnum(String v) {
        this.v = v;
    }

    public String v() {
        return v;
    }
}
