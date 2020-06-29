package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.xml.transform.Result;

import model.Sales;

public class SalesDAO {
	//DB연동
	public ArrayList<Sales> getSalesLoadList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Sales> salesList = null;

		try {
			con = DBUtil.getConnection();
			if (con != null) {
			} else {
				System.out.println(" Managercontroller.getSalesLoadList  : DB연결 실페");
			}

			String query = "select * from salestbl";

			pstmt = con.prepareStatement(query);

			rs = pstmt.executeQuery();
			salesList = new ArrayList<Sales>();

			while (rs.next()) {
				Sales sales = new Sales(rs.getDate(1).toLocalDate(), rs.getInt(2), rs.getInt(3));
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
