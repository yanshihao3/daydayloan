package com.day.loan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.day.loan.R;
import com.day.loan.activity.HtmlActivity;
import com.day.loan.adapter.WelfareAdapter;
import com.day.loan.entity.WelfareBean;
import com.day.loan.net.Api;
import com.day.loan.net.ApiService;
import com.day.loan.net.OnRequestDataListener;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author apple
 *         福利
 */
public class WelfareFragment extends Fragment {

    @BindView(R.id.recylerview)
    RecyclerView mRecylerview;
    @BindView(R.id.Swip)
    SwipeRefreshLayout mSwip;
    private WelfareAdapter mWelfareAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welfare, container, false);
        ButterKnife.bind(this, view);
        initView();
        getData();
        setListener();
        return view;
    }

    private void setListener() {

        mWelfareAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WelfareBean product = mWelfareAdapter.getData().get(position);
                startActivity(new Intent(getContext(), HtmlActivity.class)
                        .putExtra("html", product.getLink())
                        .putExtra("title",product.getName()));
            }
        });
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void initView() {
        mWelfareAdapter = new WelfareAdapter(null);
        mRecylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecylerview.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(getContext())
                .colorResId(R.color.divider_background)
                .size(10)
                .build()
        );
        mRecylerview.setAdapter(mWelfareAdapter);
    }

    private void getData() {

        ApiService.GET_SERVICE(Api.WELFARE, new HashMap<String, String>(), new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                if(mSwip.isRefreshing()){
                    mSwip.setRefreshing(false);
                }
                try {
                    String data = json.getString("data");
                    Gson gson = new Gson();
                    WelfareBean[] welfare = gson.fromJson(data, WelfareBean[].class);
                    if (welfare.length > 0) {
                        List<WelfareBean> welfare1 = Arrays.asList(welfare);
                        mWelfareAdapter.setNewData(welfare1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                if(mSwip.isRefreshing()){
                    mSwip.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
