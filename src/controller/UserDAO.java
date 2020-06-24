package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.*;

public class UserDAO {

	// user정보 찾아 가져오기
	public ArrayList<User> loadUserList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<User> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if (con != null) {

				System.out.println("UserDAO.loadUserList : DB 연결 성공");
			} else {
				System.out.println("UserDAO.loadUserList : DB 연결 실패");
			}
			String query = "select * from usertbl";

			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			arrayList = new ArrayList<User>();

			while (rs.next()) {
				User user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5));
				arrayList.add(user);
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("TotalList 점검요망");
			alert.setHeaderText("TotalList 문제방생");
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

//수정 이벤트 디비연동	
	public int getUpdate(User user) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		try {
			con = DBUtil.getConnection();

			if (con != null) {

				System.out.println("UserDAO.getUpdate : DB 연결 성공");
			} else {
				System.out.println("UserDAO.getUpdate : DB 연결 실패");
			}

			String query = "update usertbl set  uName=?, uPassword=?, uPhone=?,uEmail=? where userID = ?";

			pstmt = con.prepareStatement(query);

			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getPhone());
			pstmt.setString(4, user.getMail());
			pstmt.setString(5, user.getUserid());

			returnValue = pstmt.executeUpdate();

			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("수정 성공");
				alert.setHeaderText(user.getUserid() + "님 수정 성공");
				alert.setContentText(user.getName() + "님 수정성공하셨네요");
				alert.showAndWait();
			} else {
				throw new Exception("수정중 문제발생");
			}
		} catch (Exception e) {

		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("TotalList 수정점검요망");
				alert.setHeaderText("TotalList 수정에서문제방생\n Rootcontroller.btnEditAction");
				alert.setContentText("문제사항:" + e.getMessage());
				alert.showAndWait();
			}
		}
		return returnValue;
	}

//회원관리창안에서 데이터베이스 저장된내용을 수정하는 디비연동
	public int UserRegistry(User user) {

		Connection con = null;
		PreparedStatement pstmt = null;

		int returnValue = 0;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("userDAO.UserRegistry: DB 연동 성공");
			} else {
				System.out.println("userDAO.UserRegistry: DB 연동 실패");
			}
			String query = "insert into usertbl value(null,?,?,?)";

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, user.getUserid());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getMail());

			returnValue = pstmt.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("저장 성공");
				alert.setHeaderText(user.getName() + "님 저장 성공");
				alert.setContentText(user.getName() + "님 HELLO");
				alert.showAndWait();
			} else {
				throw new Exception(user.getName() + "문제 발생");
			}
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

//회원정보 삭제
	public int UserDelete(String user) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		try {
			con = DBUtil.getConnection();
			if (con != null) {

			}

			String query = "delete from usertbl where userid=? ";

			pstmt = con.prepareStatement(query);
			pstmt.setString(1, user);
			returnValue = pstmt.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("삭제 성공");
				alert.setHeaderText(user + "번 삭제 성공");
				alert.setContentText(user + "님 바위");
				alert.showAndWait();
			} else {
				throw new Exception("삭제중 문제발생");
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("TotalList 삭제점검요망");
			alert.setHeaderText("TotalList 삭제에서문제방생\n Rootcontroller.btndeleteaction");
			alert.setContentText("문제사항:" + e.getMessage());
			alert.showAndWait();
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
