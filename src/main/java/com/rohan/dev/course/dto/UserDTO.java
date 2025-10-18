package com.rohan.dev.course.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserDTO {
	private long id;
	@NotEmpty(message = "{user.valid.fname}")
	private String firstName;
	@NotEmpty(message = "{user.valid.lname}")
	private String lastName;
	@Email(message = "{user.valid.email}")
	private String email;
	@NotEmpty(message = "{user.valid.pass}")
	private String address;
	private String phone;
	private String imgURL;
	private boolean enabled;
	private boolean isNotLocked;
	private boolean isUsingMFA;
	private LocalDateTime createdAt;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getImgURL() {
		return imgURL;
	}
	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isNotLocked() {
		return isNotLocked;
	}
	public void setNotLocked(boolean isNotLocked) {
		this.isNotLocked = isNotLocked;
	}
	public boolean isUsingMFA() {
		return isUsingMFA;
	}
	public void setUsingMFA(boolean isUsingMFA) {
		this.isUsingMFA = isUsingMFA;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
