package com.shinlooker.smartcard.SmartCard.core;

import com.shinlooker.smartcard.utils.LogUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardResult {

    private int status;
    private String sw;
    private String rapdu;
    private String apdu;
    private String message;

    public CardResult() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

    public String getRapdu() {
        return rapdu;
    }

    public void setRapdu(String rapdu) {
        this.rapdu = rapdu;
    }

    public String getApdu() {
        return apdu;
    }

    public void setApdu(String apdu) {
        this.apdu = apdu;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CardResult(int status, String message, String rapdu) {
        this.status = status;
        this.sw = rapdu.substring(rapdu.length() - 4);
        this.rapdu = rapdu.substring(0, rapdu.length() - 4);
        this.apdu = rapdu;
        this.message = message;
    }

    public CardResult(int status, String message) {
        super();
        this.status = status;
        this.message = message;
    }


    public void check(String expSw) throws CardException {
        LogUtil.e("expSw:" + expSw);
        LogUtil.e("sw:" + this.sw);
        if (!isMatchSw(expSw, this.sw)) {
            throw new CardException(Integer.parseInt(this.sw, 16));
        }
    }

    private boolean isMatchSw(String expSw, String sw) {
        Pattern p = Pattern.compile(expSw);
        Matcher m = p.matcher(sw);
        return m.find();
    }

}
