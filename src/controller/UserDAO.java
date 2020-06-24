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

	// user���� ã�� ��������
	public ArrayList<User> loadUserList() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<User> arrayList = null;
		try {
			con = DBUtil.getConnection();
			if (con != null) {

				System.out.println("UserDAO.loadUserList : DB ���� ����");
			} else {
				System.out.println("UserDAO.loadUserList : DB ���� ����");
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
			alert.setTitle("TotalList ���˿��");
			alert.setHeaderText("TotalList �������");
			alert.setContentText("��������:" + e.getMessage());
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

//���� �̺�Ʈ ��񿬵�	
	public int getUpdate(User user) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;

		try {
			con = DBUtil.getConnection();

			if (con != null) {

				System.out.println("UserDAO.getUpdate : DB ���� ����");
			} else {
				System.out.println("UserDAO.getUpdate : DB ���� ����");
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
				alert.setTitle("���� ����");
				alert.setHeaderText(user.getUserid() + "�� ���� ����");
				alert.setContentText(user.getName() + "�� ���������ϼ̳׿�");
				alert.showAndWait();
			} else {
				throw new Exception("������ �����߻�");
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
				alert.setTitle("TotalList �������˿��");
				alert.setHeaderText("TotalList ���������������\n Rootcontroller.btnEditAction");
				alert.setContentText("��������:" + e.getMessage());
				alert.showAndWait();
			}
		}
		return returnValue;
	}

//ȸ������â�ȿ��� �����ͺ��̽� ����ȳ����� �����ϴ� ��񿬵�
	public int UserRegistry(User user) {

		Connection con = null;
		PreparedStatement pstmt = null;

		int returnValue = 0;
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("userDAO.UserRegistry: DB ���� ����");
			} else {
				System.out.println("userDAO.UserRegistry: DB ���� ����");
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
				alert.setTitle("���� ����");
				alert.setHeaderText(user.getName() + "�� ���� ����");
				alert.setContentText(user.getName() + "�� HELLO");
				alert.showAndWait();
			} else {
				throw new Exception(user.getName() + "���� �߻�");
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

//ȸ������ ����
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
				alert.setTitle("���� ����");
				alert.setHeaderText(user + "�� ���� ����");
				alert.setContentText(user + "�� ����");
				alert.showAndWait();
			} else {
				throw new Exception("������ �����߻�");
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("TotalList �������˿��");
			alert.setHeaderText("TotalList ���������������\n Rootcontroller.btndeleteaction");
			alert.setContentText("��������:" + e.getMessage());
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
