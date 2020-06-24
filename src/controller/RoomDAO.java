package controller;

import java.sql.*;
import java.util.*;

import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import model.*;

public class RoomDAO {
	
	// Room ���� ã�� ��������
	public ArrayList<Room> getRoomInfoFromName(String name) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<Room> arrayList = new ArrayList<Room>();

		// mysql ����̹� �δ�, �����ͺ��̽� ��ü�� �����´�
		try {
			con = DBUtil.getConnection();

			if (con != null) {
				// ���� �������� �ľ����� �׻� �����ִ°� ����
				System.out.println("UserController.roomSearch: DB ���� ����");

			} else {
				System.out.println("UserController.roomSearch: DB ���� ����");
			}
			// con��ü�� ������ �������� ������ �� �ִ� (select, insert, update, delete)

			String query = "select * from roomTbl where roomName like ?";

			// query���� �����ϱ� ���� �غ�.
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "%" + name + "%");

			// �������� �����Ѵ�
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Room room = new Room(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getBoolean(4),
						rs.getBoolean(5));

				arrayList.add(room);
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

			return arrayList;
		}

	}

	
}
