package com.shinlooker.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shinlooker.smartcard.SmartCard.OperationCallBack;
import com.shinlooker.smartcard.SmartCard.ThreadPoolManager;
import com.shinlooker.smartcard.SmartCard.core.CardResult;
import com.shinlooker.smartcard.SmartCard.core.SmartCard;
import com.shinlooker.smartcard.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText input_edittext;
    private Button todo_btn;
    private Button close_btn;
    private TextView jieguo_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input_edittext = findViewById(R.id.input_edittext);
        todo_btn = findViewById(R.id.todo_btn);
        jieguo_text = findViewById(R.id.jieguo_text);
        close_btn = findViewById(R.id.close_btn);
        initView();
    }

    private void initView() {
        todo_btn.setOnClickListener(v -> {
            String trim = input_edittext.getText().toString().trim();
            appletActive(trim, cardResult -> {
                runOnUiThread(() -> jieguo_text.setText(cardResult.toString()));
            });
        });
        close_btn.setOnClickListener(v -> {
            SmartCard.getInstance().closeChannel();
        });
    }

    public static void appletActive(String apdu, OperationCallBack<CardResult> callBack) {
//        String[] defaultCapu = {"00A4040009A00000015143525300", "80F001010A4F085943542E55534552"};
        List<String> apduList = new ArrayList<>();
        apduList.clear();
        apduList.add(apdu);
        ThreadPoolManager.getInstance().execute(() -> {
            CardResult cardResult = null;
            for (String capu : apduList) {
                //执行指令
                cardResult = SmartCard.getInstance().execute(capu);
                if (cardResult.getStatus() == -1) {
                    callBack.complete(cardResult);
                    SmartCard.getInstance().closeChannel();
                    return;
                }
            }
            callBack.complete(cardResult);
//            SmartCard.getInstance().closeChannel();
        });
    }
}