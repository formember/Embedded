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
            //???????????????
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password.setSelected(true);
            //????????????
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    private void setrePasswordVisibility() {
        if (iv_re_see_password.isSelected()) {
            iv_re_see_password.setSelected(false);
            //???????????????
            et_repassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_re_see_password.setSelected(true);
            //????????????
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
            showToast("???????????????????????????");
            return;
        }
        if (password.isEmpty()){
            showToast("???????????????????????????");
            return;
        }
        if (getRePassword().isEmpty()){
            showToast("????????????????????????????????????");
            return;
        }
        if(!repassword.equals(password)){
            showToast("?????????????????????????????????????????????!");
            return;
        }
        if(age.isEmpty()){
            showToast("????????????????????????!");
            return;
        }
        boolean isregister = true;
        try{
            dbOpenHelper.insertUser(account, password, Integer.parseInt(age));
        }catch (NumberFormatException ignored){
            showToast("????????????????????????!");
        }
        if(isregister){
            showToast("???????????????");
            Intent intent=new Intent(this, MainActivity1.class);
            startActivity(intent);
            finish();
        }else{
            showToast("???????????????????????????????????????");
        }

    }

    //????????????
    public String getAccount() {
        return et_name.getText().toString().trim();//????????????
    }
    //????????????
    public String getPassword() {
        return et_password.getText().toString().trim();//????????????
    }
    //??????????????????
    public String getRePassword() {
        return et_repassword.getText().toString().trim();//????????????
    }
    //????????????
    public String getAge() { return et_age.getText().toString().trim();}//????????????

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}