package com.shinlooker.smartcard.SmartCard.core;


public abstract class BaseSmartCard {
    private EnumReaderType mReaderType;
    final int STATUS_CODE_SUCCESS = 0;
    final int STATUS_CODE_FAIL = -1;

    public abstract CardResult execute(String command);

    public abstract CardResult openChannel(String command);

    public abstract void closeChannel();

    public abstract void closeService();

    CardResult operFail(String errorMsg) {
        closeChannel();
        return new CardResult(STATUS_CODE_FAIL, errorMsg);
    }

    public EnumReaderType getmReaderType() {
        if (mReaderType == null) {
            return EnumReaderType.READER_TYPE_SIM;
        }
        return mReaderType;
    }

    public void setmReaderType(EnumReaderType mReaderType) {
        this.mReaderType = mReaderType;
    }

}
