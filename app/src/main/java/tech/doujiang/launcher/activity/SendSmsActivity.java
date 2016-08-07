package tech.doujiang.launcher.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.provider.Telephony;
import android.provider.Telephony.Sms;
import android.content.Intent;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import tech.doujiang.launcher.R;
import tech.doujiang.launcher.database.WorkspaceDBHelper;
import tech.doujiang.launcher.model.ContactBean;

public class SendSmsActivity extends AppCompatActivity {
    private static final String TAG = "DOUJIANG";
    private HorizontalScrollView msgContacts;
    private EditText inputMsg;
    private ImageButton goBack, addContact, sendMsg;
    private WorkspaceDBHelper dbHelper;
    private ArrayList<ContactBean> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        initViews();
        initEvents();
        // 1. 查询当前默认的短信应用包名
        // 2. 请求用户将本应用设置成默认短信应用
        // 3. 还原第一步的设置
//        String defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this.getApplicationContext());
//        Intent intent = new Intent(this.getApplicationContext(), SendSmsActivity.class);
//        intent.putExtra(Sms.Intents.EXTRA_PACKAGE_NAME, this.getApplicationContext().getPackageName());
//        startActivity(intent);
        // 还原,尚有问题
//        Intent intent = new Intent(context, Sms.Intents.ACTION_CHANGE_DEFAULT);
//        intent.putExtra(Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsApp);
//        startActivity(intent);
    }

    private void initViews() {
        msgContacts = (HorizontalScrollView) findViewById(R.id.msg_contacts);
        inputMsg = (EditText) findViewById(R.id.input_text);
        goBack = (ImageButton) findViewById(R.id.btn_go_back);
        addContact = (ImageButton) findViewById(R.id.add_contact);
        sendMsg = (ImageButton) findViewById(R.id.send_msg);
        dbHelper = WorkspaceDBHelper.getDBHelper(this);
        contactList = dbHelper.getContact();
    }

    private void initEvents() {

    }

}
