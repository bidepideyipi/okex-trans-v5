package com.okex.open.api.bean.resp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.okex.open.api.constant.OkexConstant;
import com.okex.open.api.bean.view.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OkxApiResp {

    private String code;
    private String msg;
    private JSONArray data;

    public List<InstrumentView> instruments(){
        List<InstrumentView> list = new ArrayList<>();
        if(!OkexConstant.CODE_SUCCESS.equals(code)){
            return list;
        }

        return JSONArray.parseArray(data.toJSONString(), InstrumentView.class);
    }

    public List<OrderView> orders(){
        if(!OkexConstant.CODE_SUCCESS.equals(code)){
            return new ArrayList<>();
        }

        return JSON.parseArray(data.toJSONString(), OrderView.class);
    }

    public List<GridOrderView> gridOrders(){
        if(!OkexConstant.CODE_SUCCESS.equals(code)){
            return new ArrayList<>();
        }

        return JSON.parseArray(data.toJSONString(), GridOrderView.class);
    }

    public TickerView tickerView(){
        if(!OkexConstant.CODE_SUCCESS.equals(code) || data.isEmpty()){
            return null;
        }

        return JSONObject.parseObject(data.getString(0), TickerView.class);
    }

    public List<PositionView> positions(){
        if(!OkexConstant.CODE_SUCCESS.equals(code)){
            return new ArrayList<>();
        }

        return JSON.parseArray(data.toJSONString(), PositionView.class);
    }
}
