package tech.doujiang.launcher.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import tech.doujiang.launcher.R;
import tech.doujiang.launcher.adapter.ContentAdapter;
import tech.doujiang.launcher.fragment.CallLogListFragment;
import tech.doujiang.launcher.fragment.ContactListFragment;

public class PhoneAppActivity extends FragmentActivity implements View.OnClickListener {

    private LinearLayout phone_contact;
    private LinearLayout phone_call_log;

    private TextView text_contact;
    private TextView text_call_log;

    private Fragment callLogFragment;
    private Fragment contactFragment;

    private ContentAdapter adapter;

    private List<View> views;

    @Override
    public void onClick(View view) {
        restartButton();

        switch (view.getId()) {
            case R.id.phone_call_log:
                text_call_log.setTextColor(0xff1B940A);
                initFragment(0);
                break;
            case R.id.phone_contact:
                text_contact.setTextColor(0xff1B940A);
                initFragment(1);
                break;
            default:
                break;
        }
    }

    private void restartButton() {
        text_call_log.setTextColor(Color.GRAY);
        text_contact.setTextColor(Color.GRAY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_app);

        initView();
        initEvent();
        initFragment(1);
    }

    private void initFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        hideFragment(transaction);
        switch (index) {
            case 0:
                if (callLogFragment == null) {
                    callLogFragment = new CallLogListFragment();
                    transaction.add(R.id.phone_content, callLogFragment);
                } else {
                    transaction.show(callLogFragment);
                }
                break;
            case 1:
                if (contactFragment == null) {
                    contactFragment = new ContactListFragment();
                    transaction.add(R.id.phone_content, contactFragment);
                } else {
                    transaction.show(contactFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (callLogFragment != null) {
            transaction.hide(callLogFragment);
        }
        if (contactFragment != null) {
            transaction.hide(contactFragment);
        }
    }

    private void initEvent() {
        phone_call_log.setOnClickListener(this);
        phone_contact.setOnClickListener(this);
    }

    private void initView() {
        this.phone_call_log = (LinearLayout) findViewById(R.id.phone_call_log);
        this.phone_contact = (LinearLayout) findViewById(R.id.phone_contact);

        this.text_call_log = (TextView) findViewById(R.id.text_call_log);
        this.text_contact = (TextView) findViewById(R.id.text_contact);
    }

}
