package com.qrcode;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.data.Data;
import com.database.DBOpenHelper;
public class RegisterActivity extends AppCompatActivity {

    private EditText et_name;
    private EditText et_password;
    private EditText et_repassword;
    private EditText et_age;
    private Button mRegisterBtn;
    private ImageView iv_see_password;
    private ImageView iv_re_see_password;
    private TextView textClick;
    private DBOpenHelper dbOpenHelper;
    Data application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbOpenHelper=new DBOpenHelper(this);
        initViews();
        setupEvents();
    }
    private void initViews(){
        et_name= findViewById(R.id.et_account);
        et_password=findViewById(R.id.et_password);
        et_repassword=findViewById(R.id.et_re_password);
        et_age=findViewById(R.id.et_age);
        mRegisterBtn=findViewById(R.id.btn_register);
        iv_see_password = findViewById(R.id.iv_see_password);
        iv_re_see_password= findViewById(R.id.iv_re_see_password);
    }
    private void setupEvents(){
        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View v) {
                register();
            }
        });
        iv_see_password.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setPasswordVisibility();
            }
        });
        iv_re_see_password.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setrePasswordVisibility();
            }
        });
    }

    private void setPasswordVisibility() {
        if (iv_see_password.isSelected()) {
            iv_see_password.setSelected(false);
            //密码不可见
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password.setSelected(true);
            //密码可见
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    private void setrePasswordVisibility() {
        if (iv_re_see_password.isSelected()) {
            iv_re_see_password.setSelected(false);
            //密码不可见
            et_repassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_re_see_password.setSelected(true);
            //密码可见
            et_repassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void register(){
        String account=getAccount();
        String password=getPassword();
        String repassword=getRePassword();
        String age=getAge();
        if (account.isEmpty()){
            showToast("你输入的账号为空！");
            return;
        }
        if (password.isEmpty()){
            showToast("你输入的密码为空！");
            return;
        }
        if (getRePassword().isEmpty()){
            showToast("你第二次输入的密码为空！");
            return;
        }
        if(!repassword.equals(password)){
            showToast("两次输入密码不一致，请重新输入!");
            return;
        }
        if(age.isEmpty()){
            showToast("你输入的年龄为空!");
            return;
        }
        boolean isregister = true;
        try{
            dbOpenHelper.insertUser(account, password, Integer.parseInt(age));
        }catch (NumberFormatException ignored){
            showToast("你输入的年龄非法!");
        }
        if(isregister){
            showToast("注册成功！");
            Intent intent=new Intent(this, MainActivity1.class);
            startActivity(intent);
            finish();
        }else{
            showToast("已有用户，请换个工号试试！");
        }

    }

    //获取账号
    public String getAccount() {
        return et_name.getText().toString().trim();//去掉空格
    }
    //获取密码
    public String getPassword() {
        return et_password.getText().toString().trim();//去掉空格
    }
    //获取确认密码
    public String getRePassword() {
        return et_repassword.getText().toString().trim();//去掉空格
    }
    //获取年龄
    public String getAge() { return et_age.getText().toString().trim();}//去掉空格

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}