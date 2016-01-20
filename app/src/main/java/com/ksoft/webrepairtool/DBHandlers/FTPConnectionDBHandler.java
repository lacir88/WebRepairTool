package com.ksoft.webrepairtool.DBHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ksoft.webrepairtool.Beans.ConnectionRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laci on 2015.12.29..
 */
public class FTPConnectionDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WebRepairToolDB.db";
    private static final String TABLE_CONNECTIONS = "connections";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_HOSTNAME = "hostname";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public FTPConnectionDBHandler(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONNECTIONS_TABLE = "CREATE TABLE " +
                TABLE_CONNECTIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_HOSTNAME + " TEXT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_CONNECTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONNECTIONS);
        onCreate(db);
    }


    public void addConnection(ConnectionRecord c) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOSTNAME, c.getHost());
        values.put(COLUMN_USERNAME, c.getUserName());
        values.put(COLUMN_PASSWORD, c.getPassword());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_CONNECTIONS, null, values);
        db.close();
    }

    public void updateConnection(ConnectionRecord c) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_HOSTNAME, c.getHost());
        values.put(COLUMN_USERNAME, c.getUserName());
        values.put(COLUMN_PASSWORD, c.getPassword());

        String where = "id=?";
        String[] whereArgs = new String[] {c.getID()};

        db.update(TABLE_CONNECTIONS, values, where, whereArgs);

        db.close();
    }

    public List<ConnectionRecord> findAllConnectionRecords () {
        List<ConnectionRecord> listcr = new ArrayList<ConnectionRecord>();

        String query = "Select * FROM " + TABLE_CONNECTIONS;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ConnectionRecord cr = new ConnectionRecord();
                cr.setID(Integer.toString(cursor.getInt(0)));
                cr.setHost(cursor.getString(1));
                cr.setUserName(cursor.getString(2));
                cr.setPassword(cursor.getString(3));
                listcr.add(cr);
            } while (cursor.moveToNext());
        }
        db.close();
        return listcr;
    }

    public ConnectionRecord selectConnectionById(String connectionId) {


        String query = "Select * FROM " + TABLE_CONNECTIONS + " WHERE " + COLUMN_ID + " =  \"" + connectionId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ConnectionRecord cr = new ConnectionRecord();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            cr.setID(Integer.toString(cursor.getInt(0)));
            cr.setHost(cursor.getString(1));
            cr.setUserName(cursor.getString(2));
            cr.setPassword(cursor.getString(3));

            /*product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setQuantity(Integer.parseInt(cursor.getString(2)));*/

            cursor.close();
        } else {
            cr = null;
        }
        db.close();
        return cr;

    }


    /*public Product findProduct(String productname) {
        String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Product product = new Product();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            product.setID(Integer.parseInt(cursor.getString(0)));
            product.setProductName(cursor.getString(1));
            product.setQuantity(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        } else {
            product = null;
        }
        db.close();
        return product;
    }*/
}
