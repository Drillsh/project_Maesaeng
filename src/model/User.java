package model;

public class User {
private String userid;
private String name;
private String password;
private String phone;
private String mail;
public User(String userid,  String password,String name, String phone, String mail) {
	super();
	this.userid = userid;
	this.name = name;
	this.password = password;
	this.phone = phone;
	this.mail = mail;
}
public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getMail() {
	return mail;
}
public void setMail(String mail) {
	this.mail = mail;
}




}
