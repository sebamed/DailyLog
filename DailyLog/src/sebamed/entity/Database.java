package sebamed.entity;

public class Database {

	private String dbAdress;
	private String dbName;
	private String dbPort;
	private String dbUsername;

	public Database() {
		
	}
	
	public Database(String dbAdress, String dbName, String dbPort, String dbUsername) {
		this.dbAdress = dbAdress;
		this.dbName = dbName;
		this.dbPort = dbPort;
		this.dbUsername = dbUsername;
	}

	public String getDbAdress() {
		return dbAdress;
	}

	public void setDbAdress(String dbAdress) {
		this.dbAdress = dbAdress;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}
	@Override
	public String toString() {
		return "Database [dbAdress=" + dbAdress + ", dbName=" + dbName + ", dbPort=" + dbPort + ", dbUsername="
				+ dbUsername;
	}
	
	

}
