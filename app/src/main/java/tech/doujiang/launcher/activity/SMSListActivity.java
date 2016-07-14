package tech.doujiang.launcher.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import tech.doujiang.launcher.adapter.SMSAdapter;
import tech.doujiang.launcher.model.SMSBean;
import tech.doujiang.launcher.util.BaseIntentUtil;
import tech.doujiang.launcher.util.RexseeSMS;
import tech.doujiang.launcher.R;
import tech.doujiang.launcher.R.id;
import tech.doujiang.launcher.R.layout;

public class SMSListActivity extends AppCompatActivity {

    private ListView smsListView;
    private SMSAdapter smsAdapter;
    private RexseeSMS rsms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_list_view);
        smsListView = (ListView) findViewById(R.id.sms_list);
        smsAdapter = new SMSAdapter(SMSListActivity.this);
        rsms = new RexseeSMS(SMSListActivity.this);
        List<SMSBean> list_mmt = rsms.getThreadsNum(rsms.getThreads(0));
        // ×¢Èë¶ÌÐÅÁÐ±íÊý¾Ý
        smsAdapter.assignment(list_mmt);
        // Ìî³äÊý¾Ý
        smsListView.setAdapter(smsAdapter);
        // ¶ÌÐÅÁÐ±íÏîµã»÷ÊÂ¼þ
        smsListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Map<String, String> map = new HashMap<String, String>();
                SMSBean sb = (SMSBean) smsAdapter.getItem(position);
                map.put("phoneNumber", sb.getAddress());
                map.put("threadId", sb.getThread_id());
                BaseIntentUtil.intentSysDefault(SMSListActivity.this,
                        MessageBoxList.class, map);
            }
        });
    }
}
