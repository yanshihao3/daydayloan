package com.day.loan.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.day.loan.R;


/**
 * - @Author:  闫世豪
 * - @Time:  2018/7/26 下午4:23
 * - @Email whynightcode@gmail.com
 */
public class ProfileEdit extends LinearLayout {

    private static final String TAG = "ProfileEdit";
    private ImageView mImageView; //图片
    private ImageView mJianto; //箭头
    private TextView mTypeKey;  //类型
    private TextView mValue; //描述

    private boolean isHaveArrows;
    private int mImageRes;
    private String type;
    private String value;

    public ProfileEdit(Context context) {
        this(context, null);
    }

    public ProfileEdit(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileEdit(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.profileEdit);
        isHaveArrows = typedArray.getBoolean(R.styleable.profileEdit_isarrows, false);
        mImageRes = typedArray.getResourceId(R.styleable.profileEdit_lifeIamge, 0);
        value = typedArray.getString(R.styleable.profileEdit_value);
        type = typedArray.getString(R.styleable.profileEdit_type);
        typedArray.recycle();
        initView(context);
    }

    private void initView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.view_profile_edit, this, true);
        mImageView = inflate.findViewById(R.id.profile_icon);
        mTypeKey = inflate.findViewById(R.id.profile_key);
        mValue = inflate.findViewById(R.id.profile_value);
        mJianto = inflate.findViewById(R.id.right_arrow);

        mImageView.setImageResource(mImageRes);
        mTypeKey.setText(type);
        mValue.setText(value);
        Log.e(TAG, "initView: " + type);
        mJianto.setVisibility(isHaveArrows ? VISIBLE : GONE);

    }

    public void setTypeKey(@DrawableRes int res, String type) {
        mTypeKey.setText(type);
        mImageView.setImageResource(res);
    }

    public void setBitmap(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    public void setValue(String value) {
        mValue.setText(value);
    }

    public void setHaveArrows(boolean b) {
        mJianto.setVisibility(b ? VISIBLE : GONE);
    }
}
