package com.supermancell.okex.trans.trader.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.okex.open.api.bean.gridTrading.param.OrderAlgo;
import com.okex.open.api.bean.gridTrading.param.StopOrderAlgo;
import com.okex.open.api.bean.resp.OkxApiResp;
import com.okex.open.api.bean.view.GridOrderView;
import com.okex.open.api.bean.view.OrderView;
import com.okex.open.api.bean.view.TickerView;
import com.okex.open.api.config.APIConfiguration;
import com.okex.open.api.enums.*;
import com.okex.open.api.service.gridTrading.GridTradingAPIService;
import com.okex.open.api.service.gridTrading.impl.GridTradingAPIServiceImpl;
import com.okex.open.api.service.marketData.MarketDataAPIService;
import com.okex.open.api.service.marketData.impl.MarketDataAPIServiceImpl;
import com.supermancell.okex.trans.trader.model.DocConfig;
import com.supermancell.okex.trans.trader.model.DocNormalizer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OkxGridOrderService {

    private final ConfigService configService;
    private GridTradingAPIService tradeAPIService;
    private MarketDataAPIService marketDataAPIService;
    private final NormalizerService normalizerService;

    public OkxGridOrderService(ConfigService configService, NormalizerService normalizerService) {
        this.configService = configService;
        this.normalizerService = normalizerService;

        Optional<DocConfig> configApiKey = configService.getConfig("okexAccount", "api_key");
        Optional<DocConfig> configSecretKey = configService.getConfig("okexAccount", "secret_key");
        Optional<DocConfig> configPwd = configService.getConfig("okexAccount", "passphrase");

        APIConfiguration config = new APIConfiguration();
        config.setEndpoint("https://www.okx.com/");
        config.setApiKey(configApiKey.get().getValue());
        config.setSecretKey(configSecretKey.get().getValue());
        config.setPassphrase(configPwd.get().getValue());
        //config.setPrint(true);
        /* config.setI18n(I18nEnum.SIMPLIFIED_CHINESE);*/
        config.setI18n(I18nEnum.ENGLISH);

        this.tradeAPIService = new GridTradingAPIServiceImpl(config);
        this.marketDataAPIService = new MarketDataAPIServiceImpl(config);
    }

    public TickerView getTicker(String instId) {
        JSONObject res = this.marketDataAPIService.getTicker(instId);
        OkxApiResp okxApiResp = JSON.parseObject(res.toJSONString(), OkxApiResp.class);
        return okxApiResp.tickerView();
    }

    /**
     * GetPending Order
     * 限速：20次/2s
     *
     * @param algId
     * @param instId
     * @return
     */
    public List<GridOrderView> getOrder(String algId, String instId) {
        JSONObject res = tradeAPIService.getGridAlgoOrderList(AlgOrdTypeEnum.CONTRACT_GRID.v(), algId, instId, InstType.SWAP.v(), null, null, null);
        OkxApiResp okxApiResp = JSON.parseObject(res.toJSONString(), OkxApiResp.class);
        return okxApiResp.gridOrders();
    }

    public DocNormalizer getCloseNormalizer(String instId) {
        return normalizerService.findByInstIdAndBarAndColumn(instId, "1H", "close").get();
    }


    /**
     * 下单方法，用于创建网格订单
     * @param instId 交易产品ID
     * @param maxPx 最高价格
     * @param minPx 最低价格
     * @param gridNum 网格数量
     * @param line 止盈止损比例
     * @param sz 订单大小
     * @param direction 交易方向
     * @return 返回下单结果的JSONObject
     */
    public JSONObject placeOrder(String instId, String maxPx, String minPx, String gridNum, String line, String sz, String direction) {
        {
        // 创建算法订单对象
            OrderAlgo orderAlgo = new OrderAlgo();
        // 设置交易产品ID
            orderAlgo.setInstId(instId);
        // 设置算法订单类型为合约网格
            orderAlgo.setAlgoOrdType(AlgOrdTypeEnum.CONTRACT_GRID.v());
        // 设置最高价格
            orderAlgo.setMaxPx(maxPx);
        // 设置最低价格
            orderAlgo.setMinPx(minPx);
        // 设置网格数量
            orderAlgo.setGridNum(gridNum);
        // 设置运行类型为网格交易
            orderAlgo.setRunType(RunTypeEnum.GP.v());
//            orderAlgo.setTpTriggerPx(tpTriggerPx);//止盈触发价
//            orderAlgo.setSlTriggerPx(slTriggerPx);//止损触发价
            orderAlgo.setTag("");

            //现货网格
//        orderAlgo.setQuoteSz("");
//        orderAlgo.setBaseSz("");

            //合约网格
            orderAlgo.setSz(sz);
            orderAlgo.setDirection(direction);
            orderAlgo.setLever(LeverEnum.ten.v());
            orderAlgo.setTpRatio(line);
            orderAlgo.setSlRatio(line);
            //orderAlgo.setBasePos("");

            JSONObject result = this.tradeAPIService.orderAlgo(orderAlgo);
            return result;

        }
    }

    public JSONObject closeOrder(String algId, String instId) {
        StopOrderAlgo orderAlgo = new StopOrderAlgo();
        orderAlgo.setAlgoId(algId);
        orderAlgo.setInstId(instId);
        orderAlgo.setAlgoOrdType(AlgOrdTypeEnum.CONTRACT_GRID.v());
        orderAlgo.setStopType("1");
        JSONObject result = this.tradeAPIService.stopOrderAlgo(orderAlgo);
        return result;
    }
}
