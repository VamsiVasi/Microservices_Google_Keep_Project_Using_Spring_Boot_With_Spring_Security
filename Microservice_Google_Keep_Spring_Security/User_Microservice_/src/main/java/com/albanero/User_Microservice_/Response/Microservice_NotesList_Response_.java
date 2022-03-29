package com.albanero.User_Microservice_.Response;

import java.util.HashMap;

public class Microservice_NotesList_Response_ {

	private HashMap<String, String> note;
	private String Status;

	public Microservice_NotesList_Response_() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Microservice_NotesList_Response_(HashMap<String, String> note, String status) {
		super();
		this.note = note;
		Status = status;
	}

	public HashMap<String, String> getNote() {
		return note;
	}

	public void setNote(HashMap<String, String> note) {
		this.note = note;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	
}
