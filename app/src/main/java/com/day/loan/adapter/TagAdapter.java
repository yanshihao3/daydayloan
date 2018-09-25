package com.day.loan.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.day.loan.R;
import com.day.loan.entity.TagInfo;

import java.util.List;

/**
 * - @Author:  闫世豪
 * - @Time:  2018/8/2 上午10:41
 * - @Email whynightcode@gmail.com
 */
public class TagAdapter extends RecyclerView.Adapter<ViewHolder> {
    public interface ItmeClickListener {
        void onClick(int position);
    }

    private ItmeClickListener mItmeClickListener;

    public void setItmeClickListener(ItmeClickListener onItmeClickListener) {
        mItmeClickListener = onItmeClickListener;
    }

    private List<TagInfo> mTagInfos;
    private Context mContext;

    public TagAdapter(List<TagInfo> tagInfos, Context context) {
        mTagInfos = tagInfos;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tag_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mButton.setText(mTagInfos.get(position).getTitle());
        if (mTagInfos.get(position).isClicked()) {
            holder.mButton.setBackgroundResource(R.drawable.tag_clicked);
            holder.mButton.setTextColor(mContext.getResources().getColor(R.color.white, null));
        } else {
            holder.mButton.setBackgroundResource(R.drawable.tag_unclicked);
            holder.mButton.setTextColor(mContext.getResources().getColor(R.color.black, null));
        }
        ViewGroup.LayoutParams layoutParams = holder.mButton.getLayoutParams();
        if (layoutParams instanceof FlexboxLayoutManager.LayoutParams) {
            FlexboxLayoutManager.LayoutParams lp = (FlexboxLayoutManager.LayoutParams) layoutParams;
            lp.setFlexGrow(1.0f);
            lp.setAlignSelf(AlignSelf.FLEX_END);
        }
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItmeClickListener != null) {
                    mItmeClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTagInfos.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {

    TextView mButton;

    public ViewHolder(View itemView) {
        super(itemView);
        mButton = itemView.findViewById(R.id.item_tag_btn);
    }
}

