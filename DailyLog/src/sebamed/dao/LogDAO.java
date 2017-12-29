package sebamed.dao;

import java.sql.*;
import sebamed.entity.Log;

public class LogDAO {
	
	private Connection con;
	private String query;
	private Log log;
	
	private String adress;
	private int port;
	private String dbName;
	private String username;
	private String pass;
	

	public LogDAO() {
		
	}

	public void addLog(Log log) throws Exception {
		
		this.query = "insert into logs(LogTitle, LogText) values('"+log.getTitle()+"', '"+log.getText()+"')"; // query za prepared statement
		
		Statement st = con.createStatement();
		st.executeUpdate(this.query);
		
		System.out.println("Dodat: " + log);
		
	}
}
