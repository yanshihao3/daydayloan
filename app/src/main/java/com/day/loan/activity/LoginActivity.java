package com.day.loan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.day.loan.R;
import com.day.loan.entity.MessageEvent;
import com.day.loan.net.Api;
import com.day.loan.net.ApiService;
import com.day.loan.net.Contacts;
import com.day.loan.net.OnRequestDataListener;
import com.day.loan.utils.CaptchaTimeCount;
import com.day.loan.utils.CodeUtils;
import com.day.loan.utils.SPUtil;
import com.day.loan.utils.StatusBarUtil;
import com.day.loan.utils.ToastUtils;
import com.day.loan.view.PowerfulEditText;
import com.day.loan.view.SuperButton;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author apple
 * 登陆
 */
public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.verify_iv)
    ImageView verifyIv;
    @BindView(R.id.ed_phone)
    PowerfulEditText edPhone;
    @BindView(R.id.result)
    ImageView result;
    @BindView(R.id.ed_code)
    PowerfulEditText edCode;
    @BindView(R.id.bt_code)
    Button btCode;
    @BindView(R.id.layout_code)
    RelativeLayout layoutCode;
    @BindView(R.id.bt_login)
    SuperButton btLogin;
    @BindView(R.id.et_Result)
    EditText etResult;
    @BindView(R.id.layout_Result)
    RelativeLayout layoutResult;

    private String phone;
    private CaptchaTimeCount captchaTimeCount;
    private CodeUtils codeUtils;
    private String yanZhengResult;
    private String etYanZhengCode;
    private String yanZhengCode;
    private int oldNew = 1;
    private KProgressHUD hud;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.theme_color), 40);
        captchaTimeCount = new CaptchaTimeCount(60000, 1000, btCode, this);
        initView();
        setListener();
        initYanzheng();

    }

    private void setListener() {
        edPhone.addTextListener(new PowerfulEditText.TextListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!etResult.getText().toString().isEmpty() && s.toString().length() == 11) {
                    btLogin.setEnabled(true);
                    btLogin.setUseShape();
                } else {
                    btLogin.setEnabled(false);
                    btLogin.setUseShape();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edPhone.getText().toString().length() == 11 && !s.toString().isEmpty()) {
                    btLogin.setEnabled(true);
                    btLogin.setUseShape();
                } else {
                    btLogin.setEnabled(false);
                    btLogin.setUseShape();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edCode.addTextListener(new PowerfulEditText.TextListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (edPhone.getText().toString().length() == 11 && s.toString().length() == 4) {
                    btLogin.setEnabled(true);
                    btLogin.setUseShape();
                } else {
                    btLogin.setEnabled(false);
                    btLogin.setUseShape();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initView() {
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDimAmount(0.5f);

    }

    /**
     * isOldUser
     * 新老用户
     */
    private void isOldUser() {
        hud.show();
        phone = edPhone.getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userphone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("userphone", phone);
        ApiService.GET_SERVICE(Api.LOGIN.isOldUser, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                hud.dismiss();
                try {
                    JSONObject date = data.getJSONObject("data");
                    oldNew = date.getIntValue("isolduser");
                    if (oldNew == 1) {
                        String token = date.getString("token");
                        String userphone = date.getString("userphone");
                        SPUtil.putString(LoginActivity.this, Contacts.TOKEN, token);
                        SPUtil.putString(LoginActivity.this, Contacts.PHONE, userphone);
                        layoutCode.setVisibility(View.GONE);
                        setResult(100);
                        EventBus.getDefault().post(new MessageEvent("login"));
                        finish();
                    } else {
                        layoutResult.setVisibility(View.GONE);
                        layoutCode.setVisibility(View.VISIBLE);
                        getCode();
                        btLogin.setEnabled(false);
                        btLogin.setUseShape();
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


    /**
     * 验证码获取
     */
    private void getCode() {
        captchaTimeCount.start();

        HashMap<String, String> params = new HashMap<>();
        params.put("userphone", phone);
        ApiService.GET_SERVICE(Api.LOGIN.CODE, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                try {
                    JSONObject date = data.getJSONObject("data");
                    String msg = date.getString("msg");
                    String isSucess = date.getString("isSuccess");
                    if ("1".equals(isSucess)) {
                    }
                    ToastUtils.showToast(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requestFailure(int code, String msg) {
                ToastUtils.showToast(msg);
            }
        });

    }

    /**
     * 验证码效验
     */
    private void verCode(String code) {

        hud.show();

        HashMap<String, String> params = new HashMap<>();
        params.put("userphone", phone);
        params.put("code", code);

        ApiService.GET_SERVICE(Api.LOGIN.CHECKCODE, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                hud.dismiss();

                try {
                    JSONObject date = data.getJSONObject("data");
                    String msg = date.getString("msg");
                    String isSucess = date.getString("isSuccess");
                    if ("1".equals(isSucess)) {
                        String token = date.getString("token");
                        String userphone = date.getString("userphone");
                        SPUtil.putString(LoginActivity.this, Contacts.TOKEN, token);
                        SPUtil.putString(LoginActivity.this, Contacts.PHONE, userphone);
                        EventBus.getDefault().post(new MessageEvent("login"));
                        setResult(100);
                        finish();
                    }
                    ToastUtils.showToast(msg);

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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void initYanzheng() {
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        verifyIv.setImageBitmap(bitmap);
        yanZhengCode = codeUtils.getCode();
        yanZhengResult = codeUtils.getResult() + "";
    }

    @OnClick({R.id.back, R.id.verify_iv, R.id.bt_code, R.id.bt_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.verify_iv:
                initYanzheng();
                break;
            case R.id.bt_code:
                getCode();
                break;
            case R.id.bt_login:
                if (oldNew == 1) {
                    etYanZhengCode = etResult.getText().toString().trim();
                    if (TextUtils.isEmpty(etYanZhengCode)) {
                        ToastUtils.showToast("请输入图片里的结果");
                        return;
                    }
                    if (!yanZhengResult.equals(etYanZhengCode)) {
                        ToastUtils.showToast("图片结果输入有误");
                        etResult.getText().clear();
                        initYanzheng();
                    } else {
                        isOldUser();
                    }
                } else {
                    String code = edCode.getText().toString();
                    verCode(code);
                }

                break;
            default:
                break;
        }
    }
}