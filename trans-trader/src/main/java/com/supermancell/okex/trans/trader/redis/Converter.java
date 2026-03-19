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
        signal.setTimestamp( Long.parseLong(msg.get("timestamp").toString()));
        signal.setPrediction(msg.get("prediction").toString());
        signal.setProbability(JSONObject.parseObject(msg.get("probabilities").toString(), Map.class));
        signal.setPredictionHigh(msg.get("prediction_high").toString());
        signal.setProbabilityHigh(JSONObject.parseObject(msg.get("probabilities_high").toString(), Map.class));
        signal.setPredictionLow(msg.get("prediction_low").toString());
        signal.setProbabilityLow(JSONObject.parseObject(msg.get("probabilities_low").toString(), Map.class));

        Pattern pattern = Pattern.compile("\\((-?\\d+(?:\\.\\d+)?)% ~ (-?\\d+(?:\\.\\d+)?)%\\)");
        Matcher matcher = pattern.matcher(msg.get("prediction_label").toString());
        if (matcher.find()) {
            String num1 = matcher.group(1);  // -1.2
            String num2 = matcher.group(2);  // 1.2
            BigDecimal bdLeft = new BigDecimal(num1);
            BigDecimal bdRight = new BigDecimal(num2);
            signal.setPredictionLabelLeft(bdLeft.divide(new BigDecimal(100), 3, BigDecimal.ROUND_HALF_UP));
            signal.setPredictionLabelRight(bdRight.divide(new BigDecimal(100), 3, BigDecimal.ROUND_HALF_UP));
        }

        matcher = pattern.matcher(msg.get("prediction_high_label").toString());
        if (matcher.find()) {
            String num1 = matcher.group(1);  // -1.2
            String num2 = matcher.group(2);  // 1.2
            BigDecimal bdLeft = new BigDecimal(num1);
            BigDecimal bdRight = new BigDecimal(num2);
            signal.setPredictionHighLabelLeft(bdLeft.divide(new BigDecimal(100), 3, BigDecimal.ROUND_HALF_UP));
            signal.setPredictionHighLabelRight(bdRight.divide(new BigDecimal(100), 3, BigDecimal.ROUND_HALF_UP));
        }

        matcher = pattern.matcher(msg.get("prediction_low_label").toString());
        if (matcher.find()) {
            String num1 = matcher.group(1);  // -1.2
            String num2 = matcher.group(2);  // 1.2
            BigDecimal bdLeft = new BigDecimal(num1);
            BigDecimal bdRight = new BigDecimal(num2);
            signal.setPredictionLowLabelLeft(bdLeft.divide(new BigDecimal(100), 3, BigDecimal.ROUND_HALF_UP));
            signal.setPredictionLowLabelRight(bdRight.divide(new BigDecimal(100), 3, BigDecimal.ROUND_HALF_UP));
        }
        return signal;
    }
}
