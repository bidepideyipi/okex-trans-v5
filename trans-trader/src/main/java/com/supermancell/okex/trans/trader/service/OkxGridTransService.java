package com.supermancell.okex.trans.trader.service;

import com.alibaba.fastjson.JSONObject;
import com.okex.open.api.bean.view.GridOrderView;
import com.okex.open.api.bean.view.OrderView;
import com.okex.open.api.bean.view.PositionView;
import com.okex.open.api.bean.view.TickerView;
import com.okex.open.api.service.marketData.MarketDataAPIService;
import com.okex.open.api.service.trade.TradeAPIService;
import com.supermancell.okex.trans.trader.enums.Prediction;
import com.supermancell.okex.trans.trader.enums.TransStatusEnum;
import com.supermancell.okex.trans.trader.event.CreateOrderEvent;
import com.supermancell.okex.trans.trader.event.HasPosEvent;
import com.supermancell.okex.trans.trader.model.DocNormalizer;
import com.supermancell.okex.trans.trader.model.DocPredictionOrder;
import com.supermancell.okex.trans.trader.model.Signal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OkxGridTransService {

    private final PredictionOrderService predictionOrderService;
    private final ApplicationEventPublisher eventPublisher;
    private final OkxGridOrderService okxGridOrderService;
    private final BigDecimal keepBelieveLine = new BigDecimal("0.7");
    private static final Logger logger = LoggerFactory.getLogger(OkxGridTransService.class);

    public OkxGridTransService(PredictionOrderService predictionOrderService, OkxGridOrderService okxGridOrderService, ApplicationEventPublisher eventPublisher) {
        this.predictionOrderService = predictionOrderService;
        this.eventPublisher = eventPublisher;
        this.okxGridOrderService = okxGridOrderService;
    }

    public boolean acceptSign(Signal signal){
        checkOrder(signal);
        return true;
    }

    private void checkOrder(Signal signal){

        int p = Integer.parseInt(signal.getPrediction());
        BigDecimal probability = signal.getProbability().get(signal.getPrediction());

        List<DocPredictionOrder>  list = predictionOrderService.findByOrderStatus(TransStatusEnum.RUNNING.v());
        /**
         *  有订单的处理机制
         */
        if(list.size() >0){
            for(DocPredictionOrder o: list) {
                List<GridOrderView> gridOrders = okxGridOrderService.getOrder(o.getAlgoId(), o.getInstId());
                if(gridOrders.size() == 0){
                    o.setOrderStatus(TransStatusEnum.CLOSED.v());
                    //线上已经没有这个订单了
                    predictionOrderService.saveOrUpdate(o);
                    continue;
                }

                /**
                 * 经过了DB和线上请求的校验，确信有订单在运行了
                 */
                for(GridOrderView order: gridOrders){
                    //做多但是信号转变
                    if("long".equals(order.getDirection()) && p < 3) {
                        //JSONObject res = okxGridOrderService.closeOrder(order.getAlgoId(), order.getInstId());
                        TickerView tickerView = okxGridOrderService.getTicker(order.getInstId());
                        int last  = tickerView.getLast().intValue();
                        JSONObject res = okxGridOrderService.amdOrder(order.getAlgoId(), order.getInstId(), last +"",  (last +1) +"");
                        logger.info("信号转变, 平多订单：{}", res);
                        continue;
                    }

                    if("long".equals(order.getDirection()) && p == 3 ) {
                        int pLow = Integer.parseInt(signal.getPredictionLow());
                        if(pLow < 3 && signal.getProbabilityLow().get(signal.getPredictionLow()).compareTo(keepBelieveLine) > 0){
                            TickerView tickerView = okxGridOrderService.getTicker(order.getInstId());
                            int last  = tickerView.getLast().intValue();
                            JSONObject res = okxGridOrderService.amdOrder(order.getAlgoId(), order.getInstId(), last +"",  (last +1) +"");
                            logger.info("震荡风险，平多订单：{}", res);
                            continue;
                        }
                    }

                    if("short".equals(order.getDirection()) && p > 3) {
                        TickerView tickerView = okxGridOrderService.getTicker(order.getInstId());
                        int last  = tickerView.getLast().intValue();
                        JSONObject res = okxGridOrderService.amdOrder(order.getAlgoId(), order.getInstId(), (last-1) +"",  last +"");
                        logger.info("信号转变, 平空订单：{}", res);
                        continue;
                    }

                    if("short".equals(order.getDirection()) && p == 3 ) {
                        int pHigh = Integer.parseInt(signal.getPredictionHigh());
                        if(pHigh > 1 && signal.getProbabilityHigh().get(signal.getPredictionHigh()).compareTo(keepBelieveLine) > 0){
                            TickerView tickerView = okxGridOrderService.getTicker(order.getInstId());
                            int last  = tickerView.getLast().intValue();
                            JSONObject res = okxGridOrderService.amdOrder(order.getAlgoId(), order.getInstId(), (last-1) +"",  last +"");
                            logger.info("震荡风险，平空订单：{}", res);
                            continue;
                        }
                    }

                    /**
                     *  续期逻辑
                     */
                    if(("long".equals(order.getDirection()) && p == 5 && probability.compareTo(keepBelieveLine) > 0)  ||
                            ("short".equals(order.getDirection()) && p == 1  && probability.compareTo(keepBelieveLine) > 0)  ){
                        TickerView tickerView = okxGridOrderService.getTicker(order.getInstId());
                        String slPx = tickerView.getLast().multiply(new BigDecimal("0.964")).toString();
                        String tpPx = tickerView.getLast().multiply(new BigDecimal("1.036")).toString();
                        JSONObject res = okxGridOrderService.amdOrder(order.getAlgoId(), order.getInstId(), slPx,  tpPx);
                        logger.info("信号持续，续期：{} - {}, {} ", slPx, tpPx, res);
                    }
                }
            }
            return;
        }

        if(signal.getProbability().get(signal.getPrediction()).compareTo(keepBelieveLine) < 0){
            //没有7成把握不会建立订单
            logger.info("没有7成把握不会建立订单");
            return;
        }

        //order.setDifference();
        TickerView tickerView = okxGridOrderService.getTicker(signal.getInstId());
        BigDecimal currentPrice = tickerView.getLast();
        BigDecimal difference = currentPrice.subtract(signal.getPrice());

        DocNormalizer normalizer = okxGridOrderService.getCloseNormalizer(signal.getInstId());
        BigDecimal std = normalizer.getStd();

        String instId = signal.getInstId();
        String maxPx = currentPrice.add(std.multiply(new BigDecimal(0.75))).toString();
        String minPx = currentPrice.subtract(std.multiply(new BigDecimal(0.75))).toString();
        String gridNum = "100";
        String sz = "200";
        String line = signal.getLine2().toString();
        String direction = Prediction.ROCKET.getDirection();

        if(Prediction.CRASH.getValue().equals(signal.getPrediction())){
            direction = Prediction.CRASH.getDirection();
            line = signal.getLine2().toString();
        } else if(Prediction.DECLINE.getValue().equals(signal.getPrediction())){
            direction = Prediction.DECLINE.getDirection();
            line = signal.getLine1().toString();
        } else if(Prediction.ADVANCE.getValue().equals(signal.getPrediction())){
            direction = Prediction.ADVANCE.getDirection();
            line = signal.getLine1().toString();
        }
        JSONObject res = okxGridOrderService.placeOrder(instId, maxPx, minPx, gridNum, line,   sz,  direction);
        if ("0".equals(res.get("code"))) {
            String algoId = res.getJSONArray("data").getJSONObject(0).getString("algoId");
            eventPublisher.publishEvent(new CreateOrderEvent(this, signal, algoId, difference));
        } else {
            logger.warn("网格下单失败：{} - {}", res.get("code"),  res.get("msg"));
        }

    }


}
