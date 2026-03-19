package com.supermancell.okex.trans.trader.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.okex.open.api.bean.resp.OkxApiResp;
import com.okex.open.api.bean.view.PositionView;
import com.okex.open.api.config.APIConfiguration;
import com.okex.open.api.enums.I18nEnum;
import com.okex.open.api.enums.InstType;
import com.okex.open.api.service.account.AccountAPIService;
import com.okex.open.api.service.account.impl.AccountAPI;
import com.okex.open.api.service.account.impl.AccountAPIServiceImpl;
import com.supermancell.okex.trans.trader.model.DocConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OkxPositionService {

    private final ConfigService configService;
    private AccountAPIService accountAPIService;

    public OkxPositionService(ConfigService configService) {
        this.configService = configService;

        Optional<DocConfig> configApiKey = configService.getConfig("okexAccount", "api_key");
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

        this.accountAPIService = new AccountAPIServiceImpl(config);
    }

    /**
     * Get Position
     * @param instId
     * @return
     */
    public List<PositionView> getSwapPositionList(String instId) {
        JSONObject res = accountAPIService.getPositions(InstType.SWAP.v(), instId, "");
        OkxApiResp okxApiResp = JSON.parseObject(res.toJSONString(), OkxApiResp.class);
        return okxApiResp.positions();
    }
}
