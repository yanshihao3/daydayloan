package com.day.loan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.day.loan.R;
import com.day.loan.adapter.LoanAdapter;
import com.day.loan.entity.ProductEntity;
import com.day.loan.net.Api;
import com.day.loan.net.ApiService;
import com.day.loan.net.Contacts;
import com.day.loan.net.OnRequestDataListener;
import com.day.loan.utils.SPUtil;
import com.day.loan.utils.StatusBarUtil;
import com.day.loan.utils.ToastUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yanshihao
 */
public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.recylerview)
    RecyclerView mRecylerview;

    private ProductEntity mProductEntity;

    private String title;
    private LoanAdapter mAdapter;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.theme_color), 40);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", 0);
        if (mType == 0) {
            mProductEntity = intent.getParcelableExtra("product");
        } else {
            title = intent.getStringExtra("details");
        }
        initView();
    }

    private void initView() {
        mRecylerview.setLayoutManager(new LinearLayoutManager(this));

        mRecylerview.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(this)
                .colorResId(R.color.divider_background)
                .size(20)
                .build()
        );
        mAdapter = new LoanAdapter(null);
        mAdapter.bindToRecyclerView(mRecylerview);
        mAdapter.setEmptyView(R.layout.empty);
        if (mType == 0) {
            mAdapter.addData(mProductEntity);
        } else {
            ininData();
        }
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductEntity product = mAdapter.getData().get(position);
                start(product);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转登录或者html界面
                ProductEntity product = mAdapter.getData().get(position);
                start(product);
            }
        });
    }

    private void ininData() {
        ApiService.GET_SERVICE(Api.PRODUCT_LSIT, null, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                JSONArray jsonArray = json.getJSONArray("data");
                List<ProductEntity> entities = JSON.parseArray(JSON.toJSONString(jsonArray), ProductEntity.class);
                for (ProductEntity entity : entities
                        ) {
                    String p_name = entity.getP_name();
                    if (p_name.contains(title)) {
                        mAdapter.addData(entity);
                    }
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                ToastUtils.showToast(msg);
            }
        });

    }

    private void start(ProductEntity product) {
        String token = SPUtil.getString(this, Contacts.TOKEN);
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("html", product.getUrl());
            intent.putExtra("id", product.getId());
            intent.putExtra("title", product.getP_name());
            startActivity(intent);
        } else {
            ApiService.apply(product.getId(), token);
            Intent intent = new Intent(this, HtmlActivity.class);
            intent.putExtra("html", product.getUrl());
            intent.putExtra("title", product.getP_name());
            startActivity(intent);
        }
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
