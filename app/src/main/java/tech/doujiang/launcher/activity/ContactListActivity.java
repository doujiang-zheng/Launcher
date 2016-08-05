package tech.doujiang.launcher.activity;

import android.Manifest;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.*;
import android.R.integer;
import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.*;
import android.widget.ListView;

import tech.doujiang.launcher.adapter.ContactListAdapter;
import tech.doujiang.launcher.model.ContactBean;
import tech.doujiang.launcher.view.QuickAlphabeticBar;
import tech.doujiang.launcher.R;
import tech.doujiang.launcher.R.id;
import tech.doujiang.launcher.R.layout;

public class ContactListActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private ContactListAdapter adapter;
    private ListView contactList;
    private List<ContactBean> list;
    private AsyncQueryHandler asyncQueryHandler;
    private QuickAlphabeticBar alphabeticBar;

    private Map<Integer, ContactBean> contactIdMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.contact_list_view);
        contactList = (ListView) findViewById(id.contact_list);
        alphabeticBar = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        init();
    }

    public void init() {

    }

    private void setAdapter(List<ContactBean> list) {
        adapter = new ContactListAdapter(this, list, alphabeticBar);
        contactList.setAdapter(adapter);
        alphabeticBar.init(ContactListActivity.this);
        alphabeticBar.setListView(contactList);
        alphabeticBar.setHeight(alphabeticBar.getHeight());
        alphabeticBar.setVisibility(View.VISIBLE);
    }
}
