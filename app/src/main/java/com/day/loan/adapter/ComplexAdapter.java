package com.day.loan.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.day.loan.R;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/2 上午10:50
 * - @Email whynightcode@gmail.com
 */
public class ComplexAdapter extends BaseAdapter {
    private String[] title;
    private Context mContext;

    private int checkItemPosition = -1;

    public ComplexAdapter(Context context, String[] title) {
        mContext = context;
        this.title = title;
    }

    public void setCheckItemPosition(int checkItemPosition) {
        this.checkItemPosition = checkItemPosition;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return title[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_filter_item, parent, false);
        TextView textView = view.findViewById(R.id.item_filter_title);
        textView.setText(title[position]);

        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                textView.setTextColor(mContext.getResources().getColor(R.color.theme_color, null));
            } else {
                textView.setTextColor(mContext.getResources().getColor(R.color.black, null));
            }
        }

        return view;
    }
}
