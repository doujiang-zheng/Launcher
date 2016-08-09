package tech.doujiang.launcher.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by doujiang_zheng on 16/8/10.
 */
public class SMSHandler extends Handler {
    public static final String TAG = "SMSHandler";
    private Context mContext;

    public SMSHandler(Context context) {
        super();
        this.mContext = context;
    }

    public void handleMessage(Message message) {
        Log.i(TAG,  "handleMessage: " + message);
        MessageItem item = (MessageItem) message.obj;
        //delete the sms
        Uri uri = ContentUris.withAppendedId(SMS.CONTENT_URI, item.getId());
        mContext.getContentResolver().delete(uri, null, null);
        Log.i(TAG,  "delete sms item: " + item);
    }

}