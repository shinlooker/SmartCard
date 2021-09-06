package com.shinlooker.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.shinlooker.smartcard.R;
import com.shinlooker.smartcard.SmartCard.OperationCallBack;
import com.shinlooker.smartcard.SmartCard.ThreadPoolManager;
import com.shinlooker.smartcard.SmartCard.core.CardResult;
import com.shinlooker.smartcard.SmartCard.core.EnumReaderType;
import com.shinlooker.smartcard.SmartCard.core.SmartCard;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ThreadPoolManager.getInstance().execute(() -> {
            CardResult execute = SmartCard.getInstance().execute("");
        });
    }

    public void Personal(String string, OperationCallBack<CardResult> callBack){

    }
}