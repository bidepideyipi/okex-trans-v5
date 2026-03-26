package com.supermancell.okex.trans.trader.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.okex.open.api.bean.gridTrading.param.OrderAlgo;
import com.okex.open.api.bean.resp.OkxApiResp;
import com.okex.open.api.bean.view.OrderView;
import com.okex.open.api.config.APIConfiguration;
import com.okex.open.api.enums.*;
import com.okex.open.api.service.gridTrading.GridTradingAPIService;
import com.okex.open.api.service.gridTrading.impl.GridTradingAPIServiceImpl;
import com.supermancell.okex.trans.trader.model.DocConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OkxGridOrderService {

    private final ConfigService configService;
    private GridTradingAPIService tradeAPIService;

    public OkxGridOrderService(ConfigService configService) {
        this.configService = configService;

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
    }

    /**
     * GetPending Order
     * 限速：20次/2s
     *
     * @param algId
     * @param instId
     * @return
     */
    public List<OrderView> getOrder(String algId, String instId) {
        JSONObject res = tradeAPIService.getGridAlgoOrderList(AlgOrdTypeEnum.CONTRACT_GRID.v(), algId, instId, InstType.SWAP.v(), null, null, null);
        OkxApiResp okxApiResp = JSON.parseObject(res.toJSONString(), OkxApiResp.class);
        return okxApiResp.orders();
    }

    public JSONObject placeOrder(String instId, String maxPx, String minPx, String gridNum, String tpTriggerPx, String slTriggerPx,
                                 String sz, String direction) {
        {
            OrderAlgo orderAlgo = new OrderAlgo();
            orderAlgo.setInstId(instId);
            orderAlgo.setAlgoOrdType(AlgOrdTypeEnum.CONTRACT_GRID.v());
            orderAlgo.setMaxPx(maxPx);
            orderAlgo.setMinPx(minPx);
            orderAlgo.setGridNum(gridNum);
            orderAlgo.setRunType(RunTypeEnum.GP.v());
            orderAlgo.setTpTriggerPx(tpTriggerPx);//止盈触发价
            orderAlgo.setSlTriggerPx(slTriggerPx);//止损触发价
            orderAlgo.setTag("");

            //现货网格
//        orderAlgo.setQuoteSz("");
//        orderAlgo.setBaseSz("");

            //合约网格
            orderAlgo.setSz(sz);
            orderAlgo.setDirection(direction);
            orderAlgo.setLever(LeverEnum.ten.v());
            //orderAlgo.setBasePos("");

            JSONObject result = this.tradeAPIService.orderAlgo(orderAlgo);
            return result;

        }
    }
}
