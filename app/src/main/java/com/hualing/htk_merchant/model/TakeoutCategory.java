package com.hualing.htk_merchant.model;

/**
 * 外卖产品类别实体
 */

public class TakeoutCategory {

    private Integer id;

    private String categoryName; //类别名字

    private String description;  //说明

    private Integer shopId;  //店铺id

    private Boolean show;  //显示

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}