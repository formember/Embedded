package com.qrcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ListView;

import com.database.DBOpenHelper;

import java.util.List;

public class AllUserActivity extends AppCompatActivity {

    private ListView mList;
    private List<Pair<String,Boolean>> data;
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        dbOpenHelper=new DBOpenHelper(this);
        data=dbOpenHelper.getAllUser();
        mList=(ListView)findViewById(R.id.listView);
        MyAdapter adapter=new MyAdapter(data,dbOpenHelper);
        mList.setAdapter(adapter);
    }
}