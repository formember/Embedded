package com.qrcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.portAPi.SerialPort;

import java.io.File;
import java.io.IOException;

public class NFCActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        int nfcflag=0;
        try {
            SerialPort serialPort=new SerialPort(new File("/dev/ttyS2"),115200,0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}