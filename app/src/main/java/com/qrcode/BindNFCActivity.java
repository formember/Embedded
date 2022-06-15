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

public class BindNFCActivity extends AppCompatActivity implements OnClickListener {

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
        setContentView(R.layout.activity_bind_nfc);
        dbOpenHelper=new DBOpenHelper(this);
        nfclogin = findViewById(R.id.nfclogin);
        nfclogin.setOnClickListener(this);
        final String username= getIntent().getStringExtra("username");

        handler= new Handler(){
            @SuppressLint("HandlerLeak")
            public void handleMessage(Message msg) {
                if (dbOpenHelper.bind_nfd(username, (String)msg.obj)) {
                    Intent intent = new Intent(BindNFCActivity.this, AllUserActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
            }
        };
        new Thread(new Runnable(){
            public void run() {
                System.out.println("00000000000000");
                clientSocketThread = ClientSocketThread.getClientSocket(clientSocketTools.getLocalIpAddress(), 6109);
                clientSocketThread.setListener(new MessageListener() {
                    public void Message(byte[] message, int message_len) {
                        //System.arraycopy(message, 1, data, 0, 4);
                        System.out.println("111111111111"+username);
                        System.out.println(clientSocketTools.byte2hex(message, message_len));
                        handler.sendMessage(handler.obtainMessage(1, "\n" + clientSocketTools.byte2hex(message, message_len)));
                    }
                });
            }
        }).start();

    }

    public void onClick(View v){
        if (v.getId() == R.id.nfclogin) {
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


}