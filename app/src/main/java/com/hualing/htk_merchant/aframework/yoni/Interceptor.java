package com.hualing.htk_merchant.aframework.yoni;

public interface Interceptor {
	boolean intercept(RequestParams params, NetResponse result);
}
