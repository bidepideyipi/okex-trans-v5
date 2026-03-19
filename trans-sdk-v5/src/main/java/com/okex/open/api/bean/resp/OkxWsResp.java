package com.okex.open.api.bean.resp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.okex.open.api.bean.view.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OkxWsResp {

    private String event;
    private String code;
    private String msg;
    private JSONArray data;
    private Arg arg;
    private String op;
    private String id;

    /**
     * 行情
     * @return
     */
    public List<TickerView> tickers(){
        if(data == null) {
            return new ArrayList<>();
        }

        return JSONObject.parseArray(data.toJSONString(), TickerView.class);
    }

    /**
     * 通过账户接口拿到usdt数量
     * @return
     */
    public BigDecimal accountAmount(){
        if(data == null || data.size() == 0) {
            return null;
        }

        return data.getJSONObject(0).getBigDecimal("totalEq");
    }

    /**
     * 订单
     * @return
     */
    public List<OrderView> orders(){
        if(data == null || data.size() == 0) {
            return new ArrayList<>();
        }

        return JSONObject.parseArray(data.toJSONString(), OrderView.class);
    }

    public List<PositionView> positions(){
        if(data == null || data.size() == 0) {
            return new ArrayList<>();
        }

        return JSONObject.parseArray(data.toJSONString(), PositionView.class);
    }



    @Data
    public class Arg{
        private String instId;
        private String channel;
        private String ccy;
    }

    public OrderFail orderFail(){
        if(data == null || data.size() == 0) {
            return null;
        }

        return JSONObject.parseObject(data.getString(0), OrderFail.class);

    }

}
