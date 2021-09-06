package com.shinlooker.smartcard.SmartCard;



import java.util.List;

/**
 * @ClassName: AppletPersonalResult
 * @Description: java类作用描述
 * @Author: shinlooker
 * @Date: 2021/8/30 13:22
 */
public class AppletPersonalResult {

    private int status;
    private List<Rapdu> rapduList;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Rapdu> getRapduList() {
        return rapduList;
    }

    public void setRapduList(List<Rapdu> rapduList) {
        this.rapduList = rapduList;
    }
}
