package com.shinlooker.smartcard.SmartCard;

import java.util.List;


public class Result {
    private int status;
    private String iccid;
    private String rapdu;
    private String resultDesc;
    private String taskId;
    private List<String> accessAppletAidList;
    private int spPkExponent;
    private String spPkModulu;
    private String transactionId;
    private String callBackTag;


    public Result(int status, String resultDesc) {
        this.status = status;
        this.resultDesc = resultDesc;
    }

    public Result(int status, String iccid, String resultDesc) {
        this.status = status;
        this.iccid = iccid;
        this.resultDesc = resultDesc;
    }


    public String getCallBackTag() {
        return callBackTag;
    }

    public void setCallBackTag(String callBackTag) {
        this.callBackTag = callBackTag;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public List<String> getAccessAppletAidList() {
        return accessAppletAidList;
    }

    public void setAccessAppletAidList(List<String> accessAppletAidList) {
        this.accessAppletAidList = accessAppletAidList;
    }

    public int getSpPkExponent() {
        return spPkExponent;
    }

    public void setSpPkExponent(int spPkExponent) {
        this.spPkExponent = spPkExponent;
    }

    public String getSpPkModulu() {
        return spPkModulu;
    }

    public void setSpPkModulu(String spPkModulu) {
        this.spPkModulu = spPkModulu;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getRapdu() {
        return rapdu;
    }

    public void setRapdu(String rapdu) {
        this.rapdu = rapdu;
    }


    @Override
    public String toString() {
        return "Result{" +
                "resultDesc='" + resultDesc + '\'' +
                ", status='" + status + '\'' +
                ", taskId='" + taskId + '\'' +
                ", accessAppletAidList=" + accessAppletAidList +
                ", spPkExponent=" + spPkExponent +
                ", spPkModulu='" + spPkModulu + '\'' +
                ", iccid='" + iccid + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", rapdu='" + rapdu + '\'' +
                ", callBackTag='" + callBackTag + '\'' +
                '}';
    }
}
