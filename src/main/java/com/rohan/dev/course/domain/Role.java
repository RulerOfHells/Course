package com.rohan.dev.course.domain;

public class Role {
	private long roleID;
	private String name;
	private String perms;
	
	public long getRoleID() {
		return roleID;
	}
	public void setRoleID(long id) {
		this.roleID = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPerms() {
		return perms;
	}
	public void setPerms(String perms) {
		this.perms = perms;
	}
}
