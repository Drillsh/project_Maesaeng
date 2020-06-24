package controller;

import java.sql.*;

public class DBUtil {
	public static final String DRIVER = "org.mariadb.jdbc.Driver";
	public static final String URL = "jdbc:mariadb://jbstv.synology.me:3307/mstudycafedb";
	public static UserController userCon;

	public static Connection getConnection() throws Exception {
		Class.forName(DRIVER);
		Connection con = DriverManager.getConnection(URL, "mstudycafedb", "Maesaeng3!");
		return con;

	}
}