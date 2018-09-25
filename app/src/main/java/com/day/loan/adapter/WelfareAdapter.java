package com.day.loan.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.day.loan.R;
import com.day.loan.entity.WelfareBean;

import java.util.List;


/**
 *
 * @author apple
 * @date 2017/7/18
 */

public class WelfareAdapter extends BaseQuickAdapter<WelfareBean,BaseViewHolder> {

    public WelfareAdapter(List<WelfareBean> data) {
        super(R.layout.welfare_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WelfareBean item) {

        Glide.with(mContext).load(item.getImage())
                .into((ImageView) helper.getView(R.id.welfare));


    }
}
