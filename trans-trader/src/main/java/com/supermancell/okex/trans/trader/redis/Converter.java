package com.supermancell.okex.trans.trader.redis;

import com.alibaba.fastjson.JSONObject;
import com.supermancell.okex.trans.trader.model.Signal;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter {
    public static Signal mapToSignal(Map<?, ?> msg) {
        Signal signal = new Signal();
        signal.setInstId(msg.get("inst_id").toString());
        signal.setBar(msg.get("bar").toString());
        signal.setTimestamp( Long.parseLong(msg.get("timestamp").toString()));
        signal.setPrediction(msg.get("prediction").toString());
        signal.setProbability(JSONObject.parseObject(msg.get("probabilities").toString(), Map.class));
        signal.setPredictionHigh(msg.get("prediction_high").toString());
        signal.setProbabilityHigh(JSONObject.parseObject(msg.get("probabilities_high").toString(), Map.class));
        signal.setPredictionLow(msg.get("prediction_low").toString());
        signal.setProbabilityLow(JSONObject.parseObject(msg.get("probabilities_low").toString(), Map.class));
        signal.setLine1(new BigDecimal(msg.get("line1").toString()));
        signal.setLine2(new BigDecimal(msg.get("line2").toString()));
        signal.setPrice(new BigDecimal(msg.get("price").toString()));
        signal.setLabel(msg.get("prediction_label").toString());
        signal.setLabelHigh(msg.get("prediction_high_label").toString());
        signal.setLabelLow(msg.get("prediction_low_label").toString());
        return signal;
    }
}
