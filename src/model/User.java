package model;

public class User {

	private String userID;
	private String uPassword;
	private String uName;
	private String uPhone;
	private String uEmail;
	
	
	public User(String userID, String uPassword, String uName, String uPhone, String uEmail) {
		this.userID = userID;
		this.uPassword = uPassword;
		this.uName = uName;
		this.uPhone = uPhone;
		this.uEmail = uEmail;
	}


	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
	}


	public String getuPassword() {
		return uPassword;
	}


	public void setuPassword(String uPassword) {
		this.uPassword = uPassword;
	}


	public String getuName() {
		return uName;
	}


	public void setuName(String uName) {
		this.uName = uName;
	}


	public String getuPhone() {
		return uPhone;
	}


	public void setuPhone(String uPhone) {
		this.uPhone = uPhone;
	}


	public String getuEmail() {
		return uEmail;
	}


	public void setuEmail(String uEmail) {
		this.uEmail = uEmail;
	}
	
	
}
