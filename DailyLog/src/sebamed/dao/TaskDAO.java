package sebamed.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import sebamed.entity.Task;
import sebamed.main.DbConnection;

public class TaskDAO {

	private String query = "";

	public TaskDAO() {

	}

	public void addTask(Task task) throws SQLException {
		this.query = "insert into tasks(TaskName, TaskPriority) values('" + task.getName() + "', '" + task.getPriority()
				+ "')";

		Statement st = DbConnection.getConnection().createStatement();
		st.executeUpdate(this.query);

		System.out.println("Dodat task: " + task);
	}

	public void clearBase() throws SQLException {
		this.query = "truncate table tasks";

		Statement st = DbConnection.getConnection().createStatement();
		st.executeUpdate(this.query);

		System.out.println("Sve izbrisano!");
	}

	public DefaultTableModel getTasks() throws SQLException {
		this.query = "select * from tasks";
		Statement st = DbConnection.getConnection().createStatement();
		ResultSet rs = st.executeQuery(query);

		return buildTableModel(rs);
	}

	private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();

		// names of columns
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column).substring(4)); // Column names start with Task prefix,
																			// removing them
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

	public void removeTask(int id) throws SQLException {
		this.query = "delete from tasks where TaskID = " + id;
		Statement st = DbConnection.getConnection().createStatement();
		st.executeUpdate(this.query);
		
		System.out.println("Obrisan: " + id);
		
	}

}
