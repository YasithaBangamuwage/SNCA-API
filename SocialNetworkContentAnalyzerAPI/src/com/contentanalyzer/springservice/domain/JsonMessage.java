package com.contentanalyzer.springservice.domain;

public class JsonMessage {
	private int id;
	private String msg;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "JsonMessage [id=" + id + ", msg=" + msg + "]";
	}
}
