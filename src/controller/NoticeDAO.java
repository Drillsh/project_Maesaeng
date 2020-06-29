package controller;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import model.*;

public class NoticeDAO {
	// DB����
	public ArrayList<Notice> getNoticeLoadTotalList() {

		ArrayList<Notice> noticeList = new ArrayList<>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("NoticeDAO.getNoticeLoadTotalList : DB ���� ����");
			} else {
				System.out.println("NoticeDAO.getNoticeLoadTotalList : DB ���� ����");
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

	
	
	// ���̺�信 ����Ȱ��� DB�� ����
	public int NoticeUpdate(Notice notice, int no) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("NoticeDAO.NoticeUpdate : DB ���� ����");
			} else {
				System.out.println("NoticeDAO.NoticeUpdate : DB ���� ����");
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
				alert.setTitle("�� �� �� ��");
				alert.setContentText("����:" + e.getMessage());
				alert.showAndWait();
			}
			return returnValue;

		}
	}

	// ���̺�信 �߰�����
	public int getNoticeInsert(Notice notice) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("NoticeDAO.getNoticeInsert : DB ���� ����");
			} else {
				System.out.println("NoticeDAO.getNoticeInsert : DB ���� ����");
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
				alert.setTitle("�� �� �� ��");
				alert.setContentText("����:" + e.getMessage());
				alert.showAndWait();
			}
			return returnValue;

		}
	}

	// ���̺�信 ����� ���̺��� Ŭ���ؼ� �����ϴ� �̺�Ʈ
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
				System.out.println("NoticeDAO.getNoticeDelete : DB ���� ����");
			} else {
				System.out.println("NoticeDAO.getNoticeDelete : DB ���� ����");
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���� ����");
			alert.setHeaderText(notice + "�� ���� ����");
			alert.setContentText(notice + "�� ����");
			alert.showAndWait();
		}

		return returnValue;
	}

	// ���̺�信 ����� ���������� �����̺�Ʈ DB
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
				System.out.println("NoticeDAO.getNoticeUpdate : DB ���� ����");
			} else {
				System.out.println("NoticeDAO.getNoticeUpdate : DB ���� ����");
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���� ����");
			alert.setHeaderText(notice + "�� ���� ����");
			alert.setContentText(notice + "�� �����Ƴ׿� ^^");
			alert.showAndWait();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("�� �� �� ��");
				alert.setContentText("����:" + e.getMessage());
				alert.showAndWait();
			}
		}
		return returnValue;
	}
}
