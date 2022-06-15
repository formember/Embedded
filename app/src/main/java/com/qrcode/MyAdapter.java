package com.qrcode;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.database.DBOpenHelper;

import java.util.List;

public class MyAdapter extends BaseAdapter implements View.OnClickListener {
    //上下文
    private Context context;
    //数据项
    private List<Pair<String,Boolean>> data;
    private DBOpenHelper dbOpenHelper;
    public MyAdapter(List<Pair<String,Boolean>> data,DBOpenHelper dbOpenHelper){
        this.data = data;
        this.dbOpenHelper=dbOpenHelper;
    }
    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(context == null)
            context = viewGroup.getContext();
        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vlist,null);
            viewHolder = new ViewHolder();
            viewHolder.mTv = (TextView)view.findViewById(R.id.mTv);
            viewHolder.mBtn = (Button)view.findViewById(R.id.mBtn);
            view.setTag(viewHolder);
        }
        //获取viewHolder实例
        viewHolder = (ViewHolder)view.getTag();
        //设置数据
        viewHolder.mTv.setText(data.get(i).first);
        //设置数据
        viewHolder.mBtn.setTag(R.id.btn,i);//添加此代码
        if(data.get(i).second)
            viewHolder.mBtn.setVisibility(View.INVISIBLE);
        else{
            viewHolder.mBtn.setText("设置NFC");
            viewHolder.mBtn.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mBtn: {

                int b = (int) view.getTag(R.id.btn);
                String username = data.get(b).first;
                Intent intent = new Intent(context, BindNFCActivity.class);
                intent.putExtra("username", username);
                context.startActivity(intent);
            }
        }
    }

    static class ViewHolder{
        TextView mTv;
        Button mBtn;
    }

}