package com.hualing.htk_merchant.model;

import java.util.List;

/**
 * 外卖产品实体类
 */

public class TakeoutProduct {

    private boolean showCB;//是否显示复选框（这个属性是为了方便app端操作特意附加的）

    public boolean isShowCB() {
        return showCB;
    }

    public void setShowCB(boolean showCB) {
        this.showCB = showCB;
    }

    private boolean checkedCB;

    public boolean isCheckedCB() {
        return checkedCB;
    }

    public void setCheckedCB(boolean checkedCB) {
        this.checkedCB = checkedCB;
    }

    private int mark;

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    private Integer id;

    private String imgUrl;

    private String productName;  //名称

    private String description;  //说明

    private String label;  //标签

    private String time; //售卖时间

    private String property; //属性

    private Integer monthlySalesVolume;  //月售量

    private Double favorableRate;  //好评率

    private Double price;  //价格

    private double priceCanhe;

    private Integer inventory;  //现库存

    private Integer inventoryCount;   //库存总量

    private Integer integral;  //积分数

    private Integer categoryId;  //类别id

    private Integer shopId;  //店铺id

    private List<TakeoutCategory> categoryList;

    private Integer ifCanBuy ;

    public Integer getIfCanBuy() {
        return ifCanBuy;
    }

    public void setIfCanBuy(Integer ifCanBuy) {
        this.ifCanBuy = ifCanBuy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getMonthlySalesVolume() {
        return monthlySalesVolume = 20;
    }

    public void setMonthlySalesVolume(Integer monthlySalesVolume) {
        this.monthlySalesVolume = monthlySalesVolume;
    }

    public Double getFavorableRate() {
        return favorableRate = 4.2;
    }

    public void setFavorableRate(Double favorableRate) {
        this.favorableRate = favorableRate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Integer getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(Integer inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public List<TakeoutCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<TakeoutCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public double getPriceCanhe() {
        return priceCanhe;
    }

    public void setPriceCanhe(double priceCanhe) {
        this.priceCanhe = priceCanhe;
    }
}