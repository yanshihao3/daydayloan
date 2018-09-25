package com.day.loan.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.day.loan.R;
import com.day.loan.entity.ItemType;
import com.day.loan.entity.MultiItemBean;
import com.day.loan.utils.DimenUtil;

import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/5/23 下午6:10
 * - @Email whynightcode@gmail.com
 */
public class ProductAdapter extends BaseMultiItemQuickAdapter<MultiItemBean, BaseViewHolder>
        implements BaseQuickAdapter.SpanSizeLookup {
    private final RequestOptions mRequestOptions =
            new RequestOptions()
                    .centerCrop()
                    .transform(new GlideCircleTransform())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();

    public ProductAdapter(@Nullable List<MultiItemBean> data, RecyclerView recyclerView) {
        super(data);
        // openLoadAnimation();
        bindToRecyclerView(recyclerView);
        //多次执行动画
        isFirstOnly(false);
        addItemType(ItemType.HEAD, R.layout.item_header);
        addItemType(ItemType.PRODUCT, R.layout.item_product);
        addItemType(ItemType.MORE, R.layout.item_more);
        setSpanSizeLookup(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemBean item) {
        switch (item.getItemType()) {
            case ItemType.HEAD:
                helper.addOnClickListener(R.id.head_button_people)
                        .addOnClickListener(R.id.head_button);
                break;
            case ItemType.PRODUCT:

                helper.setText(R.id.item_title, item.getProductEntity().getP_name())
                        .setText(R.id.item_person, item.getProductEntity().getApply() + "人申请");
                int layoutPosition = helper.getLayoutPosition();
                if (layoutPosition >= 4) {
                    helper.setVisible(R.id.item_label, false);
                } else {
                    helper.setVisible(R.id.item_label, true);
                }
                Glide.with(mContext)
                        .load(item.getProductEntity().getP_logo())
                        .apply(mRequestOptions)
                        .into((ImageView) helper.getView(R.id.item_logo));
                break;
            default:
                break;
        }
    }

    @Override
    public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
        return position == 0 ? 3 : 1;
    }
}
