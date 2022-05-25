package com.qrcode;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.database.DBOpenHelper;
import com.model.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class RecordActivity extends AppCompatActivity {

    private ListView list1;
    private DBOpenHelper dbOpenHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        dbOpenHelper=new DBOpenHelper(this);
        String username= getIntent().getStringExtra("username");
        list1=findViewById(R.id.list1);
        List<Record> list_record= dbOpenHelper.getlastfiveRecord(username);
        List<Map<String, Object>> listItems=new ArrayList<Map<String,Object>>();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(list_record!=null)
        {
            for (int i = 0; i < list_record.size(); i++) {
                Map<String, Object> listItem=new HashMap<String,Object>();
                listItem.put("header", simpleDateFormat.format(list_record.get(i).getdate()));
                System.out.println("++++"+list_record.get(i).getdate());
                int type=list_record.get(i).getType();
                String second;
                if(type==MainActivity1.NORMALTYPE)
                {
                    second="密码登录";
                }else if(type==MainActivity1.NFCTYPE)
                {
                    second="NPC登录";
                }else{
                    second="扫码登录";
                }
                listItem.put("second",second);
                listItems.add(listItem);
            }
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.item_list,new String[]{"header","second"},new int[]{R.id.tvF,R.id.tvS});
        list1.setAdapter(simpleAdapter);
    }
}