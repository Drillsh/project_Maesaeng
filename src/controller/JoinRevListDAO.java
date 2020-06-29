package controller;

import java.sql.*;
import java.util.*;

import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import model.*;

public class JoinRevListDAO {
	// ������� ���� ã�� ��������
	public ArrayList<JoinRevList> getJoinRevListTotalLoadList() {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<JoinRevList> joinrevArrayList = new ArrayList<>();

		// mysql ����̹� �δ�, �����ͺ��̽� ��ü�� �����´�
		try {
			con = DBUtil.getConnection();

			if (con != null) {
				// ���� �������� �ľ����� �׻� �����ִ°� ����
				System.out.println("JoinRevListDAO.JoinRevListDAO: DB ���� ����");

			} else {
				System.out.println("JoinRevListDAO.JoinRevListDAO: DB ���� ����");
			}
			// con��ü�� ������ �������� ������ �� �ִ� (select, insert, update, delete)

			String query = "select uName, u.userID, RoomName, revDate, startTime, EndTime, PersonNum,  uphone\r\n"
					+ "from usertbl as u\r\n" + "join scheduletbl as s\r\n" + "on u.userID = s.userID;";

			// query���� �����ϱ� ���� �غ�.
			pstmt = con.prepareStatement(query);

			// �������� �����Ѵ�
			rs = pstmt.executeQuery();

			while (rs.next()) {
				JoinRevList jrl = new JoinRevList(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getDate(4).toLocalDate(), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getString(8));

				joinrevArrayList.add(jrl);
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���˿��");
			alert.setHeaderText(" �����߻�! ");
			alert.setContentText("����: \n" + e.getMessage());
			alert.showAndWait();

		} finally {

			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();

			} catch (SQLException e) {
				System.out.println("UserController.getJoinRevListTotalLoadList: " + e.getMessage());
			}

			return joinrevArrayList;
		}

	}

	// ������� ������ư���� ã�� ��������
	public int getReservationUpdate(JoinRevList joinrevlist) {

		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		try {
			con = DBUtil.getConnection();
			String query = "update noticetbl set  jname = ? , juserid= ?,jroonnum = ? , jstarttime = ?, jendtime = ? where jphone = ?";
			pstmt = con.prepareStatement(query);

			pstmt.setString(1, joinrevlist.getJName());
			pstmt.setString(2, joinrevlist.getJUserID());
			pstmt.setString(3, joinrevlist.getJRoomName());
			pstmt.setInt(4, joinrevlist.getJStartTime());
			pstmt.setInt(5, joinrevlist.getJEndTime());
			pstmt.setInt(6, joinrevlist.getJPersonNum());
			pstmt.setString(7, joinrevlist.getJPhone());

			returnValue = pstmt.executeUpdate();

			if (returnValue != 0) {

			} else {
				throw new Exception();
			}
			if (con != null) {
				System.out.println("NoticeDAO.getReservationUpdate : DB ���� ����");
			} else {
				System.out.println("NoticeDAO.getReservationUpdate : DB ���� ����");
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���� ����");
			alert.setHeaderText(joinrevlist + "�� ���� ����");
			alert.setContentText(joinrevlist + "�� �����Ƴ׿� ^^");
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

	// ������� ����
	public int getReservationDelete(String userID) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("JoinRevListDAO.getReservationDelete : DB ���� ����");
			} else {
				System.out.println("JoinRevListDAO.getReservationDelete : DB ���� ����");
			}
			
			String query = "delete from scheduletbl where userID = ?";
			pstmt = con.prepareStatement(query);

			pstmt.setString(1, userID);

			returnValue = pstmt.executeUpdate();


		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���� ����");
			alert.setHeaderText("���� �����߽��ϴ�!");
			alert.showAndWait();
		}

		return returnValue;
	}

	// ������� �˻�
	public ArrayList<JoinRevList> getReservationSearch(String userID) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<JoinRevList> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("JoinRevListDAO.getReservationSearch : DB ���� ����");
			} else {
				System.out.println("JoinRevListDAO.getReservationSearch : DB ���� ����");
			}
			
			String query = "select uName, u.userID, RoomName, revDate, startTime, EndTime, PersonNum,  uphone\n" + 
					"from scheduletbl as s\n" + 
					"join usertbl as u\n" + 
					"on s.userID = u.userID\n" + 
					"where s.userID like ?;";

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "%" + userID + "%");

			rs = pstmt.executeQuery();

			arrayList = new ArrayList<JoinRevList>();

			while (rs.next()) {
				JoinRevList jrl = new JoinRevList(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getDate(4).toLocalDate(), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getString(8));

				arrayList.add(jrl);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("�˻����˿��");
			alert.setHeaderText("�̸��� �Է��ϼ���.");
			alert.setContentText("��������:" + e.getMessage());
			alert.showAndWait();
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

		return arrayList;

	}
	
}
