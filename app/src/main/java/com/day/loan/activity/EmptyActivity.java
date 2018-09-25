package com.day.loan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.day.loan.R;
import com.day.loan.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author apple
 */
public class EmptyActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nodate)
    ImageView nodate;
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.theme_color), 90);

        toolbarBack.setVisibility(View.VISIBLE);
        String title = getIntent().getStringExtra("title");
        toolbarTitle.setText(title);
    }

    @OnClick(R.id.toolbar_back)
    public void onViewClicked() {
        finish();
    }
}
