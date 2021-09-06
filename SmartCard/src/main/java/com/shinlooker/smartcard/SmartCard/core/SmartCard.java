package com.shinlooker.smartcard.SmartCard.core;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.shinlooker.smartcard.utils.Hex;
import com.shinlooker.smartcard.utils.PreconditionUtil;

import java.util.Objects;

/**
 * OpenModileAPI封装，已根据Android版本进行适配
 */
public final class SmartCard {
    private BaseSmartCard smartCardInterface;

    private static class SingletonHolder {
        private final static SmartCard instance = new SmartCard();
    }

    public static SmartCard getInstance() {
        return SingletonHolder.instance;
    }

    private SmartCard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            smartCardInterface = new SmartCardByGoogle();
        } else {
            smartCardInterface = new SmartCardBySimalliance();
        }
    }

    /**
     * 执行APDU指令
     * @param command 字符串指令数据
     */
    public CardResult execute(String command) {
        return smartCardInterface.execute(command);
    }

    /**
     * 执行APDU指令
     *
     * @param command 字节码数据
     */
    public CardResult execute(@NonNull byte[] command) {
        return execute(Objects.requireNonNull(Hex.bytesToHexString(command)));
    }

    /**
     * 执行APDU指令
     *
     * @param command 字符串指令数据
     * @param expSw   期望状态字
     */
    public CardResult execute(@NonNull String command, @NonNull String expSw) {
        PreconditionUtil.checkNotNull(expSw, "expSw must not be null");
        CardResult cardResult = execute(command);
        if (cardResult.getStatus() == 0) {
            try {
                cardResult.check(expSw);
            } catch (CardException e) {
                cardResult.setStatus(-2);
                cardResult.setMessage(e.getMessage());
            }
        }
        return cardResult;
    }

    /**
     * 打开通道
     */
    public CardResult openChannel(String command) {
        if (!TextUtils.isEmpty(command) && "00A404".equalsIgnoreCase(command.substring(0, 6))) {
            return smartCardInterface.execute(command);
        }
        return smartCardInterface.openChannel(command);
    }

    /**
     * 关闭SE Channel
     */
    public void closeChannel() {
        smartCardInterface.closeChannel();
    }


    /**
     * 关闭SE服务
     */
    public void closeService() {
        smartCardInterface.closeService();
    }

    /**
     * 获取Reader类型
     */
    public EnumReaderType getmReaderType() {
        return smartCardInterface.getmReaderType();
    }

    /**
     * 设置Reader类型
     */
    public void setmReaderType(EnumReaderType mReaderType) {
        smartCardInterface.setmReaderType(mReaderType);
    }
}
