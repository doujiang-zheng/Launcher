package tech.doujiang.launcher.database;

import android.content.Context;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabase.CursorFactory;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.telecom.Call;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import tech.doujiang.launcher.model.CallLogBean;
import tech.doujiang.launcher.model.ContactBean;
import tech.doujiang.launcher.model.MessageBean;

/**
 * Created by 豆浆 on 2016-07-12.
 * 1. 通讯录 增删改查
 * 2. 通话记录 增查
 * 3. 短信 增查
 * PRAGMA foreign_keys = ON;使外键约束起作用
 */
public class WorkspaceDBHelper extends SQLiteOpenHelper {

    private static final String key = "launcher_key";
    public static final String DB_NAME = "worksapce.db";
    public static final int DB_VERSION = 1;
    private static WorkspaceDBHelper dbHelper;
    private static final String CREATE_CONTACT = "create table Contact("
            + "id integer primary key autoincrement,"
            + "lookUpKey string,"
            + "name string,"
            + "number string,"
            + "photoId integer"
            + ");";
    private static final String CREATE_CALLLOG = "create table CallLog("
            + "id integer,"
            + "date integer,"
            + "duration integer,"
            + "type integer," // 1. INCOMING_TYPE 2. OUTGOING_TYPE 3. MISSED_TYPE
            + "primary key(id, date, duration, type),"
            + "foreign key(id) references Contact(id)"
            + " );";
    private static final String CREATE_MESSAGE = "create table Message("
            + "id integer, "
            + "date integer,"
            + "text integer,"
            + "type integer," // 1. INCOMING_TYPE 2. OUTGOING_TYPE
            + "primary key(id, date, text, type),"
            + "foreign key(id) references Contact(id)"
            + ")";

    public WorkspaceDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_CALLLOG);
            db.execSQL(CREATE_CONTACT);
            db.execSQL(CREATE_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public static WorkspaceDBHelper getDBHelper(Context context) {
        if (dbHelper == null)
            SQLiteDatabase.loadLibs(context);
            dbHelper = new WorkspaceDBHelper(context, DB_NAME, null, DB_VERSION);
        return  dbHelper;
    }
    // Contact, CallLog, Message Interaction
    public void addContact(ArrayList<ContactBean> contacts, WorkspaceDBHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(key);
        db.beginTransaction();
        try {
            for (ContactBean contact : contacts) {
                db.execSQL("INSERT INTO Contact VALUES(?, ?, ?, ?)",
                        new Object[]{contact.getLookUpKey(), contact.getDisplayName(), contact.getPhoneNum(), contact.getPhotoId()});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void addCallLog(ArrayList<CallLogBean> callLogs, WorkspaceDBHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(key);
        db.beginTransaction();
        try {
            for (CallLogBean callLog : callLogs) {
                db.execSQL("INSERT INTO CallLog VALUES(?, ?, ?, ?)",
                        new Object[]{callLog.getId(), callLog.getDate(), callLog.getDuration(), callLog.getType()});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void addMessage(ArrayList<MessageBean> messages, WorkspaceDBHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(key);
        db.beginTransaction();
        try {
            for(MessageBean message : messages) {
                db.execSQL("INSET INTO Message VALUES(?, ?, ?, ?)",
                        new Object[]{message.getId(), message.getDate(), message.getText(), message.getType()});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<ContactBean> getContact(WorkspaceDBHelper dbHelper) {
        ArrayList<ContactBean> contacts = new ArrayList<ContactBean>();
        Cursor cursor = dbHelper.getWritableDatabase(key).rawQuery("select * from Contact", null);
        while (cursor.moveToNext()) {
            ContactBean contact = new ContactBean();
            contact.setContactId(cursor.getInt(cursor.getColumnIndex("id")));
            contact.setLookUpKey(cursor.getString(cursor.getColumnIndex("lookUpKey")));
            contact.setDisplayName(cursor.getString(cursor.getColumnIndex("name")));
            contact.setPhoneNum(cursor.getString(cursor.getColumnIndex("number")));
            contact.setPhotoId(cursor.getLong(cursor.getColumnIndex("photoId")));
            contacts.add(contact);
        }
        cursor.close();
        return contacts;
    }

    public ArrayList<CallLogBean> getCallLog(WorkspaceDBHelper dbHelper) {
        ArrayList<CallLogBean> callLogs = new ArrayList<CallLogBean>();
        Cursor cursor = dbHelper.getWritableDatabase(key).rawQuery("select * from CallLog", null);
        while (cursor.moveToNext()) {
            CallLogBean callLog = new CallLogBean();
            callLog.setId(cursor.getInt(cursor.getColumnIndex("id")));
            callLog.setDate(cursor.getString(cursor.getColumnIndex("date")));
            callLog.setDuration(cursor.getInt(cursor.getColumnIndex("duration")));
            callLog.setType(cursor.getInt(cursor.getColumnIndex("type")));
            callLogs.add(callLog);
        }
        cursor.close();
        return callLogs;
    }

    public ArrayList<MessageBean> getMessage(WorkspaceDBHelper dbHelper) {
        ArrayList<MessageBean> messages = new ArrayList<MessageBean>();
        Cursor cursor = dbHelper.getWritableDatabase(key).rawQuery("select * from Message", null);
        while (cursor.moveToNext()) {
            MessageBean message = new MessageBean();
            message.setId(cursor.getInt(cursor.getColumnIndex("id")));
            message.setDate(cursor.getString(cursor.getColumnIndex("date")));
            message.setText(cursor.getString(cursor.getColumnIndex("text")));
            message.setType(cursor.getInt(cursor.getColumnIndex("type")));
            messages.add(message);
        }
        cursor.close();
        return messages;
    }

}
