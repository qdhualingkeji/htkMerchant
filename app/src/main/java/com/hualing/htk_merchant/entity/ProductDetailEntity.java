package com.hualing.htk_merchant.entity;

import com.hualing.htk_merchant.model.TakeoutCategory;
import com.hualing.htk_merchant.model.TakeoutProduct;

import java.io.Serializable;
import java.util.List;

public class ProductDetailEntity {

    private int code;
    private String message;
    private ProductDetail data;

    public ProductDetail getData() {
        return data;
    }

    public void setData(ProductDetail data) {
        this.data = data;
    }

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

    public static class ProductDetail implements Serializable {
        List<TakeoutCategory> data;
        TakeoutProduct dataPro;
        public List<TakeoutCategory> getData() {
            return data;
        }
        public void setData(List<TakeoutCategory> data) {
            this.data = data;
        }
        public TakeoutProduct getDataPro() {
            return dataPro;
        }
        public void setDataPro(TakeoutProduct dataPro) {
            this.dataPro = dataPro;
        }

        public ProductDetail(List<TakeoutCategory> data,TakeoutProduct dataPro) {
            this.data=data;
            this.dataPro=dataPro;
        }
    }
}
