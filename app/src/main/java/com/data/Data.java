package com.data;

import android.app.Application;
import com.database.DBOpenHelper;

public class Data extends Application{
    static DBOpenHelper dbOpenHelper;  //定义全局变量

    public static DBOpenHelper getDbOpenHelper() {
        return dbOpenHelper;
    }
    @Override
    public void onCreate(){  //创建该类
        super.onCreate();
        dbOpenHelper=new DBOpenHelper(this);
    }
}
