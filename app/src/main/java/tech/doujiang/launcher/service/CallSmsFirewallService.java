package tech.doujiang.launcher.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.Activity;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;

import tech.doujiang.launcher.R;
import tech.doujiang.launcher.activity.LauncherActivity;
import tech.doujiang.launcher.database.WorkspaceDBHelper;
import tech.doujiang.launcher.model.ContactBean;


/*
* 分为两种工作情况：
*   1. 在工作区内：
*       （1）对来电、去电进行检查，不在工作区通讯录内即挂断
*       （2）对发送、接收短信进行检查，不在工作区通讯录内的取消短信通知；
*           在通讯录内的，删除系统短信记录，存入工作区数据库
*       （3）工作区内禁用复制、粘贴（剪切板），禁止写入sd卡、禁止USB
*   2. 在非工作区下：
*       （1）对来电、去电进行检查，在工作区通讯录内即挂断
*       （2）对发送、接收短信进行检查，在工作区通讯录内即删除另存到工作区数据库
*      */
public class CallSmsFirewallService extends Service {
    private static final String TAG = "CallSmsFirewallService";
    private int PRIORITY = 100;
    private int mId = 1234;

    private ArrayList<ContactBean> contactList;
    private WorkspaceDBHelper dbHelper;
    private String launcher = "tech.doujiang.launcher";
    private PhoneListener listener;
    private TelephonyManager tm;

    @Override
    public void onCreate() {
        // Build Foreground Service.
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.info)
                        .setContentTitle("MobileSafe")
                        .setContentText("Your phone is safe.");
        Intent resultIntent = new Intent(this, LauncherActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(LauncherActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, mBuilder.build());


        Log.e("CallSmsFirewallService", "onCreate");
        dbHelper = WorkspaceDBHelper.getDBHelper(this);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new PhoneListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();

        Thread endCall = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        endCall.start();
    }

    @Override
    public void onDestroy() {
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);//服务关闭的时候就取消电话状态的监听
    }

    // For those own more than one launcher app, it doesn't work.
//    public String getLauncherPackageName(Context context) {
//        final Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
//        if(res.activityInfo == null)
//        {
//            return "";
//        }
//
//        return res.activityInfo.packageName;
//    }

    private boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (currentPackageName != null && currentPackageName.equals(getPackageName())) {
            return true;
        }
        return false;
    }

    public boolean run() {
        String[] activePackages;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            activePackages = getActivePackages();
        } else {
            activePackages = getActivePackagesCompat();
        }
        if (activePackages != null) {
            for (String activePackage : activePackages) {
                if (activePackage.equals("com.google.android.calendar")) {

                }
            }
        }
    }

    String[] getActivePackagesCompat() {
        final List<ActivityManager.RunningTaskInfo> taskInfo = mActivityManager.getRunningTasks(1);
        final ComponentName componentName = taskInfo.get(0).topActivity;
        final String[] activePackages = new String[1];
        activePackages[0] = componentName.getPackageName();
        return activePackages;
    }

    String[] getActivePackages() {
        final Set<String> activePackages = new HashSet<String>();
        final List<ActivityManager.RunningAppProcessInfo> processInfos = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                activePackages.addAll(Arrays.asList(processInfo.pkgList));
            }
        }
        return activePackages.toArray(new String[activePackages.size()]);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class PhoneListener extends PhoneStateListener {
        long startTime;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.e("onCallStateChanged: ", Integer.toString(state) + " inComingNumber:" + incomingNumber);
            contactList = dbHelper.getContact();
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 空闲状态
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    String mode = "1";
                    if ("1".equals(mode) || "2".equals(mode)) {
                        Log.i(TAG, "挂断电话...");
                        endCall(incomingNumber);
                        // 呼叫记录不是立刻生成的 .
                        // 注册一个呼叫记录内容变化的内容观察者.
                        Uri uri = Uri.parse("content://call_log/calls");
                        getContentResolver().registerContentObserver(uri, true,
                                new CallLogObserver(new Handler(), incomingNumber));

                    }
                    break;
            }

            super.onCallStateChanged(state, incomingNumber);
        }

    }

    private class CallLogObserver extends ContentObserver {
        private String incomingNumber;

        public CallLogObserver(Handler handler, String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }

        // 当观察到数据发生改变的时候调用的方法.
        @Override
        public void onChange(boolean selfChange) {
            Log.i(TAG, "呼叫记录变化了.");
            deleteCallLog(incomingNumber);
            getContentResolver().unregisterContentObserver(this);//删除完通话记录之后就立马进行移除内容观察者
            super.onChange(selfChange);
        }

    }

    /**
     * 挂断电话的方法.
     *
     * @param incomingNumber
     */
    public void endCall(String incomingNumber) {
        try {
            Log.e("endCall: ",incomingNumber);
            Method method = Class.forName("android.os.ServiceManager")
                    .getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null,
                    new Object[] { TELEPHONY_SERVICE });
            ITelephony iTelephony = ITelephony.Stub.asInterface(binder);
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 移除呼叫记录
     *
     * @param incomingNumber
     */
    public void deleteCallLog(String incomingNumber) {
        Log.e("deleteCallLog: ", incomingNumber);
        Uri uri = Uri.parse("content://call_log/calls");
        getContentResolver().delete(uri, "number=?",
                new String[] { incomingNumber });
    }
}
