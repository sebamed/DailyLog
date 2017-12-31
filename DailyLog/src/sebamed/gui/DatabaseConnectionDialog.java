package sebamed.gui;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import sebamed.entity.Database;
import sebamed.main.DbConnection;
import sebamed.main.PropertiesFile;

public class DatabaseConnectionDialog extends JDialog implements ActionListener {
	
	// SWING
	private JPanel jp, jpMoreOptions;
	private JTextField textFieldServerAdress, textFieldServerPort, textFieldDbName, textFieldDbUser, textFieldNewDb, textFieldNewTable;
	private JPasswordField passFieldDbPassword;
	private JLabel labelServerAdress, labelServerPort, labelDbName, labelDbUser, labelDbPassword, labelShowAdvancedOptions;
	private JButton buttonConnect, buttonReset;
	private JCheckBox cbShowPassword, cbMakeDb, cbMakeTable, cbSaveSettings;
	
	// Properties
	private PropertiesFile pFile;
	
	// bool
	boolean moreOptions = false, showPassword = false, makeNewDb = false, makeNewTable = false, saveSettings = false;

	public DatabaseConnectionDialog(JFrame parent, String title) {

		super(parent, title);
		
		this.pFile = new PropertiesFile();
		
		// main panel
		this.jp = new JPanel();
		this.jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jp.setLayout(new GridBagLayout());
		GridBagConstraints gbcDbInfo = new GridBagConstraints();
		gbcDbInfo.fill = GridBagConstraints.HORIZONTAL;
		gbcDbInfo.insets = new Insets(3, 3, 3, 3);
		
		// more options panel
		this.jpMoreOptions = new JPanel();
		this.jpMoreOptions.setLayout(new GridBagLayout());
		this.jpMoreOptions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpMoreOptions.setVisible(false);
		GridBagConstraints gbcMoreOptions = new GridBagConstraints();
		gbcMoreOptions.fill = GridBagConstraints.HORIZONTAL;
		

		// text fields
		this.textFieldServerAdress = new JTextField(15);
		this.textFieldServerPort = new JTextField(15);
		this.textFieldDbName = new JTextField(15);
		this.textFieldDbUser = new JTextField(15);
		this.textFieldNewDb = new JTextField(15);
		this.textFieldNewTable = new JTextField(15);
		
		// password fields
		this.passFieldDbPassword = new JPasswordField(15);

		// labels
		this.labelServerAdress = new JLabel("Server adress:");
		this.labelServerPort = new JLabel("Server port:");
		this.labelDbName = new JLabel("Database name:");
		this.labelDbUser = new JLabel("Authentication username:");
		this.labelDbPassword = new JLabel("Authentication password:");
		this.labelShowAdvancedOptions = new JLabel("Show advanced options " + '\u25BC'); // 'BLACK DOWN-POINTING TRIANGLE' (U+25BC)

		// buttons
		this.buttonConnect = new JButton("Connect");
		this.buttonReset = new JButton("Reset");
		
		// check boxes
		this.cbShowPassword = new JCheckBox("Show password");
		this.cbMakeDb = new JCheckBox("Make me a database");
		this.cbMakeTable = new JCheckBox("Make me a table");
		this.cbSaveSettings = new JCheckBox("Remember current settings");

		this.add(this.jp);
		// show all in jp
		// server adress
		gbcDbInfo.gridx = 0;
		gbcDbInfo.gridy = 0;
		jp.add(this.labelServerAdress, gbcDbInfo);
		gbcDbInfo.gridx = 1;
		gbcDbInfo.gridy = 0;
		jp.add(this.textFieldServerAdress, gbcDbInfo);
		// server port
		gbcDbInfo.gridx = 0;
		gbcDbInfo.gridy = 1;
		jp.add(this.labelServerPort, gbcDbInfo);
		gbcDbInfo.gridx = 1;
		gbcDbInfo.gridy = 1;
		jp.add(this.textFieldServerPort, gbcDbInfo);
		// db name
		gbcDbInfo.gridx = 0;
		gbcDbInfo.gridy = 2;
		jp.add(this.labelDbName, gbcDbInfo);
		gbcDbInfo.gridx = 1;
		gbcDbInfo.gridy = 2;
		jp.add(this.textFieldDbName, gbcDbInfo);
		// db username
		gbcDbInfo.gridx = 0;
		gbcDbInfo.gridy = 3;
		jp.add(this.labelDbUser, gbcDbInfo);
		gbcDbInfo.gridx = 1;
		gbcDbInfo.gridy = 3;
		jp.add(this.textFieldDbUser, gbcDbInfo);
		// user password
		gbcDbInfo.gridx = 0;
		gbcDbInfo.gridy = 4;
		jp.add(this.labelDbPassword, gbcDbInfo);
		gbcDbInfo.gridx = 1;
		gbcDbInfo.gridy = 4;
		jp.add(this.passFieldDbPassword, gbcDbInfo);
		// show/hide password
		gbcDbInfo.gridx = 1;
		gbcDbInfo.gridy = 5;
		jp.add(this.cbShowPassword, gbcDbInfo);
		// buttons
		gbcDbInfo.gridwidth = 2;
		gbcDbInfo.gridx = 0;
		gbcDbInfo.gridy = 6;
		jp.add(this.buttonConnect, gbcDbInfo);
		gbcDbInfo.gridx = 0;
		gbcDbInfo.gridy = 7;
		jp.add(this.buttonReset, gbcDbInfo);

		// show advanced options
		gbcDbInfo.gridx = 0;
		gbcDbInfo.gridy = 8;
		
		try {
			this.getDbInfo();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		jp.add(this.labelShowAdvancedOptions, gbcDbInfo);
		
		// show all in show more jPanel
		// make new db
		gbcMoreOptions.gridx = 0;
		gbcMoreOptions.gridy = 0;
		jpMoreOptions.add(this.cbMakeDb, gbcMoreOptions);
		gbcMoreOptions.gridx = 1;
		gbcMoreOptions.gridy = 0;
		jpMoreOptions.add(this.textFieldNewDb, gbcMoreOptions);
		gbcMoreOptions.gridx = 0;
		gbcMoreOptions.gridy = 1;
		jpMoreOptions.add(this.cbMakeTable, gbcMoreOptions);
		gbcMoreOptions.gridx = 1;
		gbcMoreOptions.gridy = 1;
		jpMoreOptions.add(this.textFieldNewTable, gbcMoreOptions);
		gbcMoreOptions.gridwidth = 2;
		gbcMoreOptions.gridx = 0;
		gbcMoreOptions.gridy = 2;
		jpMoreOptions.add(this.cbSaveSettings, gbcMoreOptions);
		
		// settings for text fields in jpMoreOptions
		this.textFieldNewDb.setEnabled(false);
		this.textFieldNewDb.setText("Your database name");
			
		this.textFieldNewTable.setEnabled(false);
		this.textFieldNewTable.setText("Your table name");

		
		
		this.add(this.jpMoreOptions);

		// buttons action
		this.buttonConnect.addActionListener(this);
		this.buttonReset.addActionListener(this);

		this.labelShowAdvancedOptions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		// LISTENERS
		
		// CHECKBOX
		// Show/hide password
		this.cbShowPassword.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) { // password should be visible					
					DatabaseConnectionDialog.this.showPassword = true;
					DatabaseConnectionDialog.this.passFieldDbPassword.setEchoChar((char) 0);					
				} else { // password should be invisible
					DatabaseConnectionDialog.this.showPassword = false;
					DatabaseConnectionDialog.this.passFieldDbPassword.setEchoChar('\u2022'); // Unicode Character 'BULLET' (U+2022)
				}
			}
		});
		
		// make new db
		this.cbMakeDb.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					// settings for make new database
					DatabaseConnectionDialog.this.makeNewDb = true;
					DatabaseConnectionDialog.this.textFieldNewDb.setText(null);
					DatabaseConnectionDialog.this.textFieldNewDb.setEnabled(true);
					DatabaseConnectionDialog.this.textFieldNewDb.requestFocus();
					// settings for make new table (when user doesnt have a database, he doesnt have a table eather)
					DatabaseConnectionDialog.this.cbMakeTable.setSelected(true);
				} else {
					DatabaseConnectionDialog.this.makeNewDb = false;
					DatabaseConnectionDialog.this.textFieldNewDb.setText("Your database name");
					DatabaseConnectionDialog.this.textFieldNewDb.setEnabled(false);
				}
			}
		});
		
		// make new table
		this.cbMakeTable.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					DatabaseConnectionDialog.this.makeNewTable = true;
					DatabaseConnectionDialog.this.textFieldNewTable.setText(null);
					DatabaseConnectionDialog.this.textFieldNewTable.setEnabled(true);
					DatabaseConnectionDialog.this.textFieldNewTable.requestFocus();
				} else {
					if(DatabaseConnectionDialog.this.makeNewDb) { // must have both checked
						JOptionPane.showMessageDialog(DatabaseConnectionDialog.this, "You can't avoid making new table if you don't have a database!", "Warning", JOptionPane.WARNING_MESSAGE);
						return;
					}
					DatabaseConnectionDialog.this.makeNewTable = false;
					DatabaseConnectionDialog.this.textFieldNewTable.setText("Your table name");
					DatabaseConnectionDialog.this.textFieldNewTable.setEnabled(false);
				}
			}
		});
		
		// save preset
		this.cbSaveSettings.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					DatabaseConnectionDialog.this.saveSettings = true;
				} else {
					DatabaseConnectionDialog.this.saveSettings = false;
				}
			}
		});
		
		
		// show advanced options click
		this.labelShowAdvancedOptions.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (!DatabaseConnectionDialog.this.moreOptions) {
					// TODO: dodaj prosirenje sa opcijama za: pravljenje baze, pravljenje tabele	
					DatabaseConnectionDialog.this.jpMoreOptions.setVisible(true);
					DatabaseConnectionDialog.this.labelShowAdvancedOptions.setText("Hide advanced options "+ '\u25B2'); // 'BLACK UP-POINTING TRIANGLE' (U+25B2)
					DatabaseConnectionDialog.this.moreOptions = true;		
					pack();
				} else {
					DatabaseConnectionDialog.this.jpMoreOptions.setVisible(false);
					DatabaseConnectionDialog.this.moreOptions = false;
					DatabaseConnectionDialog.this.labelShowAdvancedOptions.setText("Show advanced options " + '\u25BC'); // 'BLACK UP-POINTING TRIANGLE' (U+25BC)
					pack();
				}

			}
		});
		

		// focus listener
		this.addWindowFocusListener(new WindowFocusListener() {

			public void windowGainedFocus(WindowEvent e) {

			}

			public void windowLostFocus(WindowEvent e) {
				// always focused
				DatabaseConnectionDialog.this.textFieldServerAdress.requestFocus();
				DatabaseConnectionDialog.this.toFront();
				// adds error sound
				final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit()
						.getDesktopProperty("win.sound.exclamation");
				if (runnable != null) {
					runnable.run();
				}
			}

		});

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setVisible(true);
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource() == this.buttonConnect) { // click on connect button
			if (this.textFieldServerAdress.getText().equals("") || this.textFieldServerPort.getText().equals("")
					|| this.textFieldDbName.getText().equals("") || this.textFieldDbUser.getText().equals("")
					|| this.passFieldDbPassword.getPassword().toString().equals("")) {
				JOptionPane.showMessageDialog(DatabaseConnectionDialog.this, "You have to fill in all the fields!", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				Connection conn = null;
				try {
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection(
							"jdbc:mysql://" + this.textFieldServerAdress.getText() + ":"
									+ this.textFieldServerPort.getText() + "/" + this.textFieldDbName.getText()
									+ "?useSSL=false",
							this.textFieldDbUser.getText(), new String(this.passFieldDbPassword.getPassword()));
				} catch (ClassNotFoundException e) {
					System.out.println("Error in class forname");
					return;
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null,
							"Informations you inserted are not correct, please check them again!", "Connection error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				System.out.println("DatabaseConnection.class: Konekcija uspesna!");
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.out.println("DatabaseConnection.class: Zatvorena konekcija!");

				JOptionPane.showMessageDialog(DatabaseConnectionDialog.this, "Success!", "Success", JOptionPane.INFORMATION_MESSAGE);

				try {
					this.checkForCheckBoxes();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// postavljanje staticke promenljive conn iz DbConnection koja ce se dalje
				// koristiti u app
				this.establishConnection(this.textFieldServerAdress.getText(), this.textFieldServerPort.getText(),
						this.textFieldDbName.getText(), this.textFieldDbUser.getText(),
						new String(this.passFieldDbPassword.getPassword()));

				this.dispose(); // closing the dialog

			}
		}

		else if (ae.getSource() == this.buttonReset) {
			this.textFieldServerAdress.setText(null);
			this.textFieldServerPort.setText(null);
			this.textFieldDbName.setText(null);
			this.textFieldDbUser.setText(null);
			this.passFieldDbPassword.setText(null);
			System.out.println("Resetovano");
		}

	}
	
	private void checkForCheckBoxes() throws IOException {
		if(this.saveSettings) {
			this.sendSettings();
		}
	}
	
	private void getDbInfo() throws IOException {
		if(this.pFile.fetchDbFromFile()!=null) {
			Database newDb = this.pFile.fetchDbFromFile();
			System.out.println("Saved preset" + newDb);
			this.textFieldServerAdress.setText(newDb.getDbAdress());
			this.textFieldServerPort.setText(newDb.getDbPort());
			this.textFieldDbName.setText(newDb.getDbName());
			this.textFieldDbUser.setText(newDb.getDbUsername());
		} else {
			return;
		}
	}
	
	private void sendSettings() throws IOException {
		this.pFile.writeToFile(new Database(this.textFieldServerAdress.getText(), this.textFieldDbName.getText(), this.textFieldServerPort.getText(), this.textFieldDbUser.getText()));
	}

	private void establishConnection(String serverAdress, String serverPort, String dbName, String username,
			String pass) {
		DbConnection.setConnection(serverAdress, serverPort, dbName, username, pass);
	}

}
