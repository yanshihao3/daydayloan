package com.day.loan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.day.loan.R;
import com.day.loan.activity.HtmlActivity;
import com.day.loan.activity.LoginActivity;
import com.day.loan.adapter.ComplexAdapter;
import com.day.loan.adapter.LoanAdapter;
import com.day.loan.adapter.TagAdapter;
import com.day.loan.entity.ProductEntity;
import com.day.loan.entity.TagInfo;
import com.day.loan.net.Api;
import com.day.loan.net.ApiService;
import com.day.loan.net.Contacts;
import com.day.loan.net.OnRequestDataListener;
import com.day.loan.net.Params;
import com.day.loan.utils.SPUtil;
import com.day.loan.utils.ToastUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author yanshihao
 */
public class LoanFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {


    Unbinder unbinder;
    @BindView(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;
    RecyclerView mFragmentLoanRv;
    SmartRefreshLayout mLoanRefresh;

    private List<ProductEntity> mList;

    private LoanAdapter mAdapter;


    private String[] title = {"综合排序", "额度", "筛选"};

    private String[] mComplexTitle = {"不限", "上班族", "逍遥客", "企业主"};

    private String[] mMoneyTitle = {"不限", "0-1000", "1000-2000", "2000-5000", "5000-8000", "大于1万"};

    private String[] mTagTitle = {"身份证", "实名手机", "芝麻信用", "信用卡"};


    private List<View> pupView;

    private ListView mComplexListView;
    private ListView mMoneyListView;
    private RecyclerView mTagListView;
    private TextView mButton;


    private String mMoney;

    private String mIndentity;

    private List<String> mTagList;

    private int mType = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData(0, 5, 0, null, null);
    }

    private void initData(int offset, int limit, final int type, String identity, String amount) {

        HashMap<String, String> params = new HashMap<>();
        params.put("name", Params.getAppName());
        params.put("offset", offset + "");
        params.put("number", limit + "");
        if (identity != null) {
            params.put("identity", identity);
        }
        if (amount != null) {
            params.put("amount", amount);
        }
        ApiService.GET_SERVICE(Api.PRODUCT_SCREEN, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject json) {
                JSONArray jsonArray = json.getJSONArray("data");
                List<ProductEntity> entities = JSON.parseArray(JSON.toJSONString(jsonArray), ProductEntity.class);
                if (type == 0) {
                    mAdapter.setNewData(entities);
                } else if (type == 1) {
                    if (entities.size() > 0) {
                        mAdapter.addData(entities);
                    }
                } else { //TODO 筛选tag 选择
                    ArrayList<ProductEntity> mList = new ArrayList<>();
                    for (ProductEntity entity : entities) {
                        List<ProductEntity.LabelsBean> labels = entity.getLabels();
                        if (labels != null) {
                            writeName:
                            for (ProductEntity.LabelsBean bean : labels) {
                                for (int i = 0; i < mTagList.size(); i++) {
                                    String s = mTagList.get(i);
                                    if (bean.getName().equals(s)) {
                                        mList.add(entity);
                                        break writeName;
                                    }
                                }
                            }
                        }

                    }
                    Log.e("tag", "requestSuccess: " + mList.toString());
                    mAdapter.setNewData(mList);
                }
                if (mLoanRefresh.isEnableRefresh()) {
                    mLoanRefresh.finishRefresh();
                }
                if (mLoanRefresh.isEnableLoadMore()) {
                    mLoanRefresh.finishLoadMore();

                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                mLoanRefresh.finishLoadMore();
                mLoanRefresh.finishRefresh();
                ToastUtils.showToast(msg);
            }
        });
    }

    private void initView() {
        initDropMenu();
        mAdapter = new LoanAdapter(null);
        mLoanRefresh.setOnRefreshListener(this);
        mLoanRefresh.setOnLoadMoreListener(this);
        mFragmentLoanRv.setLayoutManager(new LinearLayoutManager(getContext()));

        mFragmentLoanRv.addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(getContext())
                .colorResId(R.color.divider_background)
                .size(20)
                .build()
        );
        mAdapter.bindToRecyclerView(mFragmentLoanRv);
        mAdapter.setEmptyView(R.layout.empty);
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

    private void initDropMenu() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_contentview, null);
        mFragmentLoanRv = ButterKnife.findById(contentView, R.id.fragment_loan_rv);
        mLoanRefresh = ButterKnife.findById(contentView, R.id.loan_refresh);
        pupView = new ArrayList<>();
        //init city menu
        mComplexListView = new ListView(getContext());
        mMoneyListView = new ListView(getContext());
        View tagView = LayoutInflater.from(getContext()).inflate(R.layout.layout_meun_tag, null);
        mTagListView = ButterKnife.findById(tagView, R.id.layout_rv);
        mButton = ButterKnife.findById(tagView, R.id.layout_tab_sumit);
        initComplex();
        initMoney();
        initTag();
        pupView.add(mComplexListView);
        pupView.add(mMoneyListView);
        pupView.add(tagView);
        mDropDownMenu.setDropDownMenu(Arrays.asList(title), pupView, contentView);
    }

    //初始化 标签
    private void initTag() {
        mTagList = new ArrayList<>();
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        mTagListView.setLayoutManager(layoutManager);
        final ArrayList<TagInfo> tagInfos = new ArrayList<>();
        for (int i = 0; i < mTagTitle.length; i++) {
            tagInfos.add(new TagInfo(mTagTitle[i]));
        }
        final TagAdapter tagAdapter = new TagAdapter(tagInfos, getContext());
        mTagListView.setAdapter(tagAdapter);
        tagAdapter.setItmeClickListener(new TagAdapter.ItmeClickListener() {
            @Override
            public void onClick(int position) {
                TagInfo tagInfo = tagInfos.get(position);
                if (tagInfo.isClicked()) {
                    tagInfo.setClicked(false);
                } else {
                    tagInfo.setClicked(true);
                }
                tagAdapter.notifyItemChanged(position);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropDownMenu.closeMenu();
                mTagList.clear();
                //TODO 统计tag 并请求网络加载数据
                for (TagInfo info : tagInfos) {
                    if (info.isClicked()) {
                        //TODO 获取tag
                        mTagList.add(info.getTitle());
                        info.setClicked(false);
                    }
                }
                tagAdapter.notifyDataSetChanged();
                mType = 1;
                initData(0, 10, 2, null, null);
            }
        });
    }

    //初始化 money
    private void initMoney() {
        final ComplexAdapter moneyAdapter = new ComplexAdapter(getContext(), mMoneyTitle);
        mMoneyListView.setDividerHeight(0);
        mMoneyListView.setAdapter(moneyAdapter);
        mMoneyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDropDownMenu.setTabText(position == 0 ? title[1] : mMoneyTitle[position]);
                moneyAdapter.setCheckItemPosition(position);
                mDropDownMenu.closeMenu();
                //TODO 请求网络获取数据
                if (position != 0) {
                    switch (position) {
                        case 1:
                            mMoney = "1000";
                            break;
                        case 2:
                            mMoney = "2000";
                            break;
                        case 3:
                            mMoney = "5000";
                            break;
                        case 4:
                            mMoney = "8000";
                            break;
                        case 5:
                            mMoney = "10000";
                            break;
                        default:
                            break;
                    }
                    Log.e("money", "onItemClick: " + mMoney);
                    mType = 1;
                    initData(0, 10, 0, null, mMoney);
                }
            }
        });
    }


    //初始化综合筛选
    private void initComplex() {
        mComplexListView.setDividerHeight(0);
        final ComplexAdapter complexAdapter = new ComplexAdapter(getContext(), mComplexTitle);
        mComplexListView.setAdapter(complexAdapter);
        mComplexListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDropDownMenu.setTabText(position == 0 ? title[0] : mComplexTitle[position]);
                complexAdapter.setCheckItemPosition(position);
                mDropDownMenu.closeMenu();
                //TODO 请求网络获取数据
                if (position != 0) {
                    switch (position) {
                        case 1:
                            mIndentity = "1";
                            break;
                        case 2:
                            mIndentity = "3";
                            break;
                        case 3:
                            mIndentity = "4";
                            break;
                        default:
                            break;
                    }
                    mType = 1;
                    Log.e("indentity", "onItemClick: " + mIndentity);
                    initData(0, 10, 0, mIndentity, null);
                }
            }
        });
    }

    private ProductEntity mProductEntity;

    private void start(ProductEntity product) {
        mProductEntity = product;
        String token = SPUtil.getString(getActivity(), Contacts.TOKEN);
        ApiService.apply(product.getId(), token);

        if (TextUtils.isEmpty(token)) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, 2);
        } else {
            Intent intent = new Intent(getContext(), HtmlActivity.class);
            intent.putExtra("html", product.getUrl());
            intent.putExtra("title", product.getP_name());
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100&&requestCode==2) {
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
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData(0, 8, 0, null, null);
        mType = 0;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (mType == 0) {
            initData(mAdapter.getData().size(), 10, 1, null, null);
        } else {
            mLoanRefresh.finishLoadMore();
            mLoanRefresh.finishRefresh();
        }
    }
}