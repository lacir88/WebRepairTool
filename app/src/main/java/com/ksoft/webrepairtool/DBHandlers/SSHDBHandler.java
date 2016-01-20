package com.ksoft.webrepairtool.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ksoft.webrepairtool.Beans.ConnectionRecord;
import com.ksoft.webrepairtool.Beans.SSHCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laci on 2016.01.19..
 */
public class SSHDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WebRepairTool_SSH_DB.db";
    private static final String TABLE_SSHCOMMANDS = "sshCommands";

    private static final String COLUMN_ID = "id";
    public static final String COLUMN_HOSTNAME = "hostname";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_COMMAND_NAME = "command_name";
    public static final String COLUMN_COMMAND_STRING = "command_string";

    public SSHDBHandler(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SSHCOMMANDS_TABLE = "CREATE TABLE " +
                TABLE_SSHCOMMANDS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_HOSTNAME + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_COMMAND_NAME + " TEXT,"
                + COLUMN_COMMAND_STRING + " TEXT" + ")";
        db.execSQL(CREATE_SSHCOMMANDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SSHCOMMANDS);
        onCreate(db);
    }

    public void addCommand(SSHCommand sshCommand) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_HOSTNAME, sshCommand.getHost());
        values.put(COLUMN_USERNAME, sshCommand.getUserName());
        values.put(COLUMN_PASSWORD, sshCommand.getPassword());
        values.put(COLUMN_COMMAND_NAME, sshCommand.getCommandName());
        values.put(COLUMN_COMMAND_STRING, sshCommand.getCommandString());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_SSHCOMMANDS, null, values);
        db.close();
    }

    public List<SSHCommand> findAllCommands () {
        List<SSHCommand> commandList = new ArrayList<SSHCommand>();

        String query = "Select * FROM " + TABLE_SSHCOMMANDS;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                SSHCommand cr = new SSHCommand();
                cr.setId(Integer.toString(cursor.getInt(0)));
                cr.setHost(cursor.getString(1));
                cr.setUserName(cursor.getString(2));
                cr.setPassword(cursor.getString(3));
                cr.setCommandName(cursor.getString(4));
                cr.setCommandString(cursor.getString(5));
                commandList.add(cr);
            } while (cursor.moveToNext());
        }

        return commandList;
    }


    public SSHCommand selectCommandById(String updateId) {

        String query = "Select * FROM " + TABLE_SSHCOMMANDS + " WHERE " + COLUMN_ID + " =  \"" + updateId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        SSHCommand sshCommand = new SSHCommand();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            sshCommand.setId(Integer.toString(cursor.getInt(0)));
            sshCommand.setHost(cursor.getString(1));
            sshCommand.setUserName(cursor.getString(2));
            sshCommand.setPassword(cursor.getString(3));
            sshCommand.setCommandName(cursor.getString(4));
            sshCommand.setCommandString(cursor.getString(5));

            cursor.close();
        } else {
            sshCommand = null;
        }
        db.close();
        return sshCommand;

    }

    public void updateCommand(SSHCommand sshCommand) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_HOSTNAME, sshCommand.getHost());
        values.put(COLUMN_USERNAME, sshCommand.getUserName());
        values.put(COLUMN_PASSWORD, sshCommand.getPassword());
        values.put(COLUMN_COMMAND_NAME, sshCommand.getCommandName());
        values.put(COLUMN_COMMAND_STRING, sshCommand.getCommandString());

        String where = "id=?";
        String[] whereArgs = new String[]{sshCommand.getId()};

        db.update(TABLE_SSHCOMMANDS, values, where, whereArgs);

        db.close();

    }
}
