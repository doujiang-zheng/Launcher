package tech.doujiang.launcher.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telecom.Call;

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

    public static final String DB_NAME = "worksapce.db";
    private static final String CREATE_CONTACT = "create table Contact("
            + "id integer primary key autoincrement,"
            + "lookUpKey string,"
            + "name string,"
            + "number integer,"
            + "photoId integer,"
            + "formattedNumber string"
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

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public WorkspaceDBHelper getDBHelper() {}
    public void addContact(ContactBean contact) {}
    public void addCallLog(CallLogBean callLog) {}
    public void addMessage(MessageBean message) {}
    public ArrayList<ContactBean> getContact() {}
    public ArrayList<CallLogBean> getCallLog() {}
    public ArrayList<MessageBean> getMessage() {}
}
