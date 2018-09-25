package com.day.loan.net.callback;

import android.os.Handler;

import com.alibaba.fastjson.JSONObject;
import com.day.loan.net.RestCreator;
import com.day.loan.net.loader.LatteLoader;
import com.day.loan.net.loader.LoaderStyle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/5/9 下午6:18
 * - @Email whynightcode@gmail.com
 */
public class RequestCallbacks implements Callback<String> {
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final LoaderStyle LOADER_STYLE;
    private static final Handler HANDLER = new Handler();

    public RequestCallbacks(IRequest request, ISuccess success, IFailure failure, IError error, LoaderStyle style) {
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.LOADER_STYLE = style;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()) {
            if (call.isExecuted()) {
                JSONObject jsonObject = JSONObject.parseObject(response.body());
                int code = jsonObject.getIntValue("error_code");
                if (code == 0) {
                    if (SUCCESS != null) {
                        SUCCESS.onSuccess(response.body());
                    }
                } else {
                    if (ERROR != null) {
                        ERROR.onError(code, jsonObject.getString("error_message"));
                    }
                }
            }
        } else {

            if (ERROR != null) {
                ERROR.onError(response.code(), response.message());
            }
        }

        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }

        onRequestFinish();
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        if (FAILURE != null) {
            FAILURE.onFailure();
        }
        if (REQUEST != null) {
            REQUEST.onRequestEnd();
        }

        onRequestFinish();
    }

    private void onRequestFinish() {
        final long delayed = 1000;
        if (LOADER_STYLE != null) {
            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    RestCreator.getParams().clear();
                    LatteLoader.stopLoading();
                }
            }, delayed);
        }
    }
}