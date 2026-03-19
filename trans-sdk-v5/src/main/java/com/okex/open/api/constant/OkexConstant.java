package com.okex.open.api.constant;

public class OkexConstant {

    public final static String ENDPOINT = "https://www.okx.com";
    public final static String WS_PRIVATE_SERVICE_URL = "wss://ws.okx.com:8443/ws/v5/private";
    public final static String WS_PUBLIC_SERVICE_URL = "wss://ws.okx.com:8443/ws/v5/public";
    public final static String WS_BUSINESS_SERVICE_URL = "wss://ws.okx.com:8443/ws/v5/business";

    public final static String CODE_SUCCESS = "0";
    public final static String CODE_FAIL = "1";
    public static String EVENT_PONG = "pong";
    public static String EVENT_LOGIN = "login";
    public static String EVENT_ERROR = "error";
    public static String EVENT_SUBSCRIBE = "subscribe";
    public static String CHANNEL_TICKERS = "tickers";
    public static String CHANNEL_BOOKS = "books5";
    public static String CHANNEL_ACCOUNT = "account";
    public static String CHANNEL_FUNDING_RATE = "funding-rate";
    public static String CHANNEL_POSITIONS = "positions";
    public static String CHANNEL_ORDERS = "orders";
    public static String CHANNEL_CANDLE15m = "candle15m";
    public static String CHANNEL_CANDLE5m = "candle5m";
    public static String CHANNEL_CANDLE1H = "candle1H";
    public static String CHANNEL_CANDLE1D = "candle1D";
    public static String OP_AMEND_ORDER = "amend-order";
    public static String OP_ORDER = "amend-order";

    public static String ERROR_CODE = "51008";
}
