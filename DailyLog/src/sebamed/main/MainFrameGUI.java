package sebamed.main;

import sebamed.gui.DatabaseConnectionDialog;
import sebamed.gui.MainFrame;

public class MainFrameGUI {
	
		MainFrame mframe = new MainFrame();
		DatabaseConnectionDialog dbcDialog = new DatabaseConnectionDialog(mframe, "test");

}
