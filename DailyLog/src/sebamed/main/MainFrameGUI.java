package sebamed.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import sebamed.gui.DatabaseConnectionDialog;
import sebamed.gui.MainFrame;

public class MainFrameGUI {

	public MainFrameGUI() {
		MainFrame mframe = new MainFrame();
		mframe.setTitle("Daily Log Application");
		DatabaseConnectionDialog dbcDialog = new DatabaseConnectionDialog(mframe, "MySql Connection");

		// Listening to Main Frame close
		mframe.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to close the application?",
						"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (result == JOptionPane.NO_OPTION) {
					return;
				} else {

					if (DbConnection.getConnection() != null) {
						DbConnection.closeConnection(); // closing the connection
					} else {
						System.out.println("Closing without database connection");
					}
					
					System.exit(0);
				}
			}
		});

		// Listening to window close (it will close once user is connected to a
		// database)
		dbcDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent we) {
				if (dbcDialog.isConnected()) {
					try {
						mframe.setComponentsEnabled(true);
						mframe.refreshTable();
						mframe.allwaysOnBottom(1);
						mframe.getDatabaseMeta();
						mframe.refreshTodo();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						System.out.println(e1);
					}
				} else {
					mframe.enableMenu(true);
				}

				System.out.println("MainFrameGUI: Closed");
			}
		});

	}

}
