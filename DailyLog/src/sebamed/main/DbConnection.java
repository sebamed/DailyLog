package sebamed.main;

import java.sql.*;

public class DbConnection {

	private static Connection conn = null;

	public static Connection getConnection() {
		return conn;
	}
	
	public static void setConnection(String serverAdress, String serverPort, String dbName, String username, String pass) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://"+serverAdress+":"+serverPort+"/"+dbName+"?useSSL=false", username, pass);
			System.out.println("konektovan!");
			
		} catch (Exception e){
			System.out.println(e);
		}
	}
	
	public static void closeConnection() {
		try {
			conn.close();
			System.out.println("Zatvorena konekcija!");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Konekcija nije zatvorena!");
		}
	}
	
	public static DatabaseMetaData getDatabaseMetaData() throws Exception {
		return conn.getMetaData();
	}
	
}
