package model;

public class Room {

	private String roomName;
	private int size;
	private boolean isWB;
	private boolean isTV;
	private int price;
	
	public Room() {}
	
	public Room(String roomName, int size, int price, boolean isWB, boolean isTV) {
		this.roomName = roomName;
		this.size = size;
		this.isWB = isWB;
		this.isTV = isTV;
		this.price = price;
	}
	
	
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public boolean isWB() {
		return isWB;
	}
	public void setWB(boolean isWB) {
		this.isWB = isWB;
	}
	public boolean isTV() {
		return isTV;
	}
	public void setTV(boolean isTV) {
		this.isTV = isTV;
	}
	
}
