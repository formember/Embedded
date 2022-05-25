package com.model;

import java.security.MessageDigest;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
public class NFC {
    public static final String TABLE_NAME="NFC";
    public static final String COLUMN_NFC_ID = "nfc_id";
    public static final String COLUMN_USERID= "user_id";
    public static final String COLUMN_NFC= "nfc";

    private int nfc_id;
    private int user_id;
    private String nfc;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_NFC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USERID+" INTEGER,"
                    + COLUMN_NFC + " varchar(50),"
                    + "CONSTRAINT "+ COLUMN_USERID
                    + " FOREIGN KEY (USER_id)"
                    + " REFERENCES USER(id)"
                    + ")";

    public NFC() {
    }

    public NFC(int nfc_id, int user_id,String nfc) {
        this.nfc_id = nfc_id;
        this.user_id = user_id;
        this.nfc = nfc;
    }

    public int getId() {
        return nfc_id;
    }

    public int getuserid() {
        return user_id;
    }
    public String getNfc(){
        return nfc;
    }
    public void setuserid(int user_id) {
        this.user_id = user_id;
    }

    public void setNfc(String nfc) {
        this.nfc = nfc;
    }
}

