package tech.doujiang.launcher.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import tech.doujiang.launcher.R;
import tech.doujiang.launcher.R.layout;
import tech.doujiang.launcher.database.WorkspaceDBHelper;
import tech.doujiang.launcher.service.CallSmsFirewallService;

public class LauncherActivity extends Activity  implements OnClickListener {
    private List<ResolveInfo> mApps;
    private List<String> forbiddenPackage;
    private WorkspaceDBHelper dbHelper;
    GridView mGrid;
    Button phone, message, db_interact;

//    private OnItemClickListener listener = new OnItemClickListener() {
//
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position,
//                                long id) {
//            ResolveInfo info = mApps.get(position);
//            //Leave to be realized later.
//            String pkg = info.activityInfo.packageName;
//            if (!forbiddenPackage.contains(pkg)) {
//
//                String cls = info.activityInfo.name;
//
//                ComponentName component = new ComponentName(pkg, cls);
//
//                Intent i = new Intent();
//                i.setComponent(component);
//                startActivity(i);
//            } else {
//                new AlertDialog.Builder(LauncherActivity.this)
//                        .setIcon(R.drawable.touxiang)
//                        .setTitle(R.string.app_name)
//                        .setMessage(R.string.forbidden)
//                        .show();
//                Log.e("ForbiddenList", "This app has been forbidden!");
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadApps();
        setContentView(R.layout.activity_launcher);
        getForbiddenPackage();
//        mGrid = (GridView) findViewById(R.id.apps_list);
//        mGrid.setAdapter(new AppsAdapter());
//        mGrid.setOnItemClickListener(listener);

        phone = (Button) findViewById(R.id.phone_call);
        message = (Button) findViewById(R.id.message_box);
        db_interact = (Button) findViewById(R.id.db_test);

        phone.setOnClickListener(this);
        message.setOnClickListener(this);
        db_interact.setOnClickListener(this);
        Intent intent = new Intent(this, CallSmsFirewallService.class);
        startService(intent);
    }

    @Override
    public void onClick(View view) {
//        Toast.makeText(this, "Warning: 15366106759 Not in the List!", Toast.LENGTH_LONG).show();
        Intent intent = null;
        switch (view.getId()) {
            case R.id.phone_app:
                intent = new Intent(this, PhoneAppActivity.class);
                break;
            case R.id.message_app:
                intent = new Intent(this, SmsAppActivity.class);
            case R.id.db_test: {
                dbHelper = WorkspaceDBHelper.getDBHelper(getApplicationContext());

            }
            default:
                break;
        }
        if (intent != null)
            startActivity(intent);
    }



    private ArrayList<String> getForbiddenPackage() {
        ArrayList<String> forbiddenPackage = new ArrayList<String>();
        // Just a sample.
        forbiddenPackage.add("com.guoshisp.mobilesafe");
        return forbiddenPackage;
    }

    private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
        mApps.clear();
    }

//    public class AppsAdapter extends BaseAdapter {
//        public AppsAdapter() {
//
//        }
//
//        @Override
//        public int getCount() {
//            return mApps.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mApps.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ImageView i;
//
//            if (convertView == null) {
//                i = new ImageView(LauncherActivity.this);
//                i.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                i.setLayoutParams(new GridView.LayoutParams(100,100));
//            } else {
//                i = (ImageView) convertView;
//            }
//
//            ResolveInfo info = mApps.get(position);
//            i.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
//
//            return i;
//        }
//
//    }
}
