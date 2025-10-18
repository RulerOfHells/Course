package com.rohan.dev.course.domain;

import java.util.Map;

import org.springframework.http.HttpStatus;

public class APIResponse {
	private String timeStamp;
	private int statusCode;
	private HttpStatus status;
	private String error;
	private String message;
	private String devMsg;
	private Map<?, ?> data;
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public APIResponse setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
		return this;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public APIResponse setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		return this;
	}
	public HttpStatus getStatus() {
		return status;
	}
	public APIResponse setStatus(HttpStatus status) {
		this.status = status;
		return this;
	}
	public String getError() {
		return error;
	}
	public APIResponse setError(String error) {
		this.error = error;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public APIResponse setMessage(String message) {
		this.message = message;
		return this;
	}
	public String getDevMsg() {
		return devMsg;
	}
	public APIResponse setDevMsg(String devMsg) {
		this.devMsg = devMsg;
		return this;
	}
	public Map<?, ?> getData() {
		return data;
	}
	public APIResponse setData(Map<?, ?> data) {
		this.data = data;
		return this;
	}
	@Override
	public String toString() {
		return "APIResponse [timeStamp=" + timeStamp + ", statusCode=" + statusCode + ", status=" + status + ", error="
				+ error + ", message=" + message + ", devMsg=" + devMsg + ", data=" + data + "]";
	}
}
