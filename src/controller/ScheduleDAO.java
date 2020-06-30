package controller;

import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;

import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import model.*;

public class ScheduleDAO {

	public ArrayList<Schedule> getRevList(LocalDate date, String rName) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<Schedule> arrayList = new ArrayList<Schedule>();

		// mysql 드라이버 로더, 데이터베이스 객체를 가져온다
		try {
			con = DBUtil.getConnection();

			if (con != null) {
				// 언제 들어오는지 파악위해 항상 적어주는게 좋다
				System.out.println("ScheduleDAO.getRevList: DB 연결 성공");

			} else {
				System.out.println("ScheduleDAO.getRevList: DB 연결 실패");
			}
			// con객체를 가지고 쿼리문을 실행할 수 있다 (select, insert, update, delete)

			String query = "select * from scheduletbl group by userID having revDate = ? and roomName = ?";

			
			// query문을 실행하기 위한 준비.
			pstmt = con.prepareStatement(query);
			pstmt.setDate(1, Date.valueOf(date) );
			pstmt.setString(2, rName );

			// 쿼리문을 실행한다
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Schedule schedule = new Schedule(rs.getString(1), rs.getDate(2).toLocalDate(), rs.getInt(3), rs.getInt(4), rs.getString(5),rs.getInt(6));

				arrayList.add(schedule);
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("점검요망");
			alert.setHeaderText("스케줄 검색 문제발생! ");
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
				System.out.println("ScheduleDAO.getRevList: " + e.getMessage());
			}

			return arrayList;
		}

	}
	
	public int registerSchedule(Schedule schedule) {

		Connection con = null;
		PreparedStatement pstmt = null;

		int returnValue = 0;
		
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("ScheduleDAO.registerSchedule: DB 연동 성공");
			} else {
				System.out.println("ScheduleDAO.registerSchedule: DB 연동 실패");
			}
			String query = "insert into scheduletbl values(?,?,?,?,?,?)";

			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, schedule.getUserID());
			pstmt.setDate(2, Date.valueOf(schedule.getScheduleDate()));
			pstmt.setInt(3, schedule.getStartTime());
			pstmt.setInt(4, schedule.getEndTime());
			pstmt.setString(5, schedule.getRoomName());
			pstmt.setInt(6, schedule.getPersonNum());

			returnValue = pstmt.executeUpdate();
			
		
		} catch (Exception e) {
			
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {

			}
		} // finally

		return returnValue;

	}
	
}
