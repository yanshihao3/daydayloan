package com.day.loan.net.interceptor;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/5/10 下午3:26
 * - @Email whynightcode@gmail.com
 */
public abstract class BaseInterceptor implements Interceptor {
    @Override
    public abstract Response intercept(Chain chain) throws IOException;

    protected LinkedHashMap<String, String> getUrlParameters(Chain chain) {
        HttpUrl url = chain.request().url();
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        int i = url.querySize();
        for (int j = 0; j < i; j++) {
            parameters.put(url.queryParameterName(j), url.queryParameterValue(j));
        }
        return parameters;
    }

    protected LinkedHashMap<String, String> getBobyParameters(Chain chain) {
        FormBody formBody = (FormBody) chain.request().body();
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        int size = formBody.size();
        for (int i = 0; i < size; i++) {
            parameters.put(formBody.name(i), formBody.value(i));
        }
        return parameters;
    }


    protected String getUrlParams(Chain chain, String name) {
        HttpUrl url = chain.request().url();
        return url.queryParameter(name);
    }

    protected String getBobyParams(Chain chain, String key) {
        return getBobyParameters(chain).get(key);
    }
}
