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
	// 예약관리 정보 찾아 가져오기
	public ArrayList<JoinRevList> getJoinRevListTotalLoadList() {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<JoinRevList> jrllist = new ArrayList<JoinRevList>();

		// mysql 드라이버 로더, 데이터베이스 객체를 가져온다
		try {
			con = DBUtil.getConnection();

			if (con != null) {
				// 언제 들어오는지 파악위해 항상 적어주는게 좋다
				System.out.println("UserController.JoinRevListDAO: DB 연결 성공");

			} else {
				System.out.println("UserController.JoinRevListDAO: DB 연결 실패");
			}
			// con객체를 가지고 쿼리문을 실행할 수 있다 (select, insert, update, delete)

			String query = "select uName, u.userID, RoomName, revDate, startTime, EndTime, PersonNum,  uphone\r\n"
					+ "from usertbl as u\r\n" + "join scheduletbl as s\r\n" + "on u.userID = s.userID;";

			// query문을 실행하기 위한 준비.
			pstmt = con.prepareStatement(query);
			//pstmt.setString(8,jphone);

			// 쿼리문을 실행한다
			rs = pstmt.executeQuery();

			while (rs.next()) {
				JoinRevList jrl = new JoinRevList(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getDate(4).toLocalDate(), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getString(8));

				jrllist.add(jrl);
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

			return jrllist;
		}

	}
}
