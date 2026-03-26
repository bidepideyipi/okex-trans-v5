package com.supermancell.okex.trans.trader.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.okex.open.api.bean.view.OrderView;
import com.okex.open.api.config.APIConfiguration;
import com.okex.open.api.enums.I18nEnum;
import com.okex.open.api.enums.InstType;
import com.okex.open.api.enums.OrderType;
import com.okex.open.api.service.trade.TradeAPIService;
import com.okex.open.api.service.trade.impl.TradeAPIServiceImpl;
import com.supermancell.okex.trans.trader.model.DocConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import  com.okex.open.api.bean.resp.OkxApiResp;

@Service
public class OkxSwapOrderService {

    private final ConfigService configService;
    private TradeAPIService tradeAPIService;

    public OkxSwapOrderService(ConfigService configService) {
        this.configService = configService;

        Optional<DocConfig>  configApiKey = configService.getConfig("okexAccount", "api_key");
        Optional<DocConfig>  configSecretKey = configService.getConfig("okexAccount", "secret_key");
        Optional<DocConfig>  configPwd = configService.getConfig("okexAccount", "passphrase");

        APIConfiguration config = new APIConfiguration();
        config.setEndpoint("https://www.okx.com/");
        config.setApiKey(configApiKey.get().getValue());
        config.setSecretKey(configSecretKey.get().getValue());
        config.setPassphrase(configPwd.get().getValue());
        //config.setPrint(true);
        /* config.setI18n(I18nEnum.SIMPLIFIED_CHINESE);*/
        config.setI18n(I18nEnum.ENGLISH);

        this.tradeAPIService = new TradeAPIServiceImpl(config);
    }

    /**
     * GetPending Order
     * 限速：60次/2s
     * @param instId
     * @param orderType
     * @return
     */
    public List<OrderView> getSwapOrderList(String instId, OrderType orderType) {
        JSONObject res  = tradeAPIService.getOrderList(InstType.SWAP.v(), null, instId, orderType.v(), null, null, null, null);
        OkxApiResp okxApiResp = JSON.parseObject(res.toJSONString(), OkxApiResp.class);
        return okxApiResp.orders();
    }
}
