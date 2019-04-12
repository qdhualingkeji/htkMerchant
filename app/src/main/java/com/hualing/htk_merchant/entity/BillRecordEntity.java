package com.hualing.htk_merchant.entity;

import java.io.Serializable;
import java.util.List;

public class BillRecordEntity {

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

        private String orderNumber;   //关联的订单id

        private Double orderIncome;  //订单收入金额

        private Double spendingOnOrder;  //订单支出金额

        private Double amount;  //结算金额

        private Integer status;  //状态码：0未入账，1已入账

        private String accountShopToken;

        private Integer serialNumber;  //序号

        private Integer totalSerialNumber;  //总序号

        private String gmtCreate;

        private String gmtModified;

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

        public Double getOrderIncome() {
            return orderIncome;
        }

        public void setOrderIncome(Double orderIncome) {
            this.orderIncome = orderIncome;
        }

        public Double getSpendingOnOrder() {
            return spendingOnOrder;
        }

        public void setSpendingOnOrder(Double spendingOnOrder) {
            this.spendingOnOrder = spendingOnOrder;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getAccountShopToken() {
            return accountShopToken;
        }

        public void setAccountShopToken(String accountShopToken) {
            this.accountShopToken = accountShopToken;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(String gmtModified) {
            this.gmtModified = gmtModified;
        }

        public int getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(int serialNumber) {
            this.serialNumber = serialNumber;
        }

        public Integer getTotalSerialNumber() {
            return totalSerialNumber;
        }

        public void setTotalSerialNumber(Integer totalSerialNumber) {
            this.totalSerialNumber = totalSerialNumber;
        }
    }
}
