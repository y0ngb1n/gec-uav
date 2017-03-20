package pers.ybin.uav.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import pers.ybin.uav.util.Utils;

/**
 * Activity基类
 *
 * @author ybin
 * @since 2017-03-18
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.init(this);
    }

}
