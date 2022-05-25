package com.qrcode;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.database.DBOpenHelper;
import com.google.zxing.WriterException;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity1 extends AppCompatActivity
     implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static int NORMALTYPE=1;
    public static int NFCTYPE=2;
    public static int QRTYPE=3;
    private EditText et_name;
    private EditText et_password;
    private Button mLoginBtn;
    private Button QRLoginBtn;
    private ImageView iv_see_password;
    private TextView registerClick;
    private TextView NFCgeneral;
    private TextView QRgeneral;
    private DBOpenHelper dbOpenHelper;
    Dialog dia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        dbOpenHelper=new DBOpenHelper(this);
        initViews();
        setupEvents();

    }

    private void initViews(){
        et_name= findViewById(R.id.et_account);
        et_password=findViewById(R.id.et_password);
        mLoginBtn=findViewById(R.id.btn_login);
        iv_see_password = findViewById(R.id.iv_see_password);
        registerClick= findViewById(R.id.text);
        NFCgeneral= findViewById(R.id.generalNFC);
        QRgeneral=findViewById(R.id.generalQR);
        QRLoginBtn=findViewById(R.id.button3);
    }
    private void setupEvents(){
        QRLoginBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        iv_see_password.setOnClickListener(this);
        registerClick.setOnClickListener(this);
        NFCgeneral.setOnClickListener(this);
        QRgeneral.setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login(); //登陆
                break;
            case R.id.iv_see_password:
                setPasswordVisibility();    //改变图片并设置输入框的文本可见或不可见
                break;
            case R.id.text:
                System.out.println("00000000000000");
                Intent intent=new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.generalQR: {
                try {
                    generalQR();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.button3: {
                Intent openCameraIntent = new Intent(MainActivity1.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            }
            case R.id.generalNFC:



        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            try {
                JSONObject object=new JSONObject(scanResult);
                if(dbOpenHelper.login(object.get("account").toString(),object.get("password").toString(),QRTYPE)){
                    showToast("登录成功！");
                    Intent intent=new Intent(this, RecordActivity.class);
                    intent.putExtra("username",object.get("account").toString());
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void login(){
        String account=getAccount();
        String password=getPassword();
        if (account.isEmpty()){
            showToast("你输入的账号为空！");
            return;
        }
        if (password.isEmpty()){
            showToast("你输入的密码为空！");
            return;
        }
        if(dbOpenHelper.login(account,password,NORMALTYPE)){
            showToast("登录成功！");
            Intent intent=new Intent(this, RecordActivity.class);
            intent.putExtra("username",account);
            startActivity(intent);
        }else{
            showToast("没有该用户！");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void generalQR() throws JSONException, WriterException {
        String account=getAccount();
        String password=getPassword();
        if (account.isEmpty()){
            showToast("你输入的账号为空！");
            return;
        }
        if (password.isEmpty()){
            showToast("你输入的密码为空！");
            return;
        }
        if(dbOpenHelper.testUser(account,password)){
            JSONObject object=new JSONObject();
            object.put("account",account);
            object.put("password",password);
            String jsonStr = object.toString();
            System.out.println(jsonStr);
            Bitmap qrCodeBitmap = EncodingHandler.createQRCode(jsonStr, 500);
            Context context = MainActivity1.this;
            dia = new Dialog(context, R.style.edit_AlertDialog_style);
            dia.setContentView(R.layout.imgdalog);
            ImageView imageView = (ImageView) dia.findViewById(R.id.ivdialog);
            imageView.setBackgroundResource(R.color.bg_color);
            //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
            dia.setCanceledOnTouchOutside(true); // Sets whether this dialog is
            Window w = dia.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0;
            lp.y = 40;
            dia.onWindowAttributesChanged(lp);
            imageView.setImageBitmap(qrCodeBitmap);
            imageView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dia.dismiss();
                        }
                    });
            dia.show();
        }else{
            showToast("用户或密码输入不正确！");
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity1.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}