package sebamed.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;

import sebamed.gui.DatabaseConnectionDialog;
import sebamed.gui.MainFrame;

public class MainFrameGUI {

	public MainFrameGUI() {
		MainFrame mframe = new MainFrame();

		DatabaseConnectionDialog dbcDialog = new DatabaseConnectionDialog(mframe, "MySql Connection");

		// Listening to window close (it will close once user is connected to a
		// database)
		dbcDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (dbcDialog.isConnected()) {
					try {
						mframe.setComponentsEnabled(true);
						mframe.refreshTable();
						mframe.allwaysOnBottom();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						System.out.println(e1);
					}
				}

				System.out.println("MainFrameGUI: Closed");
			}
		});
	}

}
