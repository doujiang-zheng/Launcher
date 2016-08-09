package tech.doujiang.launcher.Receiver;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
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
    private String defaultSmsApp;

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (dbHelper == null) {
            dbHelper = WorkspaceDBHelper.getDBHelper(context);
        }
        Log.e("DefaultSmsApp: ", Telephony.Sms.getDefaultSmsPackage(context));
        Object [] pdus= (Object[]) intent.getExtras().get("pdus");
        for(Object pdu:pdus) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String content = smsMessage.getMessageBody();
            long date = System.currentTimeMillis();
            Date timeDate = new Date(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(timeDate);

            Long maxDate = dbHelper.getMaxDate();
            numbers = dbHelper.getNumber();
            constant = new Constant();
            if (numbers.contains(sender) && smsMessage.getTimestampMillis() < maxDate) {
                    MessageBean message = new MessageBean();
                    message.setType(constant.LAYOUT_INCOMING);
                    message.setText(content);
                    message.setDate(smsMessage.getTimestampMillis());
                    contactList = dbHelper.getContact();
                    for (ContactBean contact : contactList) {
                        if (contact.getPhoneNum().equals(sender)) {
                            message.setId(contact.getContactId());
                            break;
                        }
                    }
                    dbHelper.addMessage(message);

                Log.e("MessageId: ", Integer.toString(message.getId()));
                Log.e("MessageDate: ", Long.toString(message.getDate()));
                Log.e("MessageText: ", message.getText());
                Log.e("MessageType: ", Integer.toString(message.getType()));

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(context);
                    Intent smsIntent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                    smsIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.getPackageName());
                    smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(smsIntent);
                }
                abortBroadcast();
                Uri deleteUri = Uri.parse("content://sms");
                context.getContentResolver().delete(deleteUri, "address=" + sender, null);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    Log.e("Refresh: ", defaultSmsApp);
                    Intent smsIntent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                    smsIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, defaultSmsApp);
                    smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(smsIntent);
                }
            }

            Log.e("短信来自:", sender);
            Log.e("短信内容:", content);
            Log.e("短信时间:", time);

            Log.e("deleteSMS: ", sender);
        }
    }

}
