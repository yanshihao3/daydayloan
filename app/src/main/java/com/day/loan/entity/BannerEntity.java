package com.day.loan.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/5/23 下午4:55
 * - @Email whynightcode@gmail.com
 */
public class BannerEntity implements Parcelable {

    private String advername;
    private String pictrue;
    private String app;

    public BannerEntity() {
    }

    protected BannerEntity(Parcel in) {
        advername = in.readString();
        pictrue = in.readString();
        app = in.readString();
    }

    public BannerEntity(String advername, String pictrue, String app) {
        this.advername = advername;
        this.pictrue = pictrue;
        this.app = app;
    }

    public static final Creator<BannerEntity> CREATOR = new Creator<BannerEntity>() {
        @Override
        public BannerEntity createFromParcel(Parcel in) {
            return new BannerEntity(in);
        }

        @Override
        public BannerEntity[] newArray(int size) {
            return new BannerEntity[size];
        }
    };

    public String getAdvername() {
        return advername;
    }

    public void setAdvername(String advername) {
        this.advername = advername;
    }

    public String getPictrue() {
        return pictrue;
    }

    public void setPictrue(String pictrue) {
        this.pictrue = pictrue;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(advername);
        dest.writeString(pictrue);
        dest.writeString(app);
    }
}
