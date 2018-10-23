package com.cloudclass.entity;

public class LessonTypeInfo {

	public String code = "";
	public String name = "";
	public boolean isCheck;

	public LessonTypeInfo() {
		// TODO Auto-generated constructor stub
	}

	public LessonTypeInfo(String code, String name, boolean isCheck) {
		this.code = code;
		this.name = name;
		this.isCheck=isCheck; 
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "LessonTypeInfo [code=" + code + ", name=" + name + "]";
	}

}
