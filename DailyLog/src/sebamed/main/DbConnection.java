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
			String query = "insert into logs(LogTitle, LogText) values('123', '321')"; // query za prepared statement
			
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		} catch (Exception e){
			System.out.println(e);
		}
	}
	
}
