package com.day.loan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.day.loan.R;
import com.day.loan.utils.StatusBarUtil;
import com.day.loan.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author apple
 *         问题反馈
 */
public class FeedbackActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.et_message)
    EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.theme_color), 90);

        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        toolbarBack.setVisibility(View.VISIBLE);
        toolbarTitle.setText("问题反馈");
    }


    @OnClick({R.id.toolbar_back, R.id.apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.apply:
                    if(!TextUtils.isEmpty(etMessage.getText().toString())){
                        ToastUtils.showToast("感觉您的宝贵意见，我们将稍后作答");
                        finish();
                    }
                break;
            default:
                break;
        }
    }
}
