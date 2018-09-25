package com.day.loan.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.day.loan.R;
import com.day.loan.entity.ProductEntity;

import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/5/23 下午6:10
 * - @Email whynightcode@gmail.com
 */
public class LoanAdapter extends BaseQuickAdapter<ProductEntity, BaseViewHolder> {
    private final RequestOptions mRequestOptions =
            new RequestOptions()
                    .centerCrop()
                    .transform(new GlideRoundTransform(5))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .dontAnimate();

    public LoanAdapter(@Nullable List<ProductEntity> data) {
        super(R.layout.item_list, data);
        //openLoadAnimation();
        //多次执行动画
        isFirstOnly(false);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductEntity item) {


        String maximumAmount = item.getMaximum_amount();
        if (maximumAmount.length() > 4) {
            String substring = maximumAmount.substring(0, maximumAmount.length() - 4);
            maximumAmount = substring + "万";
        }
        helper.setText(R.id.home_product_title, item.getP_name())
                .setText(R.id.home_product_people, item.getApply())
                .setText(R.id.home_product_content, item.getP_desc())
                .setText(R.id.home_product_rate, item.getMin_algorithm() + "%/日")
                .setText(R.id.home_product_money, item.getMinimum_amount() + "-" + maximumAmount)
                .setText(R.id.home_product_time, item.getFastest_time());

        Glide.with(mContext)
                .load(item.getP_logo())
                .apply(mRequestOptions)
                .into((ImageView) helper.getView(R.id.home_product_logo));
    }
}
