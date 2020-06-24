package controller;

public class UserDAO {

	//user정보 찾아 가져오기
	/*public ArrayList<User> getUserFind(String name) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<User> arrayList = new ArrayList<User>();

		// mysql 드라이버 로더, 데이터베이스 객체를 가져온다
		try {
			con = DBUtil.getConnection();

			if (con != null) {
				// 언제 들어오는지 파악위해 항상 적어주는게 좋다
				System.out.println("RootController.btnSearch: DB 연결 성공");

			} else {
				System.out.println("RootController.btnSearch: DB 연결 실패");
			}
			// con객체를 가지고 쿼리문을 실행할 수 있다 (select, insert, update, delete)

			String query = "select * from gradeTbl where name like ?";

			// query문을 실행하기 위한 준비.
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "%" + name + "%");

			// 쿼리문을 실행한다
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Student student = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
						rs.getDate(10).toLocalDate(), rs.getString(11));

				arrayList.add(student);
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("검색 점검요망");
			alert.setHeaderText("검색 문제발생! ");
			alert.setContentText("주의해주세요 \n" + e.getMessage());
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
				System.out.println("RootController.BtnSearch: " + e.getMessage());
			}

			return arrayList;
		}

}*/
	}
