package tech.doujiang.launcher.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.provider.Telephony;
import android.provider.Telephony.Sms;
import android.content.Intent;
import tech.doujiang.launcher.R;

public class SendSmsActivity extends AppCompatActivity {
    public static final String TAG = "DOUJIANG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        Log.i(TAG, "SendSmsActivity");
        // 1. 查询当前默认的短信应用包名
        // 2. 请求用户将本应用设置成默认短信应用
        // 3. 还原第一步的设置
        String defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(this.getApplicationContext());
        Intent intent = new Intent(this.getApplicationContext(), SendSmsActivity.class);
        intent.putExtra(Sms.Intents.EXTRA_PACKAGE_NAME, this.getApplicationContext().getPackageName());
        startActivity(intent);
        // 还原,尚有问题
//        Intent intent = new Intent(context, Sms.Intents.ACTION_CHANGE_DEFAULT);
//        intent.putExtra(Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsApp);
//        startActivity(intent);
    }
}
