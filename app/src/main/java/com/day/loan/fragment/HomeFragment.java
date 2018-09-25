package com.day.loan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.day.loan.activity.MainActivity;
import com.day.loan.entity.ItemType;
import com.day.loan.entity.MultiItemBean;
import com.day.loan.utils.SPUtil;
import com.fondesa.recyclerviewdivider.RecyclerViewDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.day.loan.R;
import com.day.loan.activity.HtmlActivity;
import com.day.loan.activity.LoginActivity;
import com.day.loan.adapter.ProductAdapter;
import com.day.loan.entity.ProductEntity;
import com.day.loan.net.Api;
import com.day.loan.net.ApiService;
import com.day.loan.net.Contacts;
import com.day.loan.net.OnRequestDataListener;
import com.day.loan.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author yanshihao
 */
public class HomeFragment extends Fragment {


    @BindView(R.id.fragment_home_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    Unbinder unbinder;

    private ProductAdapter mProductAdapter;

    private List<MultiItemBean> mBeanList;

    private MainActivity mMainActivity;

    private ProductEntity mProductEntity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();

    }


    private void start(ProductEntity product) {
        mProductEntity = product;
        String token = SPUtil.getString(getActivity(), Contacts.TOKEN);
        ApiService.apply(product.getId(), token);
        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, 1);
        } else {
            Intent intent = new Intent(getContext(), HtmlActivity.class);
            intent.putExtra("html", product.getUrl());
            intent.putExtra("title", product.getP_name());
            startActivity(intent);
        }
    }


    private void initView() {
        mBeanList = new ArrayList<>();
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(gridLayoutManager);

        mProductAdapter = new ProductAdapter(null, mRecyclerView);

        RecyclerViewDivider.with(getContext()).build().addTo(mRecyclerView);

        mProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductEntity product = mProductAdapter.getData().get(position).getProductEntity();
                int itemType = mProductAdapter.getData().get(position).getItemType();
                switch (itemType) {
                    case ItemType.HEAD:

                        break;
                    case ItemType.MORE:
                        if (mMainActivity != null) {
                            mMainActivity.setSelect(1);
                        }
                        break;
                    case ItemType.PRODUCT:
                        start(product);
                        break;
                    default:
                        break;
                }
            }
        });

        mProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (mMainActivity != null) {
                    mMainActivity.setSelect(1);
                }
            }
        });

    }

    private void initData() {
        mBeanList.clear();
        ApiService.GET_SERVICE(Api.HOT, null, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                JSONArray jsonArray = json.getJSONArray("data");
                List<ProductEntity> entities = JSON.parseArray(JSON.toJSONString(jsonArray), ProductEntity.class);
                mBeanList.add(0, new MultiItemBean().setItemType(ItemType.HEAD));
                for (ProductEntity p : entities) {
                    mBeanList.add(new MultiItemBean().setItemType(ItemType.PRODUCT).setProductEntity(p));
                }
                mBeanList.add(new MultiItemBean().setItemType(ItemType.MORE));
                mProductAdapter.setNewData(mBeanList);
                mRefreshLayout.finishRefresh();

            }

            @Override
            public void requestFailure(int code, String msg) {
                mRefreshLayout.finishRefresh();
                ToastUtils.showToast(msg);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100 && requestCode == 1) {
            Intent intent = new Intent(getContext(), HtmlActivity.class);
            intent.putExtra("html", mProductEntity.getUrl());
            intent.putExtra("title", mProductEntity.getP_name());
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMainActivity = null;
    }
}