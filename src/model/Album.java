package model;

public class Album {

	private int albumNo;
	private String photo;
	
	public Album(int albumNo, String photo) {
		this.albumNo = albumNo;
		this.photo = photo;
	}

	public int getAlbumNo() {
		return albumNo;
	}

	public void setAlbumNo(int albumNo) {
		this.albumNo = albumNo;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
	
}
