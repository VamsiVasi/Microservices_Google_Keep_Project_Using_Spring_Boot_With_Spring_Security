package com.albanero.User_Microservice_.Model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Users_ {

	@Id
	private String id;
	private String userName;
	private String email;
	private String password;
	private String role;
	private ArrayList<String> notesIds;

	public Users_() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Users_(String userName, String email, String password, String role, ArrayList<String> notesIds) {
		super();
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.role = role;
		this.notesIds = notesIds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public ArrayList<String> getNotesIds() {
		return notesIds;
	}

	public void setNotesIds(ArrayList<String> notesIds) {
		this.notesIds = notesIds;
	}

}
