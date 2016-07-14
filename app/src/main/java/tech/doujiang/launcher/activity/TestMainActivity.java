package tech.doujiang.launcher.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import tech.doujiang.launcher.R;
import tech.doujiang.launcher.R.id;
import tech.doujiang.launcher.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class TestMainActivity extends AppCompatActivity implements OnClickListener{

    private Button btnLoadContacts;
    private Button btnGetRecentContacts;
    private Button btnGetSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btnLoadContacts = (Button) findViewById(R.id.btn_load_contacts);
        btnGetRecentContacts = (Button) findViewById(R.id.btn_get_recent_contact);
        btnGetSMS = (Button) findViewById(R.id.btn_get_sms);

        btnLoadContacts.setOnClickListener(this);
        btnGetRecentContacts.setOnClickListener(this);
        btnGetSMS.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_load_contacts:
                intent = new Intent(this, ContactListActivity.class);
                break;
            case R.id.btn_get_recent_contact:
                intent = new Intent(this, ContactRecordListActivity.class);
                break;
            case R.id.btn_get_sms:
                intent = new Intent(this, SMSListActivity.class);
                break;
        }
        startActivity(intent);
    }

}
