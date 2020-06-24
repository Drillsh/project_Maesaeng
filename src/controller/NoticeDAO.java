package controller;

import java.sql.*;

import javafx.collections.*;
import model.*;

public class NoticeDAO {
	public ObservableList<Notice> getNoticeLoadTotalList() {

		ObservableList<Notice> NoticeList = FXCollections.observableArrayList();
		String str = "select * from noticetbl";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Notice notice = null;

		try {
			con = DBUtil.getConnection();
			pstmt = con.prepareCall(str);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				notice = new Notice(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDate(4).toLocalDate());
				NoticeList.add(notice);
			}
		} catch (Exception e) {

		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return NoticeList;
	}




public Notice NoticeUpdate(Notice notice , int no) {
	//String notice ="update noticetbl set where no=?";
	return null;
	
}
}
