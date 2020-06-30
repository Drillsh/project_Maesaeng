package model;

import java.time.*;

public class Schedule {

	private String userID;
	private String roomName;
	private LocalDate scheduleDate;
	private int startTime;
	private int endTime;
	private int personNum = 1;
	
	public Schedule() {
		
	}

	public Schedule(String userID, LocalDate scheduleDate, int startTime, int endTime, String roomName, int personNum) {
		super();
		this.userID = userID;
		this.roomName = roomName;
		this.scheduleDate = scheduleDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.personNum = personNum;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public LocalDate getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(LocalDate scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getPersonNum() {
		return personNum;
	}

	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}
	
	
}
