package com.rohan.dev.course.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class User {
	private long id;
	@NotEmpty(message = "{user.valid.fname}")
	private String firstName;
	@NotEmpty(message = "{user.valid.lname}")
	private String lastName;
	@Email(message = "{user.valid.email}")
	private String email;
	@NotEmpty(message = "{user.valid.pass}")
	private String password;
	private String address;
	private String phone;
	private String imgURL;
	private boolean enabled;
	private boolean isNotLocked;
	private boolean isUsingMFA;
	public long getId() {
		return id;
	}
	public User setId(long id) {
		this.id = id;
		return this;
	}
	public String getFirstName() {
		return firstName;
	}
	public User setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}
	public String getLastName() {
		return lastName;
	}
	public User setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
	public String getEmail() {
		return email;
	}
	public User setEmail(String email) {
		this.email = email;
		return this;
	}
	public String getPassword() {
		return password;
	}
	public User setPassword(String password) {
		this.password = password;
		return this;
	}
	public String getAddress() {
		return address;
	}
	public User setAddress(String address) {
		this.address = address;
		return this;
	}
	public String getPhone() {
		return phone;
	}
	public User setPhone(String phone) {
		this.phone = phone;
		return this;
	}
	public String getImgURL() {
		return imgURL;
	}
	public User setImgURL(String imgURL) {
		this.imgURL = imgURL;
		return this;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public User setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}
	public boolean isNotLocked() {
		return isNotLocked;
	}
	public User setNotLocked(boolean isNotLocked) {
		this.isNotLocked = isNotLocked;
		return this;
	}
	public boolean isUsingMFA() {
		return isUsingMFA;
	}
	public User setUsingMFA(boolean isUsingMFA) {
		this.isUsingMFA = isUsingMFA;
		return this;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", address=" + address + ", phone=" + phone + ", imgURL=" + imgURL
				+ ", enabled=" + enabled + ", isNotLocked=" + isNotLocked + ", isUsingMFA=" + isUsingMFA
				+ "]";
	}
}
