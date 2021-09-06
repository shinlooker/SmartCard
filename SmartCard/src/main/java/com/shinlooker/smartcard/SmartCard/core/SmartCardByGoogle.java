package com.shinlooker.smartcard.SmartCard.core;

import android.se.omapi.Channel;
import android.se.omapi.Reader;
import android.se.omapi.SEService;
import android.se.omapi.Session;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.shinlooker.smartcard.utils.Hex;
import com.shinlooker.smartcard.utils.LogUtil;
import com.shinlooker.smartcard.utils.Utils;

import java.util.concurrent.Executor;

/**
 * Android P以后需要调用Google的api实现
 */
@RequiresApi(api = 28)
final class SmartCardByGoogle extends BaseSmartCard implements SEService.OnConnectedListener {
    private Object mLock = new Object();
    private SEService mSEService;
    private boolean mServiceIsConnection = false;
    private Channel mChannel;
    private Session mSession;
    private final String TAG = SmartCardByGoogle.class.getSimpleName();

    /**
     * 绑定OMA服务
     *
     * @throws InterruptedException
     */
    private void bindService() throws InterruptedException {
        // 判断SEService是否已经连接
        if (mSEService == null) {
            mSEService = new SEService(Utils.getApp().getApplicationContext(), new OMAExecutor(), this);
            LogUtil.e(TAG, "start bind SEService");
            if (!mServiceIsConnection) {
                synchronized (mLock) {
                    if (!mServiceIsConnection) {
                        LogUtil.d("thread is waiting");
                        mLock.wait();
                    }
                }
            }
        }
    }

    /**
     * 执行APDU指令
     *
     * @param command APDU指令
     */
    @Override
    public CardResult execute(String command) {
        try {
            bindService();
            return executeApduCmd(command);
        } catch (InterruptedException e) {
            return operFail("thread error:" + e.toString());
        } catch (Exception e) {
            return operFail("error:" + e.toString());
        }
    }

    @Override
    public CardResult openChannel(String command) {
        try {
            bindService();
            if (TextUtils.isEmpty(command) || command.length() < 6) {
                return new CardResult(STATUS_CODE_FAIL, "Command is null or length is not enough");
            }
            LogUtil.e(TAG, "Command APDU:" + command);
            closeChannelAndSession();
            // 打开通道
            Object[] openResult = openCurrentAvailableChannel(command);
            int resultCode = (int) openResult[0];
            String resultDesc = (String) openResult[1];
            if (resultCode == STATUS_CODE_SUCCESS) {
                String response = Hex.bytesToHexString(mChannel.getSelectResponse());
                LogUtil.e(TAG, "Response APDU：" + response);
                return new CardResult(STATUS_CODE_SUCCESS, resultDesc, response);
            }
            LogUtil.e(TAG, "OpenChannel Error Desc:" + resultDesc);
            return new CardResult(STATUS_CODE_FAIL, resultDesc);
        } catch (InterruptedException e) {
            return operFail("thread error:" + e.toString());
        } catch (Exception e) {
            return operFail("error:" + e.toString());
        }
    }


    /**
     * 关闭Channel
     */
    @Override
    public void closeChannel() {
        closeChannelAndSession();
    }

    /**
     * 关闭SEService
     */
    @Override
    public void closeService() {
        try {
            closeChannel();
            if (mSEService != null && mSEService.isConnected()) {
                mSEService.shutdown();
                mSEService = null;
                mServiceIsConnection = false;
                LogUtil.i(TAG, "SEService shutdown success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "SEService shutdown error:" + e.getMessage());
        }
    }

    @Override
    public void onConnected() {
        LogUtil.i(TAG, "service is connected");
        synchronized (mLock) {
            mServiceIsConnection = true;
            mLock.notifyAll();
        }
    }

    public class OMAExecutor implements Executor {
        @Override
        public void execute(@NonNull Runnable command) {
            command.run();
        }
    }

    /**
     * 执行APDU指令
     *
     * @param mReuqestCommand APDU指令
     * @return CardResult 执行结果
     * @throws Exception
     */
    private synchronized CardResult executeApduCmd(String mReuqestCommand) throws Exception {
        if (TextUtils.isEmpty(mReuqestCommand) || mReuqestCommand.length() < 6) {
            return new CardResult(STATUS_CODE_FAIL, "Command is null or length is not enough");
        }

        LogUtil.e(TAG, "Command APDU:" + mReuqestCommand);

        /**
         * 判断以00A404开头需开通道
         */
        if ("00A404".equalsIgnoreCase(mReuqestCommand.substring(0, 6))) {
            closeChannelAndSession();
            // 获取AID
            int totallength = Integer.parseInt(mReuqestCommand.substring(8, 10), 16);
            String aid = mReuqestCommand.substring(10, 10 + totallength * 2);
            // 打开通道
            Object[] openResult = openCurrentAvailableChannel(aid);
            int resultCode = (int) openResult[0];
            String resultDesc = (String) openResult[1];
            if (resultCode == STATUS_CODE_SUCCESS) {
                String rapdu = Hex.bytesToHexString(mChannel.getSelectResponse());
                LogUtil.e(TAG, "Response APDU：" + rapdu);
                return new CardResult(STATUS_CODE_SUCCESS, resultDesc, rapdu);
            }

            LogUtil.e(TAG, "OpenChannel Error Desc:" + resultDesc);
            return new CardResult(resultCode, resultDesc);
        }

        byte[] byteCommand = Hex.hexStringToBytes(mReuqestCommand);

        if (mChannel != null) {
            byte[] byteRapdu = mChannel.transmit(byteCommand);
            String response = Hex.bytesToHexString(byteRapdu);
            LogUtil.i(TAG, "Response APDU：" + response);
            return new CardResult(STATUS_CODE_SUCCESS, "transmit apdu success", response);
        } else {
            return new CardResult(STATUS_CODE_FAIL, "Channal is not open");
        }
    }

    /**
     * 打开当前选择的通道
     */
    private Object[] openCurrentAvailableChannel(String aid) throws Exception {
        Reader reader = getCurrentAvailableReader();

        if (reader == null) {
            return new Object[]{STATUS_CODE_FAIL, "selected reader not exist"};
        }

        if (!reader.isSecureElementPresent()) {
            return new Object[]{STATUS_CODE_FAIL, "selected reader can not use"};
        }

        mSession = reader.openSession();
        byte[] byteAid = Hex.hexStringToBytes(aid);
        LogUtil.i(TAG, "open channel applet：" + aid);
        if (mSession != null) {
            mChannel = mSession.openLogicalChannel(byteAid);
        }

        if (mChannel == null) {
            return new Object[]{STATUS_CODE_FAIL, "channel is null"};
        }

        return new Object[]{0, "open channel success"};
    }

    /**
     * 获取当前选择的通道的Reader对象
     *
     * @return
     */
    private Reader getCurrentAvailableReader() {
        LogUtil.e(TAG, "select reader name:" + getmReaderType().getValue());
        Reader[] readers = mSEService.getReaders();

        if (readers.length < 1) {
            LogUtil.e(TAG, "There is no avaliable reader");
            return null;
        }

        for (Reader reader : readers) {
            LogUtil.e(TAG, "avaliable reader name:" + reader.getName());
            if (reader.getName().startsWith(getmReaderType().getValue())) {
                return reader;
            }
        }
        return null;
    }

    /**
     * 关闭当前打开的Channel和Session
     */
    public void closeChannelAndSession() {
        try {
            if (mChannel != null && mChannel.isOpen()) {
                mChannel.close();
                mChannel = null;
                LogUtil.i(TAG, "channel close success");
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "channel close error:" + e.getMessage());
        }

        try {
            if (mSession != null && !mSession.isClosed()) {
                mSession.close();
                mSession = null;
                LogUtil.d(TAG, "session close success");
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "session close error:" + e.getMessage());
        }
    }
}
