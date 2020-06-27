package controller;

import java.sql.*;

import javafx.collections.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.*;

public class NoticeDAO {
	// DB연동
	public ObservableList<Notice> getNoticeLoadTotalList() {

		ObservableList<Notice> NoticeList = FXCollections.observableArrayList();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("NoticeDAO.getNoticeLoadTotalList : DB 연결 성공");
			} else {
				System.out.println("NoticeDAO.getNoticeLoadTotalList : DB 연결 실패");
			}
			
			String query = "select * from noticetbl";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Notice notice = new Notice(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate());
				NoticeList.add(notice);
			}
		} catch (Exception e) {

		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				
			}
		}

		return NoticeList;
	}

	// 테이블뷰에 저장된값을 DB에 저장
	public int NoticeUpdate(Notice notice, int no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("NoticeDAO.NoticeUpdate : DB 연결 성공");
			} else {
				System.out.println("NoticeDAO.NoticeUpdate : DB 연결 실패");
			}
			String query = "update noticetbl set no=?, title=?, content=?, noticedate=?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, notice.getNoticeNo());
			pstmt.setString(2, notice.getTitle());
			pstmt.setString(3, notice.getContents());
			pstmt.setDate(4, Date.valueOf(notice.getNoticeDate()));

			returnValue = pstmt.executeUpdate();
			if (returnValue != 0) {

			} else {
				throw new Exception();
			}
		} catch (Exception e) {

		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("공 지 사 항");
				alert.setContentText("저장:" + e.getMessage());
				alert.showAndWait();
			}
			return returnValue;

		}
	}

	public int getNoticeInsert(Notice notice) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("NoticeDAO.getNoticeInsert : DB 연결 성공");
			} else {
				System.out.println("NoticeDAO.getNoticeInsert : DB 연결 실패");
			}
			String query = "insert into noticetbl value(null, ? , ?, ?);";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, notice.getTitle());
			pstmt.setString(2, notice.getContents());
			pstmt.setDate(3, Date.valueOf(notice.getNoticeDate().now()));

			returnValue = pstmt.executeUpdate();
			
			if (returnValue != 0) {

			} else {
				throw new Exception();
			}
		} catch (Exception e) {

		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("공 지 사 항");
				alert.setContentText("저장:" + e.getMessage());
				alert.showAndWait();
			}
			return returnValue;

		}
	}
}
