package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.JoinRevList;

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
				System.out.println("UserController.JoinRevListDAO: DB ���� ����");

			} else {
				System.out.println("UserController.JoinRevListDAO: DB ���� ����");
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

}
