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
        asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
        init();
    }

    public void init() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };

        asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc");
    }

    private class MyAsyncQueryHandler extends AsyncQueryHandler {
        private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

            if (cursor != null && cursor.getCount() > 0) {
                contactIdMap = new HashMap<Integer, ContactBean>();
                list = new ArrayList<ContactBean>();
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i ++) {
                    cursor.moveToPosition(i);
                    String name = cursor.getString(1);
                    String number = cursor.getString(2);
                    String sortKey = cursor.getString(3);
                    int contactId = cursor.getInt(4);
                    Long photoId = cursor.getLong(5);
                    String lookUpKey = cursor.getString(6);

                    if (contactIdMap.containsKey(contactId)) {

                    } else {

                        ContactBean contact = new ContactBean();
                        contact.setDisplayName(name);
                        contact.setPhoneNum(number);
                        contact.setSortkey(sortKey);
                        contact.setPhotoId(photoId);
                        contact.setLookUpKey(lookUpKey);
                        list.add(contact);

                        contactIdMap.put(contactId, contact);
                    }
                }
                if (list.size() > 0) {
                    setAdapter(list);
                }
            }
            super.onQueryComplete(token, cookie, cursor);
        }

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
