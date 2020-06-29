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

		ArrayList<JoinRevList> jrllist = new ArrayList<JoinRevList>();

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
			//pstmt.setString(8,jphone);

			// �������� �����Ѵ�
			rs = pstmt.executeQuery();

			while (rs.next()) {
				JoinRevList jrl = new JoinRevList(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getDate(4).toLocalDate(), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getString(8));

				jrllist.add(jrl);
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���˿��");
			alert.setHeaderText("�� �˻� �����߻�! ");
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
				System.out.println("UserController.roomSearch: " + e.getMessage());
			}

			return jrllist;
		}

	}
}
