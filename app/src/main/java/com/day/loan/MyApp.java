package com.day.loan;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.commonsdk.UMConfigure;
import com.day.loan.net.Contacts;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/5/23 下午1:01
 * - @Email whynightcode@gmail.com
 */
public class MyApp extends Application {


    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initOkGo();
    }

    public static Context getApplication() {
        return mContext;
    }

    private void initOkGo() {

        AVOSCloud.initialize(this, Contacts.LEAN_ID, Contacts.LEAN_KEY);
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        UMConfigure.init(this, Contacts.UMENG_KEY, channel,
                UMConfigure.DEVICE_TYPE_PHONE, "");
        HttpParams params = new HttpParams();
        String name = getResources().getString(R.string.appName);
        params.put("market", channel);
        params.put("name", name);
        OkGo.getInstance().init(this)
                .addCommonParams(params);
    }
}
