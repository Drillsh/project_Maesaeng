package controller;

public class UserDAO {

	//user���� ã�� ��������
	/*public ArrayList<User> getUserFind(String name) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<User> arrayList = new ArrayList<User>();

		// mysql ����̹� �δ�, �����ͺ��̽� ��ü�� �����´�
		try {
			con = DBUtil.getConnection();

			if (con != null) {
				// ���� �������� �ľ����� �׻� �����ִ°� ����
				System.out.println("RootController.btnSearch: DB ���� ����");

			} else {
				System.out.println("RootController.btnSearch: DB ���� ����");
			}
			// con��ü�� ������ �������� ������ �� �ִ� (select, insert, update, delete)

			String query = "select * from gradeTbl where name like ?";

			// query���� �����ϱ� ���� �غ�.
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, "%" + name + "%");

			// �������� �����Ѵ�
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Student student = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
						rs.getDate(10).toLocalDate(), rs.getString(11));

				arrayList.add(student);
			}

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("�˻� ���˿��");
			alert.setHeaderText("�˻� �����߻�! ");
			alert.setContentText("�������ּ��� \n" + e.getMessage());
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
