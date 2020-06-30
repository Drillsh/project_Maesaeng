package controller;

import java.sql.*;
import java.util.*;

import model.*;

public class SalesDAO {
	//DB연동
	public ArrayList<Sales> getSalesLoadList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Sales> salesList = new ArrayList<Sales>();

		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println(" SalesDAO.getSalesLoadList  : DB연결 성공");
			} else {
				System.out.println(" SalesDAO.getSalesLoadList  : DB연결 실패");
			}

			String query = "select Month(revDate), sum(EndTime - startTime) * sum(PersonNum) * sum(price) AS '매출'  \n" + 
					"from scheduletbl AS sch\n" + 
					"join roomtbl As room\n" + 
					"on sch.RoomName = room.roomName\n" + 
					"group by Month(sch.revDate);";

			pstmt = con.prepareStatement(query);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				Sales sales = new Sales(rs.getInt(1), rs.getInt(2));
				salesList.add(sales);
			}

		} catch (Exception e) {

		}finally {
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
		return salesList;

	}
}
