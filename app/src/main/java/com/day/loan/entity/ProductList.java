package com.day.loan.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/1 下午5:33
 * - @Email whynightcode@gmail.com
 */
public class ProductList implements Parcelable {

    private List<ProductEntity> mList;

    public ProductList() {
        mList = new ArrayList<>();
    }

    protected ProductList(Parcel in) {
        mList = in.createTypedArrayList(ProductEntity.CREATOR);
    }

    public static final Creator<ProductList> CREATOR = new Creator<ProductList>() {
        @Override
        public ProductList createFromParcel(Parcel in) {
            return new ProductList(in);
        }

        @Override
        public ProductList[] newArray(int size) {
            return new ProductList[size];
        }
    };

    public List<ProductEntity> getList() {
        return mList;
    }

    public void add(ProductEntity productEntity) {
        mList.add(productEntity);
    }

    public void addAll(List<ProductEntity> list) {
        mList.addAll(list);
    }

    public void clear() {
        mList.clear();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mList);
    }
}
