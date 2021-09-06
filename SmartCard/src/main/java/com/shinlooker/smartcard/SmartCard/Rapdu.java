package com.shinlooker.smartcard.SmartCard;
/**
 * Created by zhangfuxiao on 2017/9/5.
 */
public class Rapdu {
	/**
	 * 执行CAPDU对应的索引号
	 */
	private int index;
	/**
	 * 执行返回状态字
	 */
	private String rapdu;
	/**
	 * SE响应数据
	 */
	private String sw;

	public Rapdu(){
		
	}
	
	public Rapdu(int index, String rapdu, String sw) {
		this.index = index;
		this.rapdu = rapdu;
		this.sw = sw;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getRapdu() {
		return rapdu;
	}

	public void setRapdu(String rapdu) {
		this.rapdu = rapdu;
	}

	public String getSw() {
		return sw;
	}

	public void setSw(String sw) {
		this.sw = sw;
	}

    @Override
    public String toString() {
        return "Rapdu{" +
                "index=" + index +
                ", rapdu='" + rapdu + '\'' +
                ", sw='" + sw + '\'' +
                '}';
    }
}
