package controller;

import java.sql.*;
import java.util.*;

import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import model.*;

public class JoinRevListDAO {
	// 예약관리 정보 찾아 가져오기
	public ArrayList<JoinRevList> getJoinRevListTotalLoadList() {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<JoinRevList> joinrevArrayList = new ArrayList<>();

		// mysql 드라이버 로더, 데이터베이스 객체를 가져온다
		try {
			con = DBUtil.getConnection();

			if (con != null) {
				// 언제 들어오는지 파악위해 항상 적어주는게 좋다
				System.out.println("JoinRevListDAO.JoinRevListDAO: DB 연결 성공");

			} else {
				System.out.println("JoinRevListDAO.JoinRevListDAO: DB 연결 실패");
			}
			// con객체를 가지고 쿼리문을 실행할 수 있다 (select, insert, update, delete)

			String query = "select uName, u.userID, RoomName, revDate, startTime, EndTime, PersonNum,  uphone\r\n"
					+ "from usertbl as u\r\n" + "join scheduletbl as s\r\n" + "on u.userID = s.userID;";

			// query문을 실행하기 위한 준비.
			pstmt = con.prepareStatement(query);

			// 쿼리문을 실행한다
			rs = pstmt.executeQuery();

			while (rs.next()) {
				JoinRevList jrl = new JoinRevList(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getDate(4).toLocalDate(), rs.getInt(5), rs.getInt(6), rs.getInt(7), rs.getString(8));

				joinrevArrayList.add(jrl);
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("점검요망");
			alert.setHeaderText(" 문제발생! ");
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
				System.out.println("UserController.getJoinRevListTotalLoadList: " + e.getMessage());
			}

			return joinrevArrayList;
		}

	}

	// 예약관리 수정버튼정보 찾아 가져오기
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
				System.out.println("NoticeDAO.getReservationUpdate : DB 연결 성공");
			} else {
				System.out.println("NoticeDAO.getReservationUpdate : DB 연결 실패");
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("수정 성공");
			alert.setHeaderText(joinrevlist + "번 수정 성공");
			alert.setContentText(joinrevlist + "님 수정됐네요 ^^");
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

	// 예약관리 삭제
	public int getReservationDelete(String userID) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("JoinRevListDAO.getReservationDelete : DB 연결 성공");
			} else {
				System.out.println("JoinRevListDAO.getReservationDelete : DB 연결 실패");
			}
			
			String query = "delete from scheduletbl where userID = ?";
			pstmt = con.prepareStatement(query);

			pstmt.setString(1, userID);

			returnValue = pstmt.executeUpdate();


		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("삭제 에러");
			alert.setHeaderText("삭제 실패했습니다!");
			alert.showAndWait();
		}

		return returnValue;
	}

	// 예약관리 검색
	public ArrayList<JoinRevList> getReservationSearch(String userID) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<JoinRevList> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("JoinRevListDAO.getReservationSearch : DB 연결 성공");
			} else {
				System.out.println("JoinRevListDAO.getReservationSearch : DB 연결 실패");
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
			alert.setTitle("검색점검요망");
			alert.setHeaderText("이름을 입력하세요.");
			alert.setContentText("문제사항:" + e.getMessage());
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
