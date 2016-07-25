package tech.doujiang.launcher.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.app.IntentService;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;

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
public class CallSmsFirewallService extends IntentService {
    public static final String TAG = "CallSmsFirewallService";
    //private InnerSmsReceiver receiver;
    private ArrayList<String> dao;
    private PhoneListener listener;

    private TelephonyManager tm;

    public CallSmsFirewallService() {
        super("CallSmsFirewallService");
    }

    public CallSmsFirewallService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate() {
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new PhoneListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);//服务关闭的时候就取消电话状态的监听
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
            switch (state) {

                case TelephonyManager.CALL_STATE_IDLE: // 空闲状态
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    String mode = "1";
                    //String mode = dao.contains(incomingNumber);
                    if ("1".equals(mode) || "2".equals(mode)) {
                        Log.i(TAG, "挂断电话...");
                        endCall(incomingNumber);// 呼叫记录不是立刻生成的 .
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

            // Object obj = getSystemService(TELEPHONY_SERVICE);
            // ITelephony iTelephony = ITelephony.Stub.asInterface((IBinder)
            // obj);
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
        Uri uri = Uri.parse("content://call_log/calls");
        getContentResolver().delete(uri, "number=?",
                new String[] { incomingNumber });
    }
}
