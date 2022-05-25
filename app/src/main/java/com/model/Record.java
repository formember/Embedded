package com.model;

import java.security.MessageDigest;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
public class Record {
    public static final String TABLE_NAME="RECORD";
    public static final String COLUMN_RECORD_ID = "record_id";
    public static final String COLUMN_USERID= "user_id";
    public static final String COLUMN_TYPE= "type";
    public static final String COLUMN_DATETIME = "datetime";

    private int record_id;
    private int user_id;
    private int type;
    private Date datetime;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USERID+" INTEGER,"
                    + COLUMN_DATETIME + " varchar(20),"
                    + COLUMN_TYPE + " TEXT,"
                    + "CONSTRAINT "+ COLUMN_USERID
                    + " FOREIGN KEY (USER_id)"
                    + " REFERENCES USER(id)"
                    + ")";

    public Record() {
    }

    public Record(int record_id, int user_id, int type,Date datetime) {
        this.record_id = record_id;
        this.user_id = user_id;
        this.type = type;
        this.datetime=datetime;
    }

    public int getId() {
        return record_id;
    }

    public int getuserid() {
        return user_id;
    }
    public int getType(){
        return type;
    }
    public Date getdate(){
        return datetime;
    }
    public void setuserid(int user_id) {
        this.user_id = user_id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}

