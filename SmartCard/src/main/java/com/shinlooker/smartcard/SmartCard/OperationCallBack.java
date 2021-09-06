package com.shinlooker.smartcard.SmartCard;


/**
 * 操作回调
 */
public interface OperationCallBack<T> {

    void complete(T t);
}