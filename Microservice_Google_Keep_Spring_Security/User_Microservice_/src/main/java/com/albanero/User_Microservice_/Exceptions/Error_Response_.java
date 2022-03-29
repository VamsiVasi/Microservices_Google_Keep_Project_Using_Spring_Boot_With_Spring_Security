package com.albanero.User_Microservice_.Exceptions;

public class Error_Response_ {
	
	private String message;
	private String status;
	public Error_Response_(String message, String status) {
		super();
		this.message = message;
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public String getStatus() {
		return status;
	}

	
}