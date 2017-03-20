package pers.ybin.uav.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import pers.ybin.uav.R;
import pers.ybin.uav.adapter.GuideViewPagerAdapter;
import pers.ybin.uav.util.SPUtils;
import pers.ybin.uav.util.constant.AppConstants;

/**
 * 引导页
 *
 * @author ybin
 * @since 2017-03-16
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager viewPager;

    private GuideViewPagerAdapter pagerAdapter;

    private List<View> views;

    private Button startButton;

    // 引导页图片资源
    private static final int[] pics = {R.layout.guide_view_01, R.layout.guide_view_02, R.layout.guide_view_03};

    // 底部指示点
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 一定要放在加载布局之前
        Fresco.initialize(this);
        setContentView(R.layout.activity_guide);
        // 设置状态栏全透明
        StatusBarUtil.setTransparent(this);

        initViews();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        views = new ArrayList<>();
        // 初始化引导页视图列表
        for (int pic : pics) {
            View view = LayoutInflater.from(this).inflate(pic, null);
            views.add(view);
        }
        // 初始化 adapter
        viewPager = (ViewPager) findViewById(R.id.view_pager_guide);
        // 开始按钮
        startButton = (Button) views.get(views.size() - 1).findViewById(R.id.start_button);
        startButton.setTag("enter");
        startButton.setOnClickListener(this);
        // 指示器
        initDots();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        pagerAdapter = new GuideViewPagerAdapter(views);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new PageChangeListener());

        showGIF();
    }

    /**
     * 加载GIF
     */
    private void showGIF() {
        // 1. 无人机
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) views.get(0).findViewById(R.id.guide_01);
        // 本地GIF
        Uri uri;
        // 方法1：
//        uri = new Uri.Builder()
//                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
//                .path(String.valueOf(R.drawable.guide_01))
//                .build();
        // 方法2：Uri uri = Uri.parse("res://包名(实际可以是任何字符串甚至留空)/" + R.drawable.ic_launcher);
        uri = Uri.parse("res://*/" + R.drawable.guide_01);
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)//自动播放动画
                .setUri(uri)//路径
                .build();
        simpleDraweeView.setController(draweeController);

        // 2. 无人机
        simpleDraweeView = (SimpleDraweeView) views.get(2).findViewById(R.id.guide_01);
        draweeController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(uri)
                .build();
        simpleDraweeView.setController(draweeController);

        // 3. 波浪动效
//        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.guide_wave_bg);
//        uri = Uri.parse("res://*/" + R.drawable.wave_bg);
//        draweeController = Fresco.newDraweeControllerBuilder()
//                .setAutoPlayAnimations(true)
//                .setUri(uri)
//                .build();
//        simpleDraweeView.setController(draweeController);
    }

    @Override
    public void onClick(View view) {
        if (view.getTag().equals("enter")) {
            enterMainActivity();
            return;
        }
        int position = (Integer) view.getTag();
        setCurrentView(position);
        setCurrentDot(position);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入引导页
        SPUtils spUtils = new SPUtils(AppConstants.FIRST_OPEN);
        // 设为非首次打开APP
        spUtils.put(AppConstants.FIRST_OPEN, false);

        finish();
    }

    /**
     * 初始化指示器
     */
    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.guide_dot_ll);
        ll.setMinimumHeight(50);
        dots = new ImageView[pics.length];

        // 循环取得小点图片
        ImageView imageView;
        int padding_left, padding_top, padding_right, padding_bottom;
        padding_left = padding_right = 15;
        padding_top = padding_bottom = 0;
        for (int i = 0, length = pics.length; i < length; i++) {
            imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.dot_selector);
            // 设置为非选中状态
            imageView.setEnabled(false);
            imageView.setOnClickListener(this);
            // 设置位置tag，方便取出与当前位置对应
            imageView.setTag(i);
            // 添加边距
            imageView.setPadding(padding_left, padding_top, padding_right, padding_bottom);
            // 添加子控件
            ll.addView(imageView);
            dots[i] = imageView;
        }

        currentIndex = 0;
        // 设置为选中状态
        dots[currentIndex].setEnabled(true);
    }

    /**
     * 设置当前view
     *
     * @param position 位置
     */
    private void setCurrentView(int position) {
        if (position < 0 || position >= pagerAdapter.getCount()) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    /**
     * 设置当前指示点
     *
     * @param position 位置
     */
    private void setCurrentDot(int position) {
        if (position < 0 || position > pagerAdapter.getCount() || currentIndex == position) {
            return;
        }
        // 当前页的指示点为选中状态
        dots[position].setEnabled(true);

        // 上一页的指示点为非选中状态
        dots[currentIndex].setEnabled(false);
        currentIndex = position;
        // 判断是否为最后一页，显示或隐藏按钮
//        if (position == views.size() - 1) {
//            startButton.setVisibility(Button.VISIBLE);
//        } else {
//            startButton.setVisibility(Button.GONE);
//        }
    }

    /**
     * 进入主程序
     */
    private void enterMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        SPUtils spUtils = new SPUtils(AppConstants.FIRST_OPEN);
        // 设为非首次打开APP
        spUtils.put(AppConstants.FIRST_OPEN, false);
        finish();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当前页面被滑动时调用
         *
         * @param position             当前页面，及你点击滑动的页面
         * @param positionOffset       当前页面偏移的百分比
         * @param positionOffsetPixels 当前页面偏移的像素位置
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 当新的页面被选中时调用
         *
         * @param position 当前位置
         */
        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
            setCurrentDot(position);
        }

        /**
         * 当滑动状态改变时调用
         *
         * @param state {@code == 1}:表示正在滑动, {@code == 2}:表示滑动完毕, {@code == 0}:表示什么都没做
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

}
