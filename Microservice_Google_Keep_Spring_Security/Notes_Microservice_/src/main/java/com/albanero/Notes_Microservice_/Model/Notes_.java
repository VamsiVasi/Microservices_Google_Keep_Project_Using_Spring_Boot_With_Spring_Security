package com.albanero.Notes_Microservice_.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Notes_ {

	@Id
	private String id;
	private String name;
	private byte[] file;
	private String fileURL;

	public Notes_() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Notes_(String name, byte[] file, String fileURL) {
		super();
		this.name = name;
		this.file = file;
		this.fileURL = fileURL;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

}
