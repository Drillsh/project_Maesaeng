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

		// mysql ����̹� �δ�, �����ͺ��̽� ��ü�� �����´�
		try {
			con = DBUtil.getConnection();

			if (con != null) {
				// ���� �������� �ľ����� �׻� �����ִ°� ����
				System.out.println("ScheduleDAO.getRevList: DB ���� ����");

			} else {
				System.out.println("ScheduleDAO.getRevList: DB ���� ����");
			}
			// con��ü�� ������ �������� ������ �� �ִ� (select, insert, update, delete)

			String query = "select * from scheduletbl group by userID having revDate = ? and roomName = ?";

			
			// query���� �����ϱ� ���� �غ�.
			pstmt = con.prepareStatement(query);
			pstmt.setDate(1, Date.valueOf(date) );
			pstmt.setString(2, rName );

			// �������� �����Ѵ�
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Schedule schedule = new Schedule(rs.getString(1), rs.getDate(2).toLocalDate(), rs.getInt(3), rs.getInt(4), rs.getString(5),rs.getInt(6));

				arrayList.add(schedule);
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("���˿��");
			alert.setHeaderText("������ �˻� �����߻�! ");
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
				System.out.println("ScheduleDAO.registerSchedule: DB ���� ����");
			} else {
				System.out.println("ScheduleDAO.registerSchedule: DB ���� ����");
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
