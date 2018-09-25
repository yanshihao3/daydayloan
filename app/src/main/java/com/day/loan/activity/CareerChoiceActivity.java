package com.day.loan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.day.loan.R;
import com.day.loan.flowlayout.FlowLayout;
import com.day.loan.flowlayout.TagAdapter;
import com.day.loan.flowlayout.TagFlowLayout;
import com.day.loan.net.Api;
import com.day.loan.net.ApiService;
import com.day.loan.net.Contacts;
import com.day.loan.net.OnRequestDataListener;
import com.day.loan.utils.ToastUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author apple
 *         登陆完善信息
 */
public class CareerChoiceActivity extends AppCompatActivity {

    @BindView(R.id.id_flowlayout)
    TagFlowLayout idFlowlayout;
    @BindView(R.id.layoutCheck)
    LinearLayout layoutCheck;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.house_flowlayout)
    TagFlowLayout houseFlowlayout;
    @BindView(R.id.car_flowlayout)
    TagFlowLayout carFlowlayout;
    @BindView(R.id.occupation_flowlayout)
    TagFlowLayout occupationFlowlayout;
    @BindView(R.id.checkbox)
    AppCompatCheckBox checkbox;
    private ArrayList<String> list;
    private ArrayList<String> houseList;
    private ArrayList<String> carList;
    private ArrayList<String> workList;
    private KProgressHUD hud ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_choice);
        ButterKnife.bind(this);
        initDate();
        initView();
        setListener();
    }
    private void initView() {
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

        int isInsure = getIntent().getIntExtra("is_insure", 0);
        if (isInsure == 1) {
            layoutCheck.setVisibility(View.VISIBLE);
        }
        idFlowlayout.setAdapter(new TagAdapter<String>(list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                final LayoutInflater mInflater = LayoutInflater.from(CareerChoiceActivity.this);
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        idFlowlayout, false);
                tv.setText(s);
                return tv;
            }
        });
        houseFlowlayout.setAdapter(new TagAdapter<String>(houseList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                final LayoutInflater mInflater = LayoutInflater.from(CareerChoiceActivity.this);
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        idFlowlayout, false);
                tv.setText(s);
                return tv;
            }
        });
        carFlowlayout.setAdapter(new TagAdapter<String>(carList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                final LayoutInflater mInflater = LayoutInflater.from(CareerChoiceActivity.this);
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        idFlowlayout, false);
                tv.setText(s);
                return tv;
            }
        });
        occupationFlowlayout.setAdapter(new TagAdapter<String>(workList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                final LayoutInflater mInflater = LayoutInflater.from(CareerChoiceActivity.this);
                TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                        idFlowlayout, false);
                tv.setText(s);
                return tv;
            }
        });
    }

    private void initDate() {
        list = new ArrayList<>();
        list.add("有卡");
        list.add("无卡");
        houseList = new ArrayList<>();
        houseList.add("有房贷");
        houseList.add("有房无贷");
        houseList.add("无贷");
        carList = new ArrayList<>();
        carList.add("有车贷");
        carList.add("有车无贷");
        carList.add("无车");
        workList = new ArrayList<>();
        workList.add("上班族");
        workList.add("企业主");
        workList.add("自由职业");
        workList.add("其他");
    }

    private void setListener() {
        idFlowlayout.getAdapter().setSelectedList(0);
        houseFlowlayout.getAdapter().setSelectedList(0);
        carFlowlayout.getAdapter().setSelectedList(0);
        occupationFlowlayout.getAdapter().setSelectedList(0);
        final Map<String,String> map=new HashMap<>();

        idFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                return false;
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<Integer> isSelected = idFlowlayout.getSelectedList();
                Set<Integer> houseSelected = houseFlowlayout.getSelectedList();
                Set<Integer> carList = carFlowlayout.getSelectedList();
                Set<Integer> occselectedList = occupationFlowlayout.getSelectedList();
                    for (Integer s : isSelected) {
                        map.put("is_credit", String.valueOf(s + 1));
                    }
                    for (Integer s : houseSelected) {
                        map.put("has_house", String.valueOf(s + 1));
                    }
                    for (Integer s : carList) {
                        map.put("has_car", String.valueOf(s + 1));
                    }
                    for (Integer s : occselectedList) {
                        if (s == 0) {
                            map.put("professional", String.valueOf(s + 1));
                        } else {
                            map.put("professional", String.valueOf(s + 2));
                        }
                    }
                    String userPhone = getIntent().getStringExtra(Contacts.PHONE);
                    map.put("userphone",userPhone);

                hud.show();
                ApiService.GET_SERVICE(Api.LOGIN.IDENTITY, map, new OnRequestDataListener() {
                    @Override
                    public void requestSuccess(int code, JSONObject data) {
                        hud.dismiss();

                        try {
                            JSONObject date = data.getJSONObject("data");
                            String msg = date.getString("msg");
                            String isSucess = date.getString("isSuccess");
                            finish();

                            if ("1".equals(isSucess)) {
                                finish();
                            } else {
                                ToastUtils.showToast( msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void requestFailure(int code, String msg) {
                        hud.dismiss();
                        ToastUtils.showToast(msg);
                    }
                });
            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}