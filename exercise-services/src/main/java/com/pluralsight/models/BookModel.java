package com.pluralsight.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "book")
public class BookModel {
	
	private String id;
	private String title;
	private String author;
	private String genre;
	private Boolean read;
	
	public String get_id() {
		return id;
	}
	public void set_id(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public Boolean getRead() {
		return read;
	}
	public void setRead(Boolean read) {
		this.read = read;
	}
}
