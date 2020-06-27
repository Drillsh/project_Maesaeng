package controller;

import java.sql.*;
import java.util.*;

import javafx.collections.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.*;

//�ٹ�����DB�� �Ŵ�����Ʈ�ѷ��� DB����
public class AlbumDAO {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	ObservableList<Album> albumList = FXCollections.observableArrayList();
	ArrayList<Album> arrayList = null;

	public ArrayList<Album> AlbumTotal() {

		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("AlbumDAO.AlbumTotal : DB���Ἲ��");
			} else {
				System.out.println("AlbumDAO.AlbumTotal : DB�������");
			}

			String query = "select * from albumtbl";

			pstmt = con.prepareStatement(query);

			rs = pstmt.executeQuery();
			arrayList = new ArrayList<Album>();

			while (rs.next()) {
				Album albumImage = new Album(rs.getInt(1), rs.getString(2));

				arrayList.add(albumImage);
			}

		} catch (Exception e) {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e1) {

			}
		}
		return arrayList;
	}

	// �ٹ����� ������ư DB����
	public void getalbumUpdate(String url, int imgViewIndex) {

		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		
		
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("AlbumDAO.getalbumUpdate : DB ���� ����");
			} else {
				System.out.println("AlbumDAO.getalbumUpdate : DB ���� ����");
			}
			
			
			String query = "update albumtbl set photo =? where no = ?" ;

			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, url);
			pstmt.setInt(2, imgViewIndex);
			
			returnValue= pstmt.executeUpdate();
			
			if (returnValue == 1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("����  ����");
				alert.setHeaderText(imgViewIndex + "�� ���� ���� ����!!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("��������");
				alert.setHeaderText("�������� �����߻�.");
				alert.showAndWait();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("��������");
			alert.setHeaderText("�������� �����߻�222.");
			alert.showAndWait();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}

	}

	// �ٹ����� ���� DB����
	public int getAlbumDelete(int albumno) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		try {
			con = DBUtil.getConnection();
			if (con != null) {

			} else {

			}

			String query = "delete from albumtbl where no =?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, albumno);

			returnValue = pstmt.executeUpdate();
			if (returnValue != 0) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("���� ����");
				alert.setHeaderText(albumno + "�� ���� ����");
				alert.setContentText(albumno + "�� ����");
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
		}
		return returnValue;

	}

	
}
