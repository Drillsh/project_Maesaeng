package controller;

import java.sql.*;
import java.util.*;

import javafx.collections.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.*;

//앨범관리DB의 매니저컨트롤러와 DB연동
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
				System.out.println("AlbumDAO.AlbumTotal : DB연결성공");
			} else {
				System.out.println("AlbumDAO.AlbumTotal : DB연결실패");
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

	// 앨범관리 수정버튼 DB연동
	public void getalbumUpdate(String url, int imgViewIndex) {

		Connection con = null;
		PreparedStatement pstmt = null;
		int returnValue = 0;
		
		
		try {
			con = DBUtil.getConnection();
			if (con != null) {
				System.out.println("AlbumDAO.getalbumUpdate : DB 연결 성공");
			} else {
				System.out.println("AlbumDAO.getalbumUpdate : DB 연결 실패");
			}
			
			
			String query = "update albumtbl set photo =? where no = ?" ;

			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, url);
			pstmt.setInt(2, imgViewIndex);
			
			returnValue= pstmt.executeUpdate();
			
			if (returnValue == 1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("사진  수정");
				alert.setHeaderText(imgViewIndex + "번 사진 수정 성공!!");
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("사진수정");
				alert.setHeaderText("사진수정 문제발생.");
				alert.showAndWait();
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("사진수정");
			alert.setHeaderText("사진수정 문제발생222.");
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

	// 앨범관리 삭제 DB연동
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
				alert.setTitle("삭제 성공");
				alert.setHeaderText(albumno + "번 삭제 성공");
				alert.setContentText(albumno + "님 바위");
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
		}
		return returnValue;

	}

	
}
