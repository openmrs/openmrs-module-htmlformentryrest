package org.openmrs.module.htmlformentryrest;

public class HtmlFormShort {
	
	String name;
	
	Integer id;
	
	public HtmlFormShort(String name, Integer id) {
		this.id = id;
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
