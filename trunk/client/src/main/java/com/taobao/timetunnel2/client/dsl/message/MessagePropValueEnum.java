package com.taobao.timetunnel2.client.dsl.message;

public enum MessagePropValueEnum {
	YES("y"), NO("n");

	private String token;

	private MessagePropValueEnum(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
