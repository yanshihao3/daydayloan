package com.day.loan.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.day.loan.R;
import com.day.loan.utils.CodeUtils;
import com.day.loan.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author apple
 *         运算验证
 */
public class VerificationFragment extends DialogFragment {


    @BindView(R.id.verify_iv)
    ImageView verifyIv;
    @BindView(R.id.verify_et)
    EditText verifyEt;
    @BindView(R.id.verify_btn)
    Button verifyBtn;
    private CodeUtils codeUtils;
    private String yanZhengCode;
    private String yanZhengResult;
    private String etYanZhengCode;
    private VerListener listener;
    public VerificationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Base_AlertDialog);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (VerListener)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verification, container, false);
        final Window window = getDialog().getWindow();
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(wlp);
            getDialog().setCancelable(false);
            getDialog().setCanceledOnTouchOutside(false);
        }
        ButterKnife.bind(this, view);
        initYanzheng();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.verify_iv, R.id.verify_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.verify_iv:
                initYanzheng();
                break;
            case R.id.verify_btn:
                etYanZhengCode = verifyEt.getText().toString().trim();
                if (TextUtils.isEmpty(etYanZhengCode)) {
                    ToastUtils.showToast("请输入图片里的结果");
                    return;
                }
                if (!yanZhengResult.equals(etYanZhengCode)) {
                    ToastUtils.showToast("图片结果输入有误");
                    initYanzheng();
                }else {
                    listener.success();
                    dismiss();
                }
                //对话框隐藏
                break;
            default:
                break;
        }
    }
    private void initYanzheng() {
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();
        verifyIv.setImageBitmap(bitmap);
        yanZhengCode = codeUtils.getCode();
        yanZhengResult = codeUtils.getResult() + "";
    }
}
