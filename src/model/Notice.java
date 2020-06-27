package model;

import java.time.*;

public class Notice {

	private int noticeNo;
	private String title;
	private String contents;
	private LocalDate noticeDate;

	public Notice() {
		super();
	}

	public Notice(int noticeNo, String title, String contents, LocalDate noticeDate) {
		this.noticeNo = noticeNo;
		this.title = title;
		this.contents = contents;
		this.noticeDate = noticeDate;
	}

	public int getNoticeNo() {
		return noticeNo;
	}

	public void setNoticeNo(int noticeNo) {
		this.noticeNo = noticeNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public LocalDate getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(LocalDate noticeDate) {
		this.noticeDate = noticeDate;
	}

}
