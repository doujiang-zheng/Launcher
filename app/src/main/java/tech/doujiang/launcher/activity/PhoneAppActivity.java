package tech.doujiang.launcher.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import tech.doujiang.launcher.R;
import tech.doujiang.launcher.adapter.ContentAdapter;

public class PhoneAppActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private LinearLayout phone_contact;
    private LinearLayout phone_call_log;

    private TextView text_contact;
    private TextView text_call_log;

    private ViewPager viewPager;

    private ContentAdapter adapter;

    private List<View> views;

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_app);

        initEvent();
        initView();
    }

    private void initEvent() {

    }

    private void initView() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
