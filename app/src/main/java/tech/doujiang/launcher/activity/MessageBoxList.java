package tech.doujiang.launcher.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import tech.doujiang.launcher.adapter.MessageBoxListAdapter;
import tech.doujiang.launcher.model.MessageBean;
import tech.doujiang.launcher.R;
import tech.doujiang.launcher.R.layout;

public class MessageBoxList extends AppCompatActivity {

    private ListView talkView;
    private List<MessageBean> messages = null;
    private AsyncQueryHandler asyncQuery;
    private String address;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_list_view);
        sdf = new SimpleDateFormat("MM-dd HH:mm");
        String thread = getIntent().getStringExtra("threadId");
        init(thread);
    }

    private void init(String thread) {
        talkView = (ListView) findViewById(R.id.message_list);
        messages = new ArrayList<MessageBean>();
    }
}
