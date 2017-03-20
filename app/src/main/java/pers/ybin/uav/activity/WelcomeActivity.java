package pers.ybin.uav.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jaeger.library.StatusBarUtil;

import pers.ybin.uav.R;
import pers.ybin.uav.util.SPUtils;
import pers.ybin.uav.util.constant.AppConstants;

/**
 * 欢迎页
 *
 * @author ybin
 * @since 2017-03-17
 */
public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. 是不是第一次开启APP
        SPUtils spUtils = new SPUtils(AppConstants.FIRST_OPEN);
        boolean is_first_open = spUtils.getBoolean(AppConstants.FIRST_OPEN, true);

        // 2.1 第一次启动APP：则先进入功能引导页
        if (is_first_open) {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 2.1 不是第一次启动APP：则正常显示启动屏
        setContentView(R.layout.activity_welcome);
        // 设置状态栏全透明
        StatusBarUtil.setTransparent(this);

        // 延迟3秒，进入主界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

}
