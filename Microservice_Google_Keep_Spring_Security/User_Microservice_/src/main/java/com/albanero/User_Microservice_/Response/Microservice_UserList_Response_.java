package com.albanero.User_Microservice_.Response;

import java.util.List;

public class Microservice_UserList_Response_ {

	private List<String> users;
	private String Status;
	
	public Microservice_UserList_Response_() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Microservice_UserList_Response_(List<String> users, String status) {
		super();
		this.users = users;
		Status = status;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	
}
