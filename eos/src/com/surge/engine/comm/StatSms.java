package com.surge.engine.comm;

public class StatSms {

	/**
	 * �յ���MO����
	 */
	private int moTotal=0;
	/**
	 * �·���MT����
	 */
	private int mtTotal=0;
	/**
	 * �ɹ�����
	 */
	private int sucssessSms=0;

	/**
	 *ʧ������ 
	 */
	private int failSms=0;
	/**
	 * �����쳣��
	 */
	private int errorMobleSms=0;
	/**
	 * ����ʧ��
	 */
	private int otherfail=0;
	/**
	 * 
	 */
	private int unknown=0;

	public int getMoTotal() {
		return moTotal;
	}

	public void setMoTotal(int moTotal) {
		this.moTotal += moTotal;
	}

	public int getMtTotal() {
		return mtTotal;
	}

	public void setMtTotal(int mtTotal) {
		this.mtTotal += mtTotal;
	}

	public int getSucssessSms() {
		return sucssessSms;
	}

	public void setSucssessSms(int sucssessSms) {
		this.sucssessSms += sucssessSms;
	}

	public int getFailSms() {
		return failSms;
	}

	public void setFailSms(int failSms) {
		this.failSms += failSms;
	}

	public int getErrorMobleSms() {
		return errorMobleSms;
	}

	public void setErrorMobleSms(int errorMobleSms) {
		this.errorMobleSms += errorMobleSms;
	}

	public int getOtherfail() {
		return otherfail;
	}

	public void setOtherfail(int otherfail) {
		this.otherfail += otherfail;
	}

	public int getUnknown() {
		return unknown;
	}

	public void setUnknown(int unknown) {
		this.unknown += unknown;
	}
}
