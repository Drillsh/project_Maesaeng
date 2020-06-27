package controller;

import java.sql.*;

import javafx.collections.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.*;

public class NoticeDAO {
	// DB����
	public ObservableList<Notice> getNoticeLoadTotalList() {

		ObservableList<Notice> NoticeList = FXCollections.observableArrayList();
		String query = "select * from noticetbl";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Notice notice = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareCall(query);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				notice = new Notice(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate());
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return NoticeList;
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
}
