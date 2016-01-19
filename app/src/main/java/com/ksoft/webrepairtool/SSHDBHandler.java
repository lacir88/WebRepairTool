package com.ksoft.webrepairtool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laci on 2016.01.19..
 */
public class SSHDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WebRepairToolDB.db";
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


}
