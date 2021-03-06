package com.qrcode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
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
import com.socket.ClientSocketThread;
import com.socket.clientSocketTools;
import com.socket.MessageListener;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity1 extends AppCompatActivity
     implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    ClientSocketThread clientSocketThread;
    byte[] buffer={(byte) 0xFE, (byte) 0xE0, 0x08, 0x00, 0x72, 0x00, 0x02, 0x0A};
    public static int NORMALTYPE=1;
    public static int NFCTYPE=2;
    public static int QRTYPE=3;
    private EditText et_name;
    private EditText et_password;
    private Button mLoginBtn;
    private Button QRLoginBtn;
    private Button NFCLoginBtn;
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
        NFCLoginBtn=findViewById(R.id.NFCbutton);
    }
    private void setupEvents(){
        QRLoginBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        iv_see_password.setOnClickListener(this);
        registerClick.setOnClickListener(this);
        NFCgeneral.setOnClickListener(this);
        QRgeneral.setOnClickListener(this);
        NFCLoginBtn.setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login(); //??????
                break;
            case R.id.iv_see_password:
                setPasswordVisibility();    //?????????????????????????????????????????????????????????
                break;
            case R.id.text:

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
            case R.id.generalNFC:{
                Intent intent1 = new Intent(MainActivity1.this, AllUserActivity.class);
                startActivity(intent1);
                break;
            }
            case R.id.NFCbutton:{
                NFClogin();
                break;
            }

        }
    }

    private void NFClogin(){
        Intent intent=new Intent(this, NFCActivity.class);
        startActivity(intent);
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
                    final byte[] blights = {(byte) 0x21, (byte) 0x12, (byte) 0x14};
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            clientSocketThread=ClientSocketThread.getClientSocket(clientSocketTools.getLocalIpAddress(),6109);
                            // ???????????????????????????
                            buffer[3] = 0x32;
                            buffer[6] = 0x02;
                            try {
                                clientSocketThread.getOutputStream().write(buffer);  // ???buffer?????????????????????????????????????????????
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // ?????????????????????
                            buffer[3] = 0x44;
                            for (int i = 0; i < 6; ++i) {
                                for (int j = 0; j < 3; ++j) {
                                    buffer[6] = blights[j];
                                    try {
                                        clientSocketThread.getOutputStream().write(buffer);  // ???buffer?????????????????????????????????????????????
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        Thread.sleep(250);
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            // ??????????????????
                            buffer[3] = 0x32;
                            buffer[6] = 0x01;
                            try {
                                clientSocketThread.getOutputStream().write(buffer);  // ???buffer?????????????????????????????????????????????
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // ???????????????
                            buffer[3] = 0x44;
                            buffer[6] = 0x00;
                            try {
                                clientSocketThread.getOutputStream().write(buffer);  // ???buffer?????????????????????????????????????????????
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    showToast("???????????????");
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
            //???????????????
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password.setSelected(true);
            //????????????
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void login(){
        String account=getAccount();
        String password=getPassword();
        if (account.isEmpty()){
            showToast("???????????????????????????");
            return;
        }
        if (password.isEmpty()){
            showToast("???????????????????????????");
            return;
        }
        if(dbOpenHelper.login(account,password,NORMALTYPE)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // ???????????????
                    try {
                        Runtime.getRuntime().exec("ioctl -d /dev/ledtest 0 4");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // ????????????LED????????????
                    String execOpen = "ioctl -d /dev/ledtest 1 ";
                    String execClose = "ioctl -d /dev/ledtest 0 ";
                    int t;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 4; ++j) {
                            t=j;
                            if (i % 2 != 0)
                                t = 3 - j;
                            // ??????LED???
                            try {
                                Runtime.getRuntime().exec(execOpen + Integer.toString(t));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // ????????????
                            try {
                                Thread.sleep(50);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // ??????LED???
                            try {
                                Runtime.getRuntime().exec(execClose + Integer.toString(t));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // ????????????
                            try {
                                Thread.sleep(300);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // ???????????????
                    try {
                        Runtime.getRuntime().exec("ioctl -d /dev/ledtest 1 4");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            showToast("???????????????");
            Intent intent=new Intent(this, RecordActivity.class);
            intent.putExtra("username",account);
            startActivity(intent);
        }else{
            showToast("??????????????????");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void generalQR() throws JSONException, WriterException {
        String account=getAccount();
        String password=getPassword();
        if (account.isEmpty()){
            showToast("???????????????????????????");
            return;
        }
        if (password.isEmpty()){
            showToast("???????????????????????????");
            return;
        }
        if(dbOpenHelper.testUser(account,password)){
            JSONObject object=new JSONObject();
            object.put("account",account);
            object.put("password",password);
            String jsonStr = object.toString();
//            System.out.println(jsonStr);
            Bitmap qrCodeBitmap = EncodingHandler.createQRCode(jsonStr, 500);
            Context context = MainActivity1.this;
            dia = new Dialog(context, R.style.edit_AlertDialog_style);
            dia.setContentView(R.layout.imgdalog);
            ImageView imageView = (ImageView) dia.findViewById(R.id.ivdialog);
            imageView.setBackgroundResource(R.color.bg_color);
            //??????true?????????????????????????????????dialog????????????false??????????????????
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
            showToast("?????????????????????????????????");
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