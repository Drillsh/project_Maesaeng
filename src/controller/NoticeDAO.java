package controller;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import model.*;

public class NoticeDAO {
	// DB연동
	public ArrayList<Notice> getNoticeLoadTotalList() {

		ArrayList<Notice> noticeList = new ArrayList<>();

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
				noticeList.add(notice);
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

		return noticeList;
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

	// 테이블뷰에 추가삽입
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

	// 테이블뷰에 저장된 테이블을 클릭해서 삭제하는 이벤트
	public int getNoticeDelete(Notice notice) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		try {
			con = DBUtil.getConnection();
			String query = "delete from noticetbl where no = ?";
			pstmt = con.prepareStatement(query);

			pstmt.setInt(1, notice.getNoticeNo());

			returnValue = pstmt.executeUpdate();

			if (con != null) {
				System.out.println("NoticeDAO.getNoticeDelete : DB 연결 성공");
			} else {
				System.out.println("NoticeDAO.getNoticeDelete : DB 연결 실패");
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("삭제 성공");
			alert.setHeaderText(notice + "번 삭제 성공");
			alert.setContentText(notice + "님 바위");
			alert.showAndWait();
		}

		return returnValue;
	}

	// 테이블뷰에 저장된 공지사항을 수정이벤트 DB
	public int getNoticeUpdate(Notice notice) {

		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		try {
			con = DBUtil.getConnection();
			String query = "update noticetbl set  title = ? , contents = ? where no = ?";
			pstmt = con.prepareStatement(query);

			pstmt.setString(1, notice.getTitle());
			pstmt.setString(2, notice.getContents());
			pstmt.setInt(3, notice.getNoticeNo());

			returnValue = pstmt.executeUpdate();

			if (returnValue != 0) {

			} else {
				throw new Exception();
			}
			if (con != null) {
				System.out.println("NoticeDAO.getNoticeUpdate : DB 연결 성공");
			} else {
				System.out.println("NoticeDAO.getNoticeUpdate : DB 연결 실패");
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("수정 성공");
			alert.setHeaderText(notice + "번 수정 성공");
			alert.setContentText(notice + "님 수정됐네요 ^^");
			alert.showAndWait();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("공 지 사 항");
				alert.setContentText("수정:" + e.getMessage());
				alert.showAndWait();
			}
		}
		return returnValue;
	}
}
