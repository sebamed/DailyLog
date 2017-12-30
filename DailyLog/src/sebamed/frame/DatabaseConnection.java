package sebamed.frame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sebamed.main.DbConnection;

public class DatabaseConnection extends JFrame implements ActionListener {

	JTextField textFieldServerAdress, textFieldServerPort, textFieldDbName, textFieldDbUser, textFieldDbPassword;
	JLabel labelServerAdress, labelServerPort, labelDbName, labelDbUser, labelDbPassword;
	JButton buttonConnect, buttonReset;
	JPanel jp;

	public DatabaseConnection() {

		this.jp = new JPanel();
		this.jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

		// text fields
		this.textFieldServerAdress = new JTextField(15);
		this.textFieldServerPort = new JTextField(15);
		this.textFieldDbName = new JTextField(15);
		this.textFieldDbUser = new JTextField(15);
		this.textFieldDbPassword = new JTextField(15);

		// labels
		this.labelServerAdress = new JLabel("Server adress:");
		this.labelServerPort = new JLabel("Server port:");
		this.labelDbName = new JLabel("Database name:");
		this.labelDbUser = new JLabel("Authentication username:");
		this.labelDbPassword = new JLabel("Authentication password:");

		// buttons
		this.buttonConnect = new JButton("Connect");
		this.buttonReset = new JButton("Reset");

		add(jp);
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
		jp.add(this.textFieldDbPassword);
		// buttons
		jp.add(this.buttonConnect);
		jp.add(this.buttonReset);

		// buttons action
		this.buttonConnect.addActionListener(this);
		this.buttonReset.addActionListener(this);

		jp.setLayout(new GridLayout(7, 2));
		this.setVisible(true);
		this.setSize(400, 250);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource() == this.buttonConnect) { // click on connect button
			if (this.textFieldServerAdress.getText().equals("") || this.textFieldServerPort.getText().equals("")
					|| this.textFieldDbName.getText().equals("") || this.textFieldDbUser.getText().equals("")
					|| this.textFieldDbPassword.getText().equals("")) {
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
							this.textFieldDbUser.getText(), this.textFieldDbPassword.getText());
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
				
				// postavljanje staticke promenljive conn iz DbConnection koja ce se dalje koristiti u app 
				this.establishConnection(this.textFieldServerAdress.getText(), this.textFieldServerPort.getText(),
						this.textFieldDbName.getText(), this.textFieldDbUser.getText(),
						this.textFieldDbPassword.getText());

			}
		}

		else if (ae.getSource() == this.buttonReset) {
			this.textFieldServerAdress.setText(null);
			this.textFieldServerPort.setText(null);
			this.textFieldDbName.setText(null);
			this.textFieldDbUser.setText(null);
			this.textFieldDbPassword.setText(null);
			System.out.println("Resetovano");
		}

	}

	public void establishConnection(String serverAdress, String serverPort, String dbName, String username,
			String pass) {
		DbConnection.setConnection(serverAdress, serverPort, dbName, username, pass);
	}

}
