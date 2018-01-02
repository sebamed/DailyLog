package sebamed.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import sebamed.entity.Database;

/* database.properties
 * saved = true
 * db_Adress = adress
 * db_Port = port
 * db_Name = name
 * db_Username = user
 * db_Password = pass
 */

public class PropertiesFile {
	
	private final String location = "database.properties";
	private File file = new File(this.location);
	
	private Properties prop;
	private Database db;
	
	public PropertiesFile() {
		this.prop = new Properties();
		this.db = new Database();
	}
	
	public void writeToFile(Database db) throws IOException {
		this.writeDbInfo(db);
	}
	
	private void writeDbInfo(Database db) throws IOException {
		OutputStream os = new FileOutputStream(this.location);
		this.prop.setProperty("db_Adress", db.getDbAdress());
		this.prop.setProperty("db_Port", db.getDbPort());
		this.prop.setProperty("db_Name", db.getDbName());
		this.prop.setProperty("db_Username", db.getDbUsername());
		this.prop.store(os, "Added db info");
		os.close();
		this.setSaved("true");
		System.out.println("Added db info: " + db);
	}
	
	private void setSaved(String saved) throws IOException {
		OutputStream os = new FileOutputStream(this.location);
		this.prop.setProperty("saved", saved);
		this.prop.store(os, "Automatic saved = false");
		os.close();
	}
	
	private boolean isSaved() throws IOException {
		InputStream is = new FileInputStream(this.location);
		this.prop.load(is);
		if(this.prop.getProperty("saved").equals("true")) {
			return true;
		} else if (this.prop.getProperty("saved") == null) {
			this.setSaved("false");
			return false;
		} else {
			return false;
		}
	}
	
	public String getLastLogin() throws IOException {
		InputStream is = new FileInputStream(this.location);
		this.prop.load(is);
		if(this.prop.getProperty("last_open") != null) {
			is.close();
			return this.prop.getProperty("last_open");
		} else {
			is.close();
			return null;
		}
	}
	
	private void createFile() throws IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		OutputStream os = new FileOutputStream(this.location);
		this.prop.setProperty("last_open", dateFormat.format(date));
		os.close();
	}
	
	public Database fetchDbFromFile() throws IOException {
		
		if(this.file.exists()) {	
			InputStream is = new FileInputStream(this.location);
			
			this.prop.load(is);
			
			if(this.isSaved()){ // there is saved preset
				this.db.setDbAdress(this.prop.getProperty("db_Adress"));
				this.db.setDbPort(this.prop.getProperty("db_Port"));
				this.db.setDbName(this.prop.getProperty("db_Name"));
				this.db.setDbUsername(this.prop.getProperty("db_Username"));
				is.close();
				return this.db;
			} else { // there is no saved preset
				is.close();
				System.out.println("No saved preset!");
				return null;
			}		
		} else {
			this.createFile();
			return null;
		}
	}

}
