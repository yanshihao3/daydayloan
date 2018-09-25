package com.day.loan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.day.loan.R;
import com.day.loan.entity.ProductList;
import com.day.loan.utils.StatusBarUtil;
import com.day.loan.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yanshihao
 */
public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.recylerview)
    RecyclerView mRecylerview;
    @BindView(R.id.search_tv)
    TextView mSearchTv;
    @BindView(R.id.back)
    ImageView mBack;
    private ProductList mProductList;

    private StringBuilder mStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.theme_color), 40);
        ButterKnife.bind(this);
        mProductList = getIntent().getParcelableExtra("product");
        initView();
    }

    private void initView() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        mRecylerview.setLayoutManager(layoutManager);
        mRecylerview.setAdapter(new MyAdapter());
        ImageView imageView = (ImageView) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = 64;
        layoutParams.width = 64;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(layoutParams);
        lp.setMargins(5, 0, 5, 0);
        lp.gravity=Gravity.CENTER;
        imageView.setLayoutParams(lp);

        EditText textView = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textView.setTextSize(14);
        textView.setGravity(Gravity.CENTER);

        mStringBuilder = new StringBuilder();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("search", "onQueryTextSubmit: " + query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("search", "onQueryTextChange: " + newText);
                mStringBuilder.setLength(0);
                mStringBuilder.append(newText);
                return false;
            }
        });
    }

    @OnClick({R.id.search_tv, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_tv:
                if (mStringBuilder.toString().length() > 0) {
                    Intent intent = new Intent(this, DetailsActivity.class);
                    intent.putExtra("details", mStringBuilder.toString());
                    intent.putExtra("type", 1);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast("请输入内容");
                }
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHole> {

        @NonNull
        @Override
        public MyViewHole onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_search, parent, false);
            return new MyViewHole(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHole holder, final int position) {
            holder.mTextView.setText(mProductList.getList().get(position).getP_name());
            ViewGroup.LayoutParams layoutParams = holder.mTextView.getLayoutParams();
            if (layoutParams instanceof FlexboxLayoutManager.LayoutParams) {
                FlexboxLayoutManager.LayoutParams lp = (FlexboxLayoutManager.LayoutParams) layoutParams;
                lp.setFlexGrow(1.0f);
                lp.setAlignSelf(AlignSelf.FLEX_END);
            }
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
                    intent.putExtra("product", mProductList.getList().get(position));
                    intent.putExtra("type", 0);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mProductList.getList().size();
        }
    }

    class MyViewHole extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public MyViewHole(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv);
        }
    }


}
