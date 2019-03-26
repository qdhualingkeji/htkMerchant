package com.hualing.htk_merchant.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LoginUserEntity {

    private int code;
    private String message;
    private LoginUserEntity.DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        private Integer userId;

        private String userName;

        private String password;

        private String email;

        private String token;

        private String saltToken;

        private String encryptToken;

        private boolean pcLoginState;  //电脑网页登陆状态  true在线　　false离线

        private String role;

        private Integer accountStatus;

        private short loginWay;

        private String avatarImg; //头像url

        private String useStartTime;  //商户使用时间

        private String useEndTime;  //商户使用时间

        private String nickName; //呢称

        private boolean rememberMe;  //是否住当前登陆用户

        private String shopName; //店铺名字

        private int state;  //店铺状态
        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        /**
         * 创建时间
         */
        private Date createTime;
        /**
         * 最后登录时间
         */
        private Date lastLoginTime;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getEncryptToken() {
            return encryptToken;
        }

        public void setEncryptToken(String encryptToken) {
            this.encryptToken = encryptToken;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getSaltToken() {
            return saltToken;
        }

        public void setSaltToken(String saltToken) {
            this.saltToken = saltToken;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(Integer accountStatus) {
            this.accountStatus = accountStatus;
        }

        public short getLoginWay() {
            return loginWay;
        }

        public void setLoginWay(short loginWay) {
            this.loginWay = loginWay;
        }

        public boolean getPcLoginState() {
            return pcLoginState;
        }

        public void setPcLoginState(boolean pcLoginState) {
            this.pcLoginState = pcLoginState;
        }

        public String getAvatarImg() {
            return avatarImg;
        }

        public void setAvatarImg(String avatarImg) {
            this.avatarImg = avatarImg;
        }

        public String getUseStartTime() {
            return useStartTime;
        }

        public void setUseStartTime(String useStartTime) {
            this.useStartTime = useStartTime;
        }

        public String getUseEndTime() {
            return useEndTime;
        }

        public void setUseEndTime(String useEndTime) {
            this.useEndTime = useEndTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Date getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(Date lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public boolean getRememberMe() {
            return rememberMe;
        }

        public void setRememberMe(boolean rememberMe) {
            this.rememberMe = rememberMe;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }
    }
}
