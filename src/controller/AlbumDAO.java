package controller;

import java.sql.*;
import java.util.*;

import javafx.collections.*;
import model.*;

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

	public Album AlbumUpdate(Album album, int no) {

		String dml = "update albumtbl set" + "image = ? where no = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtil.getConnection();

			pstmt = con.prepareStatement(dml);
			pstmt.setString(1, album.getPhoto());
			pstmt.setInt(2, no);

			int i = pstmt.executeUpdate();
			if (i == 1) {

			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println("테스트입니다.");
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}

		return album;
	}
}
