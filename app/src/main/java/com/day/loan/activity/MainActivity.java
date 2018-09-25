package com.day.loan.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.day.loan.R;
import com.day.loan.entity.TabEntity;
import com.day.loan.fragment.HomeFragment;
import com.day.loan.fragment.LoanFragment;
import com.day.loan.fragment.MyFragment;
import com.day.loan.fragment.WelfareFragment;
import com.day.loan.net.Api;
import com.day.loan.net.Params;
import com.day.loan.update.AppUpdateUtils;
import com.day.loan.update.OkGoUpdateHttpUtil;
import com.day.loan.update.UpdateAppBean;
import com.day.loan.update.UpdateAppManager;
import com.day.loan.update.UpdateCallback;
import com.day.loan.utils.StatusBarUtil;
import com.day.loan.utils.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yanshihao
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_ctl)
    CommonTabLayout mMainCtl;

    private int newversioncode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.theme_color), 40);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndPermission.with(this)
                    .requestCode(200)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES)
                    .callback(listener)
                    .start();
        } else {
            AndPermission.with(this)
                    .requestCode(200)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .callback(listener)
                    .start();
        }
        initView();
    }

    private void initView() {
        ArrayList<CustomTabEntity> tabEntitie = new ArrayList<>();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new LoanFragment());
        fragments.add(new MyFragment());
        tabEntitie.add(new TabEntity("主页", R.mipmap.iv_home_select, R.mipmap.iv_home));
        tabEntitie.add(new TabEntity("产品大全", R.mipmap.iv_loan_select, R.mipmap.iv_loan));
        tabEntitie.add(new TabEntity("个人", R.mipmap.iv_my_select, R.mipmap.iv_my));
        mMainCtl.setTabData(tabEntitie, this, R.id.main_fl, fragments);

    }

    private long mLastBackTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mLastBackTime) < 1000) {
            finish();
        } else {
            mLastBackTime = System.currentTimeMillis();
            Toast.makeText(this, "请再确认一次", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateDiy() {
        newversioncode = AppUpdateUtils.getVersionCode(this);
        Map<String, String> params = new HashMap<String, String>();
        String string = getString(R.string.appName);
        params.put("version", String.valueOf(newversioncode));
        params.put("name", string);
        new UpdateAppManager
                .Builder()
                .setAppKey(Params.getAppName())
                .setActivity(this)
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new OkGoUpdateHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(Api.STATUS.UPDATE)
                //以下设置，都是可选
                .setPost(true)
                //不显示通知栏进度条
//                .dismissNotificationProgress()
                //是否忽略版本
//                .showIgnoreVersion()
                //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
                .setParams(params)
                //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度
                .hideDialogOnDownloading(false)
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
                //为按钮，进度条设置颜色。
                //.setThemeColor(0xffffac5d)
                //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
//                .setTargetPath(path)
                //设置appKey，默认从AndroidManifest.xml获取，如果，使用自定义参数，则此项无效
//                .setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")
                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {

                        UpdateAppBean updateAppBean = new UpdateAppBean();

                        try {
                            JSONObject object = JSONObject.parseObject(json);

                            JSONObject jsonObject = object.getJSONObject("data");

                            if (!TextUtils.isEmpty(jsonObject.toString())) {
                                int size = jsonObject.getIntValue("size");
                                Double i = (double) size / 1024 / 1024;
                                DecimalFormat df = new DecimalFormat("0.0");
                                String format = df.format(i);
                                int versioncode = jsonObject.getIntValue("versioncode");

                                String update = "No";
                                if (versioncode > newversioncode) {
                                    update = "Yes";
                                }
                                updateAppBean
                                        //（必须）是否更新Yes,No
                                        .setUpdate(update)
                                        //（必须）新版本号，
                                        .setNewVersion(jsonObject.getString("versionname"))
                                        //（必须）下载地址
                                        .setApkFileUrl(jsonObject.getString("url"))
                                        //测试下载路径是重定向路径
//                                      .setApkFileUrl("http://openbox.mobilem.360.cn/index/d/sid/3282847")
                                        //（必须）更新内容
//                                      .setUpdateLog(jsonObject.optString("update_log"))
                                        //测试内容过度
//                                      .setUpdateLog("测试")
                                        .setUpdateLog(jsonObject.getString("updatecontent"))
//                                      .setUpdateLog("今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说相对于其他行业来说今天我们来聊一聊程序员枯燥的编程生活，相对于其他行业来说\r\n")
                                        //大小，不设置不显示大小，可以不设置
                                        .setTargetSize(String.valueOf(format) + "M")
                                        //是否强制更新，可以不设置
                                        .setConstraint(jsonObject.getBooleanValue("isForce"))
                                        //设置md5，可以不设置
                                        .setNewMd5(jsonObject.getString("md5"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        updateAppManager.showDialogFragment();
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
                        //CProgressDialogUtils.showProgressDialog(HomeActivity.this);

                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
                        //CProgressDialogUtils.cancelProgressDialog(HomeActivity.this);

                    }
                });
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 200) {
                updateDiy();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            ToastUtils.showToast("为了您的账号安全,请打开设备权限");
            if (requestCode == 200) {
                if ((AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions))) {
                    AndPermission.defaultSettingDialog(MainActivity.this, 500).show();
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 500:
                // 这个400就是上面defineSettingDialog()的第二个参数。
                // 你可以在这里检查你需要的权限是否被允许，并做相应的操作。
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    updateDiy();
                } else {
                    ToastUtils.showToast("获取权限失败");
                }
                break;
            default:
                break;
        }
    }

    public void setSelect(int position) {
        mMainCtl.setCurrentTab(position);
    }
}
