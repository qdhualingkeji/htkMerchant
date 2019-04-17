package com.hualing.htk_merchant.entity;

import com.hualing.htk_merchant.model.OrderProduct;

import java.io.Serializable;
import java.util.List;

public class OrderRecordEntity {

    private int code;
    private String message;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        private Integer id;

        private String orderNumber;  //订单号

        private Double orderAmount;  //订单金额

        private Double priceCanhe;  //餐盒费

        private Double deliveryFee;  //配送费

        private String receiptName;  //收货人姓名

        private Integer sex;   //收货人性别

        private String receivingCall;  //收货电话

        private String shippingAddress;  //收货地址

        private Double longitude;   //地址经度

        private Double latitude;   //地址纬度

        private String orderBody;  //订单内容

        private Integer paymentMethod;   //0微信    1支付宝

        private String remark;   //备注

        private Integer mark;  //标记  0是外卖   1是团购

        private Integer orderState;  //订单状态  1:用户下单成功   2:商家接单成功   3:派送中   4:用户收货成功 5取消订单

        private List<OrderProduct> productLists; //产品集合

        private String jsonProductList;  //产品集合json

        private String jsonPackage; //套餐对象json

        private String token;  //app用户token

        private String accountToken;//app用户accountToken

        private Integer shopId;   //店铺id

        private String orderTime;   //下单时间

        private String orderHandle;

        private Integer number;  //序号

        private Integer allNumber;  //总订单列表序号

        private String modifiedTime;  //最后修改时间

        private Integer packageId;   //套餐id

        private Integer quantity;  //数量

        private long voucherNumber;  //消费券码

        private long timeLeft;  //剩余时间

        private String lastUpdateTime;  //最后修改时间

        private Integer couponId ;

        private String distance;

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public Integer getCouponId() {
            return couponId;
        }

        public void setCouponId(Integer couponId) {
            this.couponId = couponId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public Double getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(Double orderAmount) {
            this.orderAmount = orderAmount;
        }

        public Double getPriceCanhe() {
            return priceCanhe;
        }

        public void setPriceCanhe(Double priceCanhe) {
            this.priceCanhe = priceCanhe;
        }

        public Double getDeliveryFee() {
            return deliveryFee;
        }

        public void setDeliveryFee(Double deliveryFee) {
            this.deliveryFee = deliveryFee;
        }

        public String getReceiptName() {
            return receiptName;
        }

        public void setReceiptName(String receiptName) {
            this.receiptName = receiptName;
        }

        public String getReceivingCall() {
            return receivingCall;
        }

        public void setReceivingCall(String receivingCall) {
            this.receivingCall = receivingCall;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public Integer getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(Integer paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public Integer getOrderState() {
            return orderState;
        }

        public void setOrderState(Integer orderState) {
            this.orderState = orderState;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getOrderBody() {
            return orderBody;
        }

        public void setOrderBody(String orderBody) {
            this.orderBody = orderBody;
        }

        public List<OrderProduct> getProductLists() {
            return productLists;
        }

        public void setProductLists(List<OrderProduct> productLists) {
            this.productLists = productLists;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getAccountToken() {
            return accountToken;
        }

        public void setAccountToken(String accountToken) {
            this.accountToken = accountToken;
        }

        public Integer getShopId() {
            return shopId;
        }

        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Integer getSex() {
            return sex;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }

        public String getJsonProductList() {
            return jsonProductList;
        }

        public Integer getMark() {
            return mark;
        }

        public void setMark(Integer mark) {
            this.mark = mark;
        }

        public String getJsonPackage() {
            return jsonPackage;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public String getOrderHandle() {
            return orderHandle;
        }

        public void setOrderHandle(String orderHandle) {
            this.orderHandle = orderHandle;
        }

        public String getModifiedTime() {
            return modifiedTime;
        }

        public void setModifiedTime(String modifiedTime) {
            this.modifiedTime = modifiedTime;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Integer getAllNumber() {
            return allNumber;
        }

        public void setAllNumber(Integer allNumber) {
            this.allNumber = allNumber;
        }

        public Integer getPackageId() {
            return packageId;
        }

        public void setPackageId(Integer packageId) {
            this.packageId = packageId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public long getVoucherNumber() {
            return voucherNumber;
        }

        public void setVoucherNumber(long voucherNumber) {
            this.voucherNumber = voucherNumber;
        }

        public long getTimeLeft() {
            return timeLeft;
        }

        public void setTimeLeft(long timeLeft) {
            this.timeLeft = timeLeft;
        }

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(String lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }
    }
}
