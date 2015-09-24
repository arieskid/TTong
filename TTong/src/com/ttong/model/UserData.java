package com.ttong.model;

public class UserData {

	private String userName;
	private String userPhone;
	private int userState;
	
	public UserData(String un, String up, int us){
		userName = un;
		userPhone = up;
		userState = us;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public int getUserState() {
		return userState;
	}
	public void setUserState(int userState) {
		this.userState = userState;
	}
}
