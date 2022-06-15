package com.qrcode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.database.DBOpenHelper;
import com.socket.ClientSocketThread;
import com.socket.MessageListener;
import com.socket.clientSocketTools;

import java.util.concurrent.BlockingDeque;

public class NFCActivity extends AppCompatActivity implements OnClickListener {

    Handler handler;
    private DBOpenHelper dbOpenHelper;
    private Context context;
    ClientSocketThread clientSocketThread;
    byte[] buffer={(byte) 0xFE, (byte) 0xE0, 0x08, 0x00, 0x00, 0x00, 0x02, 0x0A};
    private Button nfclogin;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        dbOpenHelper=new DBOpenHelper(this);
        nfclogin = findViewById(R.id.nfclogin1);
        nfclogin.setOnClickListener(this);
        final String username= getIntent().getStringExtra("username");
        final Boolean[] is = {false};
        handler= new Handler(){
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message msg) {
                if(!is[0]){
                    is[0] =true;
                    Pair<String, Boolean> resultPair = dbOpenHelper.login_by_nfc((String)msg.obj);
                    if (resultPair.second) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 打开点阵
                                buffer[3] = 0x12;
                                buffer[4] = 0x72;
                                buffer[6] = 0x00;
                                try {
                                    clientSocketThread.getOutputStream().write(buffer);  // 把buffer写到输出流中，从而传递给服务器
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // buffer[3] = 0x24;
                                // 打开数码管
                                buffer[3] = 0x22;
                                buffer[4] = 0x72;
                                buffer[6] = 0x12;
                                try {
                                    clientSocketThread.getOutputStream().write(buffer);  // 把buffer写到输出流中，从而传递给服务器
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // 持续一段时间
                                try {
                                    Thread.sleep(6000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // 关闭点阵
                                buffer[3] = 0x13;
                                buffer[4] = 0x72;
                                buffer[6] = 0x00;
                                try {
                                    clientSocketThread.getOutputStream().write(buffer);  // 把buffer写到输出流中，从而传递给服务器
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // 关闭数码管
                                buffer[3] = 0x23;
                                buffer[4] = 0x72;
                                buffer[6] = 0x12;
                                try {
                                    clientSocketThread.getOutputStream().write(buffer);  // 把buffer写到输出流中，从而传递给服务器
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        showToast("登录成功！");
                        Intent intent = new Intent(NFCActivity.this, RecordActivity.class);
                        intent.putExtra("username", resultPair.first);
                        startActivity(intent);
                        finish();
                    } else {
                        showToast("没有该用户！");
                        finish();
                    }
                }

            }
        };
        new Thread(new Runnable(){
            public void run() {
                clientSocketThread = ClientSocketThread.getClientSocket(clientSocketTools.getLocalIpAddress(), 6109);
                clientSocketThread.setListener(new MessageListener() {
                    public void Message(byte[] message, int message_len) {
                        //System.arraycopy(message, 1, data, 0, 4);
                        System.out.println(clientSocketTools.byte2hex(message, message_len));
                        handler.sendMessage(handler.obtainMessage(1, "\n" + clientSocketTools.byte2hex(message, message_len)));
                    }
                });
            }
        }).start();

    }

    public void onClick(View v){
        if (v.getId() == R.id.nfclogin1) {
            try {
                buffer[3] = 0x55;
                buffer[4] = 0x72;
                buffer[6] = 0x02;
                clientSocketThread.getOutputStream().write(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NFCActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}