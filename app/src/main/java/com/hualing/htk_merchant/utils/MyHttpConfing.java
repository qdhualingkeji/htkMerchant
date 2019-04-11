package com.hualing.htk_merchant.utils;

/**
 * Created by Administrator on 2017/6/15.
 */

public class MyHttpConfing {

    public static final String tag = "TAG-->";

    public static final String baseUrl = "http://120.27.5.36:8080/htkApp/API/";//客户版本外网
    public static final String merchantAppUrl = baseUrl + "merchantAppAPI/";
    //public static final String payUrl = baseUrl + "paymentInterfaceAPI/";
    public static final String riderUrl = baseUrl + "riderAPI/";

    /* 获得新订单数据 */
    public static final String getNewOrderList = merchantAppUrl + "getNewOrderList";
    /*取消订单*/
    public static final String cancelOrder = merchantAppUrl + "callUpRefundInterface";
    /*确认订单*/
    public static final String confirmTheOrder = merchantAppUrl + "confirmTheOrder";
    /*确认已完成订单*/
    public static final String confirmFinishedOrder = merchantAppUrl + "confirmFinishedOrder";
    /*自行配送*/
    public static final String orderItemsToShip = riderUrl + "order/itemsToShip";
    /* 获得已完成订单数据 */
    public static final String getFinishedOrderList = merchantAppUrl + "getFinishedOrderList";
    /* 商户登录 */
    public static final String login = merchantAppUrl + "login";
    /*确认收货*/
    public static final String enterReceipt = riderUrl + "enterReceipt";
    /*获得产品信息*/
    public static final String getProductData = merchantAppUrl + "takeout/product/getProductData";
    /*商品上架*/
    public static final String takeOn = merchantAppUrl + "takeout/product/takeOn";
    /*商品下架*/
    public static final String takeOff = merchantAppUrl + "takeout/product/takeOff";
    /*编辑商品*/
    public static final String saveProduct = merchantAppUrl + "takeout/product/saveProduct";
    /*添加商品*/
    public static final String addProduct = merchantAppUrl + "takeout/product/addProduct";
    /*
    * 前往编辑商品
    * */
    public static final String editProduct = merchantAppUrl + "takeout/product/editProduct";
    /**
     *根据用户id查询类别
     */
    public static final String getCategoryListById = merchantAppUrl + "takeout/getCategoryListById";
    /**
     * 给商家接单app端推送消息
     */
    public static final String sendNotification = merchantAppUrl + "sendNotification";

    /* 用户注册 */
    public static final String registered = baseUrl + "register";
    /* 确认抢单 */
    public static final String confirmQiangDan = baseUrl + "confirmQiangDan";
    /* 确认取货 */
    public static final String confirmQuHuo = baseUrl + "order/itemsToShip";
    /* 获得待取货数据 */
    public static final String getDaiQuHuo = baseUrl + "getDaiQuHuo";
    /* 确认送达 */
    public static final String confirmSongDa = baseUrl + "enterReceipt";
    /* 获得待抢单详情数据 */
    public static final String getDaiQiangDanDetail = baseUrl + "getDaiQiangDanDetail";
    /* 获得待送达数据 */
    public static final String getDaiSongDa = baseUrl + "getDaiSongDa";
}



