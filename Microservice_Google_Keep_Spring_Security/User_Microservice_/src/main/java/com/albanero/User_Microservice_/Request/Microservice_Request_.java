package com.albanero.User_Microservice_.Request;

import com.albanero.User_Microservice_.Model.Users_;

public class Microservice_Request_ {

	private Users_ user;
	private Notes_ note;

	public Microservice_Request_() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Microservice_Request_(Users_ user, Notes_ note) {
		super();
		this.user = user;
		this.note = note;
	}

	public Users_ getUser() {
		return user;
	}

	public void setUser(Users_ user) {
		this.user = user;
	}

	public Notes_ getNote() {
		return note;
	}

	public void setNote(Notes_ note) {
		this.note = note;
	}

	
}
