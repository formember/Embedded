package com.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.model.NFC;
import com.model.Record;
import com.model.User;
import com.qrcode.MainActivity1;
import com.qrcode.R;

import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_db";
    private static final int DATABASE_VERSION = 1;
    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句并执行
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(Record.CREATE_TABLE);
        db.execSQL(NFC.CREATE_TABLE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()) { // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }
    public boolean insertUser(String username,String password,int age) {
        // get writable database as we want to write data
        if(getUser(username)!=null){
            //创建失败
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(User.COLUMN_USERNAME, username);
        values.put(User.COLUMN_PASSWORD, password);
        values.put(User.COLUMN_AGE, age);

        // insert row
        long id = db.insert(User.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        //创建成功
        return true;
    }
    public User getUser(String username) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID, User.COLUMN_USERNAME, User.COLUMN_PASSWORD,User.COLUMN_AGE},
                User.COLUMN_USERNAME + "=?",
                new String[]{String.valueOf(username)}, null, null, null, null);

        if (cursor.moveToFirst())
            cursor.moveToFirst();
        else{
            return null;
        }

        // prepare note object
        User user = new User(
                cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_USERNAME)),
                cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD)),
                cursor.getInt(cursor.getColumnIndex(User.COLUMN_AGE)));

        // close the db connection
        cursor.close();

        return user;
    }
    public List<Pair<String,Boolean>> getAllUser() {
        List<Pair<String,Boolean>> users = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + User.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        List<Integer> allNfcs=getAllNfc();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                if(allNfcs.contains(cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)))){
                    Pair<String,Boolean> tmppair=new Pair<String,Boolean>(cursor.getString(cursor.getColumnIndex(User.COLUMN_USERNAME)),true);
                    users.add(tmppair);
                }else{
                    Pair<String,Boolean> tmppair=new Pair<String,Boolean>(cursor.getString(cursor.getColumnIndex(User.COLUMN_USERNAME)),false);
                    users.add(tmppair);
                }
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();

        // return notes list
        return users;
    }

    public List<Integer> getAllNfc(){
        List<Integer> Nfcs=new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + NFC.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Nfcs.add(cursor.getInt(cursor.getColumnIndex(NFC.COLUMN_USERID)));
            } while (cursor.moveToNext());
        }
        return Nfcs;
    }
    public boolean bind_nfd(String username,String nfc){
        User user=getUser(username);
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db1 = this.getWritableDatabase();

        Cursor cursor = db.query(NFC.TABLE_NAME,
                new String[]{NFC.COLUMN_NFC_ID, NFC.COLUMN_USERID, NFC.COLUMN_NFC},
                NFC.COLUMN_NFC + "=?",
                new String[]{String.valueOf(nfc)}, null, null, null, null);
        if (cursor.moveToFirst()){
            return false;
        }
        else{
            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them.
            values.put(NFC.COLUMN_USERID, user.getId());
            values.put(NFC.COLUMN_NFC, nfc);

            // insert row
            long id = db1.insert(NFC.TABLE_NAME, null, values);

            // close db connection
            db.close();
            return true;
        }
    }

    public Pair<String,Boolean> login_by_nfc(String nfc){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(NFC.TABLE_NAME,
                new String[]{NFC.COLUMN_NFC_ID, NFC.COLUMN_USERID, NFC.COLUMN_NFC},
                NFC.COLUMN_NFC + "=?",
                new String[]{String.valueOf(nfc)}, null, null, null, null);


        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            Date date = new Date();
            insertExcord(cursor.getInt(cursor.getColumnIndex(NFC.COLUMN_USERID)),date, MainActivity1.NFCTYPE);
            SQLiteDatabase db1 = this.getReadableDatabase();
            Cursor cursor1 = db1.query(User.TABLE_NAME,
                    new String[]{User.COLUMN_ID, User.COLUMN_USERNAME, User.COLUMN_PASSWORD,User.COLUMN_AGE},
                    User.COLUMN_ID + "=?",
                    new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(NFC.COLUMN_USERID)))}, null, null, null, null);
            if(cursor1.moveToFirst()){
                cursor1.moveToFirst();
                Pair<String,Boolean> newPair=new Pair<String,Boolean>(cursor1.getString(cursor1.getColumnIndex(User.COLUMN_USERNAME)),true);
                return newPair;
            }
            else{
                Pair<String,Boolean> newPair=new Pair<String,Boolean>("",false);
                return newPair;
            }
        }else
        {
            Pair<String,Boolean> newPair=new Pair<String,Boolean>("",false);
            return newPair;
        }
    }
    public boolean login(String username,String password,int type){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID, User.COLUMN_USERNAME, User.COLUMN_PASSWORD,User.COLUMN_AGE},
                User.COLUMN_USERNAME + "=?",
                new String[]{String.valueOf(username)}, null, null, null, null);

        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            if(cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD)).equals(password)) {
                //-----------------------------
                //添加记录
                Date date = new Date();
                insertExcord(cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)),date,type);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    public boolean testUser(String username,String password){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(User.TABLE_NAME,
                new String[]{User.COLUMN_ID, User.COLUMN_USERNAME, User.COLUMN_PASSWORD,User.COLUMN_AGE},
                User.COLUMN_USERNAME + "=?",
                new String[]{String.valueOf(username)}, null, null, null, null);

        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            if(cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD)).equals(password)) {
                //-----------------------------
                //添加记录
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }


    public void insertExcord(int user_id, Date datetime, int type){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put(Record.COLUMN_USERID, user_id);
        values.put(Record.COLUMN_TYPE, type);
        values.put(Record.COLUMN_DATETIME, simpleDateFormat.format(datetime));

        // insert row
        long id = db.insert(Record.TABLE_NAME, null, values);

        // close db connection
        db.close();
    }
    public List<Record> getlastfiveRecord(String username){
        List<Record> records = new ArrayList<>();
        User user=getUser(username);
        if(user==null)
        {
            return null;
        }
        // Select All Query
        int user_id=user.getId();
        String selectQuery = "SELECT  * FROM " + Record.TABLE_NAME +" WHERE "+Record.COLUMN_USERID+"="+user_id+" ORDER BY "+Record.COLUMN_RECORD_ID+" desc " + "LIMIT 0,5";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setuserid(cursor.getInt(cursor.getColumnIndex(Record.COLUMN_USERID)));
                Date date=null;
                try{
                    date= simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(Record.COLUMN_DATETIME)));
                }catch (ParseException e){
                    e.printStackTrace();
                }
                record.setDatetime(date);
                record.setType(cursor.getInt(cursor.getColumnIndex(Record.COLUMN_TYPE)));
                records.add(record);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return records;
    }

}

