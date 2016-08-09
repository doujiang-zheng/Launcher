package tech.doujiang.launcher.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import tech.doujiang.launcher.adapter.SMSAdapter;
import tech.doujiang.launcher.database.WorkspaceDBHelper;
import tech.doujiang.launcher.model.MessageBean;
import tech.doujiang.launcher.model.SMSBean;
import tech.doujiang.launcher.util.BaseIntentUtil;
import tech.doujiang.launcher.util.RexseeSMS;
import tech.doujiang.launcher.R;
import tech.doujiang.launcher.R.id;
import tech.doujiang.launcher.R.layout;

public class SMSListActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_SMS = 100;

    private ImageButton wrtieMsg;
    private ListView smsListView;
    public static List<MessageBean> messageList;
    private List<SMSBean> smsList;
    private WorkspaceDBHelper dbHelper;
    private SMSAdapter smsAdapter;
    private RexseeSMS rsms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_list_view);
        wrtieMsg = (ImageButton) findViewById(id.btn_new_msg);
        wrtieMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SMSListActivity.this, SendSmsActivity.class);
                startActivity(intent);
            }
        });
        smsListView = (ListView) findViewById(R.id.sms_list);
        smsAdapter = new SMSAdapter(SMSListActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, PERMISSIONS_REQUEST_READ_SMS);
        }
        dbHelper = WorkspaceDBHelper.getDBHelper(this);
        smsList = dbHelper.getSMS();
//        rsms = new RexseeSMS(SMSListActivity.this);
//        List<SMSBean> list_mmt = rsms.getThreadsNum(rsms.getThreads(0));
//        if (list_mmt == null) {
//            Log.e("msg: ", "null");
//        } else if (list_mmt.isEmpty()) {
//            Log.e("msg:", "empty");
//        }
//        smsAdapter.assignment(list_mmt);
        smsAdapter.assignment(smsList);
        smsListView.setAdapter(smsAdapter);
        smsListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Map<String, String> map = new HashMap<String, String>();
                SMSBean sb = (SMSBean) smsAdapter.getItem(position);
                map.put("phoneNumber", sb.getNumber());
                map.put("threadId", Integer.toString(sb.getThread_id()));
                BaseIntentUtil.intentSysDefault(SMSListActivity.this,
                        MessageBoxList.class, map);
            }
        });
    }
}
