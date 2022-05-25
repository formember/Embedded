package com.model;

import java.security.MessageDigest;
import org.apache.commons.codec.digest.DigestUtils;
public class User {
    public static final String TABLE_NAME="USER";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_AGE = "age";

    private int id;
    private String username;
    private String password;
    private int age;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USERNAME + " varchar(20),"
                    + COLUMN_PASSWORD + " varchar(50),"
                    + COLUMN_AGE + " INTEGER"
                    + ")";

    public User() {
    }

    public User(int id, String username, String password,int age) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.age=age;
    }

    public int getId() {
        return id;
    }

    public String getusername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setpassword(String password) {
        this.password = DigestUtils.md5Hex(password.getBytes());
    }
    public boolean logintest(String password){
        return this.password.equals(password);
    }

}

