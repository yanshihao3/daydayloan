package com.day.loan.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.day.loan.R;
import com.day.loan.activity.EmptyActivity;
import com.day.loan.activity.FeedbackActivity;
import com.day.loan.activity.HtmlActivity;
import com.day.loan.activity.LoginActivity;
import com.day.loan.activity.SettingActivity;
import com.day.loan.entity.MessageEvent;
import com.day.loan.net.Contacts;
import com.day.loan.utils.DimenUtil;
import com.day.loan.utils.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author yanshihao
 */
public class MyFragment extends Fragment {


    @BindView(R.id.my_linear_layout)
    LinearLayout mMyLinearLayout;
    @BindView(R.id.my_linear)
    LinearLayout mMyLinear;
    @BindView(R.id.my_phone)
    TextView mTextView;
    Unbinder unbinder;

    private String TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setViewHeight(mMyLinear);
        setViewHeight(mMyLinearLayout);
        initView(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initView(MessageEvent event) {
        if (isLogin()) {
            mTextView.setText(SPUtil.getString(getContext(), Contacts.PHONE));
        } else {
            mTextView.setText("登录");
        }
    }

    private void setViewHeight(LinearLayout layout) {
        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        layoutParams.height = DimenUtil.getScreenWidth() / 3;
        layout.setLayoutParams(layoutParams);
    }

    private boolean isLogin() {
        String token = SPUtil.getString(getActivity(), Contacts.TOKEN);
        return TextUtils.isEmpty(token) ? false : true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.my_edit, R.id.my_progess, R.id.my_rate, R.id.my_setting, R.id.my_problem, R.id.my_safe, R.id.my_phone})
    public void onViewClicked(View view) {
        if (!isLogin()) {
            //todo 登录
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, 3);
        } else {
            switch (view.getId()) {
                case R.id.my_edit:
                    edit();
                    break;
                case R.id.my_progess:
                    //todo 空的
                    startActivity(new Intent(getContext(), EmptyActivity.class).putExtra("title", "贷款进度"));

                    break;
                case R.id.my_rate:
                    //todo 空的
                    startActivity(new Intent(getContext(), EmptyActivity.class).putExtra("title", "我的免息卷"));
                    break;
                case R.id.my_setting:
                    //setting
                    startActivity(new Intent(getContext(), SettingActivity.class));

                    break;
                case R.id.my_problem:
                    startActivity(new Intent(getContext(), FeedbackActivity.class));
                    break;
                case R.id.my_safe:
                    //Html
                    Intent intent2 = new Intent(getActivity(), HtmlActivity.class);
                    intent2.putExtra("title", "安全小贴士");
                    intent2.putExtra("html", "http://m.anwenqianbao.com/#/minTips");
                    startActivity(intent2);
                    break;
                case R.id.my_phone:
                    break;
                default:
                    break;
            }

        }
    }

    private void edit() {
        final EditText editText = new EditText(getContext());
        editText.setHint("请输入姓名");
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("修改姓名")
                .setView(editText)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTextView.setText(editText.getText().toString());
                    }
                }).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == 100) {
            mTextView.setText(SPUtil.getString(getContext(), Contacts.PHONE));
        }
    }
}
