package com.shinlooker.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shinlooker.smartcard.SmartCard.OperationCallBack;
import com.shinlooker.smartcard.SmartCard.ThreadPoolManager;
import com.shinlooker.smartcard.SmartCard.core.CardResult;
import com.shinlooker.smartcard.SmartCard.core.EnumReaderType;
import com.shinlooker.smartcard.SmartCard.core.SmartCard;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private Button btn_test;
    private TextView output_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn_test = findViewById(R.id.btn_test);
        output_text = findViewById(R.id.output_text);

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appletActive("00A404000DA000000809434343444B417631", cardResult -> {
                    runOnUiThread(() -> output_text.setText(cardResult.toString()));
                });
            }
        });
    }


    public static void appletActive(String apdu, OperationCallBack<CardResult> callBack) {
        List<String> apduList = new ArrayList<>();
        apduList.clear();
        apduList.add(apdu);
        ThreadPoolManager.getInstance().execute(() -> {
            CardResult cardResult = null;
            for (String capu : apduList) {
                //执行指令
                SmartCard.getInstance().setmReaderType(EnumReaderType.READER_TYPE_ESE);
                cardResult = SmartCard.getInstance().execute(capu);
                if (cardResult.getStatus() == -1) {
                    callBack.complete(cardResult);
                    SmartCard.getInstance().closeChannel();
                    return;
                }
            }
            callBack.complete(cardResult);
            //测试提交
//            SmartCard.getInstance().closeChannel();
        });
    }
}
