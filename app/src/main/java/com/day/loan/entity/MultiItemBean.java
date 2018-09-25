package com.day.loan.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/27 下午5:39
 * - @Email whynightcode@gmail.com
 */
public class MultiItemBean implements MultiItemEntity {


    private int itemType;

    public MultiItemBean setItemType(int itemType) {
        this.itemType = itemType;
        return this;
    }

    private ProductEntity mProductEntity;

    public MultiItemBean setProductEntity(ProductEntity productEntity) {
        mProductEntity = productEntity;
        return this;
    }

    public ProductEntity getProductEntity() {
        return mProductEntity;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
