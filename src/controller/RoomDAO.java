package controller;

import java.sql.*;
import java.util.*;

import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import model.*;

public class RoomDAO {
	
	// Room 정보 찾아 가져오기
	public ArrayList<Room> getRoomInfoFromName(String name) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<Room> arrayList = new ArrayList<Room>();

		// mysql 드라이버 로더, 데이터베이스 객체를 가져온다
		try {
			con = DBUtil.getConnection();

			if (con != null) {
				// 언제 들어오는지 파악위해 항상 적어주는게 좋다
				System.out.println("UserController.roomSearch: DB 연결 성공");

			} else {
				System.out.println("UserController.roomSearch: DB 연결 실패");
			}
			// con객체를 가지고 쿼리문을 실행할 수 있다 (select, insert, update, delete)

			String query = "select * from roomTbl where roomName like ?";

			// query문을 실행하기 위한 준비.
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "%" + name + "%");

			// 쿼리문을 실행한다
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Room room = new Room(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getBoolean(4),
						rs.getBoolean(5));

				arrayList.add(room);
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("점검요망");
			alert.setHeaderText("룸 검색 문제발생! ");
			alert.setContentText("원인: \n" + e.getMessage());
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
