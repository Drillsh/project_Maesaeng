package model;

import java.time.LocalDate;

public class JoinRevList {

	private String jName;
	private String jUserID;
	private String jRoomName;
	private LocalDate jDate;
	private int jStartTime;
	private int jEndTime;
	private int jPersonNum;
	private String jPhone;
	public JoinRevList(String jName, String jUserID, String jRoomName, LocalDate jDate, int jStartTime, int jEndTime,
			int jPersonNum, String jPhone) {
		super();
		this.jName = jName;
		this.jUserID = jUserID;
		this.jRoomName = jRoomName;
		this.jDate = jDate;
		this.jStartTime = jStartTime;
		this.jEndTime = jEndTime;
		this.jPersonNum = jPersonNum;
		this.jPhone = jPhone;
	}
	public String getjName() {
		return jName;
	}
	public void setjName(String jName) {
		this.jName = jName;
	}
	public String getjUserID() {
		return jUserID;
	}
	public void setjUserID(String jUserID) {
		this.jUserID = jUserID;
	}
	public String getjRoomName() {
		return jRoomName;
	}
	public void setjRoomName(String jRoomName) {
		this.jRoomName = jRoomName;
	}
	public LocalDate getjDate() {
		return jDate;
	}
	public void setjDate(LocalDate jDate) {
		this.jDate = jDate;
	}
	public int getjStartTime() {
		return jStartTime;
	}
	public void setjStartTime(int jStartTime) {
		this.jStartTime = jStartTime;
	}
	public int getjEndTime() {
		return jEndTime;
	}
	public void setjEndTime(int jEndTime) {
		this.jEndTime = jEndTime;
	}
	public int getjPersonNum() {
		return jPersonNum;
	}
	public void setjPersonNum(int jPersonNum) {
		this.jPersonNum = jPersonNum;
	}
	public String getjPhone() {
		return jPhone;
	}
	public void setjPhone(String jPhone) {
		this.jPhone = jPhone;
	}

}
