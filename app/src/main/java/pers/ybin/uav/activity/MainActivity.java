package pers.ybin.uav.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import pers.ybin.uav.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* 上一次按返回按键的时间 */
    private long preBackPressTime;
    /* 按返回按键的次数 */
    private long pressTimes;

    @Override
    public void onBackPressed() {
        // 调用父方法会失效
        // super.onBackPressed();
        long cBackPressTime = SystemClock.uptimeMillis();
        if (cBackPressTime - preBackPressTime < 2000) {
            pressTimes++;
            if (pressTimes >= 2) {
                this.finish();
                System.exit(0);
            }
        } else {
            pressTimes = 1;
        }
        if (pressTimes == 1) {
            Toast.makeText(this, "再次点击，确认退出", Toast.LENGTH_SHORT).show();
        }
        preBackPressTime = cBackPressTime;
    }

}
