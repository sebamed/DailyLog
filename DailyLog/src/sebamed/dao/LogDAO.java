package sebamed.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import sebamed.entity.Log;
import sebamed.main.DbConnection;

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
	
	public DefaultTableModel getDataSet() throws SQLException {
		String query = "select * from logs";
		Statement st = DbConnection.getConnection().createStatement();
		ResultSet rs = st.executeQuery(query);

		return buildTableModel(rs);
	}
	
	public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

		ResultSetMetaData metaData = rs.getMetaData();

		// names of columns
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}

		// data of the table
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}

		return new DefaultTableModel(data, columnNames);

	}
}
