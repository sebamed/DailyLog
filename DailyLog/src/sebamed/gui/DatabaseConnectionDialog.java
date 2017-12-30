package sebamed.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.BorderFactory;
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

import sebamed.main.DbConnection;

public class DatabaseConnectionDialog extends JDialog implements ActionListener {

	JPanel jp, jpMoreOptions;
	JTextField textFieldServerAdress, textFieldServerPort, textFieldDbName, textFieldDbUser;
	JPasswordField passFieldDbPassword;
	JLabel labelServerAdress, labelServerPort, labelDbName, labelDbUser, labelDbPassword, labelShowAdvancedOptions;
	JButton buttonConnect, buttonReset;
	JCheckBox cbShowPassword;

	boolean moreOptions = false;
	boolean showPassword = false;

	public DatabaseConnectionDialog(JFrame parent, String title) {

		super(parent, title);

		// main panel
		this.jp = new JPanel();
		this.jp.setBorder(BorderFactory.createEmptyBorder(10, 10, -25, 10));
		

		// more options panel
		this.jpMoreOptions = new JPanel();
		this.jpMoreOptions.setBackground(Color.YELLOW);
		this.jpMoreOptions.add(new JLabel("asd"));
		this.jpMoreOptions.add(new JLabel("asd"));
		this.jpMoreOptions.add(new JLabel("asd"));
		this.jpMoreOptions.add(new JLabel("asd"));
		this.jpMoreOptions.add(new JLabel("asd"));

		// text fields
		this.textFieldServerAdress = new JTextField(15);
		this.textFieldServerPort = new JTextField(15);
		this.textFieldDbName = new JTextField(15);
		this.textFieldDbUser = new JTextField(15);
		this.passFieldDbPassword = new JPasswordField(15);

		// labels
		this.labelServerAdress = new JLabel("Server adress:");
		this.labelServerPort = new JLabel("Server port:");
		this.labelDbName = new JLabel("Database name:");
		this.labelDbUser = new JLabel("Authentication username:");
		this.labelDbPassword = new JLabel("Authentication password:");
		this.labelShowAdvancedOptions = new JLabel("Show advanced options");

		// buttons
		this.buttonConnect = new JButton("Connect");
		this.buttonReset = new JButton("Reset");
		
		// check boxes
		this.cbShowPassword = new JCheckBox("Show password");

		add(this.jp);
		// show all
		// server adress
		jp.add(this.labelServerAdress);
		jp.add(this.textFieldServerAdress);
		// server port
		jp.add(this.labelServerPort);
		jp.add(this.textFieldServerPort);
		// db name
		jp.add(this.labelDbName);
		jp.add(this.textFieldDbName);
		// db username
		jp.add(this.labelDbUser);
		jp.add(this.textFieldDbUser);
		// user password
		jp.add(this.labelDbPassword);
		jp.add(this.passFieldDbPassword);
		
		jp.add(new JLabel("")); // placeholder
		jp.add(this.cbShowPassword);
		// buttons
		jp.add(this.buttonConnect);
		jp.add(this.buttonReset);

		// show advanced options
		jp.add(this.labelShowAdvancedOptions);

		// buttons action
		this.buttonConnect.addActionListener(this);
		this.buttonReset.addActionListener(this);

		this.labelShowAdvancedOptions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		// LISTENERS
		
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
		
		// show advanced options click
		this.labelShowAdvancedOptions.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (!DatabaseConnectionDialog.this.moreOptions) {
					// TODO: dodaj prosirenje sa opcijama za: pravljenje baze, pravljenje tabele
					DatabaseConnectionDialog.this.add(DatabaseConnectionDialog.this.jpMoreOptions);
					DatabaseConnectionDialog.this.setSize(400, 400);
					DatabaseConnectionDialog.this.labelShowAdvancedOptions.setText("Hide advanced options");
					DatabaseConnectionDialog.this.moreOptions = true;
				} else {
					DatabaseConnectionDialog.this.setSize(400, 300);
					DatabaseConnectionDialog.this.moreOptions = false;
					DatabaseConnectionDialog.this.labelShowAdvancedOptions.setText("Show advanced options");
					
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

		jp.setLayout(new GridLayout(9, 2));

		this.setVisible(true);
		this.setSize(400, 300);
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
				JOptionPane.showMessageDialog(null, "You have to fill in all the fields!", "Error",
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

				JOptionPane.showMessageDialog(null, "Success!", "Success", JOptionPane.INFORMATION_MESSAGE);

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

	public void establishConnection(String serverAdress, String serverPort, String dbName, String username,
			String pass) {
		DbConnection.setConnection(serverAdress, serverPort, dbName, username, pass);
	}

}
