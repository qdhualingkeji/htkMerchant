package com.hualing.htk_merchant.entity;

import com.hualing.htk_merchant.model.TakeoutProduct;

import java.io.Serializable;
import java.util.List;

public class ReturnCategoryAndProductEntity {

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
        private String categoryName; //类别名字

        private String description;  //说明

        private Integer categoryId; //分类id

        private List<TakeoutProduct> takeoutProductList;   //产品集合

        public String getCategoryName() {
            return categoryName;
        }

        public DataBean(String categoryName, String description,Integer categoryId, List<TakeoutProduct> productList) {
            this.categoryName = categoryName;
            this.description = description;
            this.categoryId = categoryId;
            this.takeoutProductList = productList;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<TakeoutProduct> getTakeoutProductList() {
            return takeoutProductList;
        }

        public void setTakeoutProductList(List<TakeoutProduct> takeoutProductList) {
            this.takeoutProductList = takeoutProductList;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }
    }
}
