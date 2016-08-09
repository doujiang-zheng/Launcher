package tech.doujiang.launcher.Receiver;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tech.doujiang.launcher.database.WorkspaceDBHelper;
import tech.doujiang.launcher.model.ContactBean;
import tech.doujiang.launcher.model.MessageBean;
import tech.doujiang.launcher.util.Constant;
import tech.doujiang.launcher.util.SmsWriteOpUtil;

public class SmsReceiver extends BroadcastReceiver {
    private WorkspaceDBHelper dbHelper;
    private ArrayList<ContactBean> contactList;
    private Constant constant;
    private ArrayList<String> numbers;

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (dbHelper == null) {
            dbHelper = WorkspaceDBHelper.getDBHelper(context);
        }

        Object [] pdus= (Object[]) intent.getExtras().get("pdus");
        for(Object pdu:pdus) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String content = smsMessage.getMessageBody();
//            long date = smsMessage.getTimestampMillis();
            long date = System.currentTimeMillis();
            Date timeDate = new Date(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(timeDate);

            numbers = dbHelper.getNumber();
            constant = new Constant();
            if (constant.run(context)) {
                if (numbers.contains(sender)) {
                    MessageBean message = new MessageBean();
                    message.setType(constant.LAYOUT_INCOMING);
                    message.setText(content);
                    message.setDate(smsMessage.getTimestampMillis());
                    Log.e("text: ", content);
                    Log.e("date: ", Long.toString(smsMessage.getTimestampMillis()));
                    contactList = dbHelper.getContact();
                    for (ContactBean contact : contactList) {
                        if (contact.getPhoneNum().equals(sender)) {
                            message.setId(contact.getContactId());
                            break;
                        }
                    }
                    dbHelper.addMessage(message);
                } else {
                    abortBroadcast();
                }
            } else {
                if (numbers.contains(sender)) {

                }
            }

            Log.e("短信来自:", sender);
            Log.e("短信内容:", content);
            Log.e("短信时间:", time);

            Log.e("deleteSMS: ", sender);
            if (!SmsWriteOpUtil.isWriteEnabled(context)) {
                SmsWriteOpUtil.setWriteEnabled(context, true);
            }
            Uri deleteUri = Uri.parse("content://sms");
            context.getContentResolver().delete(deleteUri, "address=" + sender, null);
            deleteSMS(context, sender);
        }
    }

    public void deleteSMS(Context context, String incomingNUmber) {
//        try {
//            ContentResolver CR = context.getContentResolver();
//            // Query SMS
//            Uri uriSms = Uri.parse("content://sms/sent");
//            Cursor c = CR.query(uriSms,
//                    new String[] { "_id", "thread_id" }, null, null, null);
//            if (null != c && c.moveToFirst()) {
//                do {
//                    // Delete SMS
//                    long threadId = c.getLong(1);
//                    CR.delete(Uri.parse("content://sms/conversations/" + threadId),
//                            null, null);
//                    Log.d("deleteSMS", "threadId:: "+threadId);
//                } while (c.moveToNext());
//            }
//        } catch (Exception e) {
//            Log.d("deleteSMS", "Exception:: " + e);
//        }
    }

}
