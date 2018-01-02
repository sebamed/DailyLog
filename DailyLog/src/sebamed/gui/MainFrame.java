package sebamed.gui;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sebamed.dao.LogDAO;
import sebamed.entity.Log;
import sebamed.main.DbConnection;
import sebamed.main.PropertiesFile;

public class MainFrame extends JFrame implements ActionListener {

	// Instances
	private LogDAO lDao;
	private Log log;
	private PropertiesFile propFile;
	
	private static String[] logInfo = new String[6];
	
	// SWING
	private JMenuBar mbMain;
	private JMenu mFile, mEdit;
	public JMenuItem miFConnect, miFExit, miEClearBase;
	private JLabel lblAddTitle, lblAddText, lblAddDate, lblServer, lblServerName, lblUser, lblUserName, lblDayCount, lblDaysCounted, lblLastLogin, lblLastLoggedIn;
	private JTabbedPane tpMain;
	private JPanel jpDataView, jpInfoView, jpServerInfo, jpToDo, jpTable, jpNew, jAddNewTab, jpTableTab,
			jpTableTabButtons, jpAboutServer, jpTodoTable;
	private JTable tblLogs;
	private JScrollPane spTableScroll, spTextArea;
	private JButton btnRefresh, btnAddNew, btnDelete, btnEdit, btnView, btnAdd;
	private JTextField tfLogTitle;
	private JTextArea taLogText;
	private JComboBox<String> combDate;
	

	public MainFrame() {

		this.propFile = new PropertiesFile();
		this.lDao = new LogDAO();
		this.log = new Log();
		
		// menu item	
		this.miFConnect = new JMenuItem("Connect");
		
		this.miFExit = new JMenuItem("Exit");
		this.miFExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if(DbConnection.getConnection()!=null) { // closing the connection
					DbConnection.closeConnection();
				}
				MainFrame.this.dispose();
			}
		});
		
		this.miEClearBase = new JMenuItem("Clear data in base");
		this.miEClearBase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) { // logic for delete whole base
				
				int response = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure you want to clear your whole base? There is no undo!", "Warning", JOptionPane.YES_NO_OPTION);
				if(response == JOptionPane.NO_OPTION) {
					return; 
				} else {
					try {
						MainFrame.this.lDao.clearBase();
						MainFrame.this.refreshTable();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		
		// menu
		this.mEdit = new JMenu("Edit");
		this.mEdit.add(this.miEClearBase);
		
		this.mFile = new JMenu("File");
		this.mFile.add(this.miFConnect);
		this.mFile.add(this.miFExit);
		
		// menu bar
		this.mbMain = new JMenuBar();
		this.mbMain.add(this.mFile);
		this.mbMain.add(this.mEdit);

		// combo boxes
		this.combDate = new JComboBox(this.getDates());
		this.combDate.setSelectedItem(this.combDate.getItemAt(0));

		// labels
		this.lblLastLogin = new JLabel("Last login:", SwingConstants.RIGHT);
		this.lblLastLoggedIn = new JLabel("", SwingConstants.LEFT);
		this.lblDayCount = new JLabel("Days:", SwingConstants.RIGHT);
		this.lblDaysCounted = new JLabel("", SwingConstants.LEFT);
		this.lblServer = new JLabel("Server name:", SwingConstants.RIGHT);
		this.lblServerName = new JLabel("", SwingConstants.LEFT);
		this.lblUser = new JLabel("Username:", SwingConstants.RIGHT);
		this.lblUserName = new JLabel("", SwingConstants.LEFT);
		
		this.lblAddTitle = new JLabel("Log title:", SwingConstants.RIGHT);
		this.lblAddText = new JLabel("Log text:", SwingConstants.RIGHT);
		this.lblAddDate = new JLabel("Choose date:", SwingConstants.RIGHT);

		// buttons
		this.btnRefresh = new JButton("Refresh");
		this.btnAddNew = new JButton("Add new");
		this.btnDelete = new JButton("Delete");
		this.btnEdit = new JButton("Edit");
		this.btnView = new JButton("View");
		this.btnAdd = new JButton("Add");

		// text fields
		this.tfLogTitle = new JTextField(15);
		this.taLogText = new JTextArea(7, 20);

		// Main panels
		this.jpDataView = new JPanel();
		this.jpDataView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpDataView.setBackground(Color.YELLOW);
		this.jpDataView.setLayout(new GridLayout(1, 1));

		this.jpInfoView = new JPanel();
		this.jpInfoView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpInfoView.setBackground(Color.BLUE);
		this.jpInfoView.setLayout(new BoxLayout(this.jpInfoView, BoxLayout.Y_AXIS));
		
		// scrollable
		this.spTextArea = new JScrollPane(this.taLogText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.tblLogs = new JTable() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) { // restrict user from editing the table
				return false;
			};
		};
		// tbl settings
		this.tblLogs.setCellSelectionEnabled(true);
		this.tblLogs.setColumnSelectionAllowed(false);
		this.tblLogs.setRowSelectionAllowed(true);
		this.tblLogs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// tbl double click listener
		this.tblLogs.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				if(me.getClickCount() == 2) {
					MainFrame.this.viewLog();
				}
			}
		});

		// Tab layout panel
		this.jpTable = new JPanel();
		this.jpTable.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpTable.setBackground(Color.RED);

		// Table and buttons panel
		this.jpTableTab = new JPanel();
		this.jpTableTab.setBackground(Color.GREEN);
		this.jpTableTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpTableTab.setLayout(new BoxLayout(this.jpTableTab, BoxLayout.Y_AXIS));

		// table tab buttons
		this.jpTableTabButtons = new JPanel();
		this.jpTableTabButtons.setBackground(Color.GRAY);
		this.jpTableTabButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpTableTabButtons.setLayout(new GridBagLayout());
		GridBagConstraints gbcButtons = new GridBagConstraints();
		gbcButtons.insets = new Insets(5, 5, 5, 5);

		gbcButtons.gridx = 0;
		gbcButtons.gridy = 0;
		this.jpTableTabButtons.add(this.btnRefresh, gbcButtons);

		gbcButtons.gridx = 1;
		gbcButtons.gridy = 0;
		this.jpTableTabButtons.add(this.btnAddNew, gbcButtons);

		gbcButtons.gridx = 2;
		gbcButtons.gridy = 0;
		this.jpTableTabButtons.add(this.btnView, gbcButtons);

		gbcButtons.gridx = 3;
		gbcButtons.gridy = 0;
		this.jpTableTabButtons.add(this.btnEdit, gbcButtons);

		gbcButtons.gridx = 4;
		gbcButtons.gridy = 0;
		this.jpTableTabButtons.add(this.btnDelete, gbcButtons);

		// Add new tab
		this.jpNew = new JPanel();
		this.jpNew.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// this.jpNew.setBackground(Color.MAGENTA);
		this.jpNew.setLayout(new GridBagLayout());
		GridBagConstraints gbcAddNew = new GridBagConstraints();
		gbcAddNew.insets = new Insets(5, 5, 5, 5);
		gbcAddNew.fill = GridBagConstraints.BOTH;

		gbcAddNew.gridx = 0;
		gbcAddNew.gridy = 0;
		this.jpNew.add(this.lblAddTitle, gbcAddNew);

		gbcAddNew.gridx = 1;
		gbcAddNew.gridy = 0;
		this.jpNew.add(this.tfLogTitle, gbcAddNew);

		gbcAddNew.gridx = 0;
		gbcAddNew.gridy = 1;
		this.jpNew.add(this.lblAddText, gbcAddNew);

		gbcAddNew.gridx = 1;
		gbcAddNew.gridy = 1;
		this.jpNew.add(this.spTextArea, gbcAddNew);

		gbcAddNew.gridx = 0;
		gbcAddNew.gridy = 2;
		this.jpNew.add(this.lblAddDate, gbcAddNew);

		gbcAddNew.gridx = 1;
		gbcAddNew.gridy = 2;
		this.jpNew.add(this.combDate, gbcAddNew);
		
		gbcAddNew.gridx = 0;
		gbcAddNew.gridy = 3;
		gbcAddNew.gridwidth = 2;
		this.jpNew.add(this.btnAdd, gbcAddNew);

		// tabs
		this.tpMain = new JTabbedPane();
		this.tpMain.addTab("Database", this.jpTableTab);
		this.tpMain.addTab("Add new", this.jpNew);
		this.tpMain.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				// temp fix for gui bug (size changing)
				try {
					MainFrame.this.refreshTable();
				} catch (SQLException e) {
					System.out.println(e);
				}
				MainFrame.this.pack();
			}
		});

		this.spTableScroll = new JScrollPane(this.tblLogs);

		// tab 1
		this.jpTableTab.add(this.spTableScroll);
		this.jpTableTab.add(this.jpTableTabButtons);
		
		// Info layout panel
		
		// todo info 
		this.jpTodoTable = new JPanel();
		this.jpTodoTable.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpTodoTable.setBackground(Color.RED);
		GridBagConstraints gbcToDo = new GridBagConstraints();
		gbcToDo.fill = GridBagConstraints.HORIZONTAL;
		gbcToDo.insets = new Insets(3, 5, 5, 3);
		
		
		// todo panel
		this.jpToDo = new JPanel();
		this.jpToDo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpToDo.setBackground(Color.PINK);
		this.jpToDo.add(this.jpTodoTable);

		// server about panel
		this.jpAboutServer = new JPanel();
		this.jpAboutServer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpAboutServer.setBackground(Color.YELLOW);
		this.jpAboutServer.setLayout(new GridBagLayout());
		GridBagConstraints gbcServerInfo = new GridBagConstraints();
		gbcServerInfo.fill = GridBagConstraints.HORIZONTAL;
		gbcServerInfo.insets = new Insets(3, 5, 3, 5);
		
		gbcServerInfo.gridx = 0;
		gbcServerInfo.gridy = 0;		
		this.jpAboutServer.add(this.lblServer, gbcServerInfo);
		
		gbcServerInfo.gridx = 1;
		gbcServerInfo.gridy = 0;		
		this.jpAboutServer.add(this.lblServerName, gbcServerInfo);
		
		gbcServerInfo.gridx = 0;
		gbcServerInfo.gridy = 1;		
		this.jpAboutServer.add(this.lblUser, gbcServerInfo);
		
		gbcServerInfo.gridx = 1;
		gbcServerInfo.gridy = 1;		
		this.jpAboutServer.add(this.lblUserName, gbcServerInfo);
		
		gbcServerInfo.gridx = 0;
		gbcServerInfo.gridy = 2;		
		this.jpAboutServer.add(this.lblDayCount, gbcServerInfo);
		
		gbcServerInfo.gridx = 1;
		gbcServerInfo.gridy = 2;		
		this.jpAboutServer.add(this.lblDaysCounted, gbcServerInfo);
		
		gbcServerInfo.gridx = 0;
		gbcServerInfo.gridy = 3;
		this.jpAboutServer.add(this.lblLastLogin, gbcServerInfo);
		
		gbcServerInfo.gridx = 1;
		gbcServerInfo.gridy = 3;
		this.jpAboutServer.add(this.lblLastLoggedIn, gbcServerInfo);
		
		// server info panel
		this.jpServerInfo = new JPanel();
		this.jpServerInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpServerInfo.setBackground(Color.CYAN);
		this.jpServerInfo.setLayout(new GridLayout(2, 1));
		this.jpServerInfo.add(this.jpAboutServer);
		
		// show all
		// table
		this.jpTable.add(this.tpMain);

		// in InfoView panel
		// JPanel for ToDo list
		this.jpInfoView.add(this.jpToDo);
		// JPanel for ServerInfo
		this.jpInfoView.add(this.jpServerInfo);

		// show main panels
		this.add(this.jpDataView);
		this.add(this.jpInfoView);

		// in DataView panel
		// JPanel for Tabs
		this.jpDataView.add(this.jpTable);

		// listeners
		this.btnRefresh.addActionListener(this);
		this.btnAddNew.addActionListener(this);
		this.btnAdd.addActionListener(this);
		this.btnView.addActionListener(this);

		// disabling all components if user is not connected
		this.setComponentsEnabled(false);
		this.setJMenuBar(this.mbMain);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
		this.setVisible(true);
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		// this.setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == this.btnRefresh) { // refresh button pressed
			try {
				this.refreshTable();
			} catch (SQLException e) {
				System.out.println(e);
			}
		} else if (ae.getSource() == this.btnAddNew) { // go to add new tab
			MainFrame.this.tpMain.setSelectedComponent(MainFrame.this.jpNew);
		} else if (ae.getSource() == this.btnAdd) { // go to add new tab
			try {
				MainFrame.this.checkForAddNew();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (ae.getSource() == this.btnView) { // open dialog with log info
			this.viewLog();
		}
	}

	public void refreshTable() throws SQLException {
		this.tblLogs.setModel(this.lDao.getDataSet());
		this.tblLogs.removeColumn(this.tblLogs.getColumnModel().getColumn(0)); // hiding the ID column
		System.out.println("Refreshed");
	}

	public void setComponentsEnabled(boolean enabled) {
		// TODO: Dodaj za sve zivo i nezivo
		// Tabs
		this.tpMain.setEnabled(enabled);

		// buttons
		this.btnRefresh.setEnabled(enabled);
		this.btnAddNew.setEnabled(enabled);
		this.btnView.setEnabled(enabled);
		this.btnEdit.setEnabled(enabled);
		this.btnDelete.setEnabled(enabled);
		
		// menu items
		this.miEClearBase.setEnabled(enabled);
		
		// menu
		this.enableMenu(enabled);
		
	}

	private String[] getDates() {
		String[] days = new String[3];
		DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		days[0] = (LocalDate.now()).format(dtFormatter) + " - Today";
		days[1] = (LocalDate.now().minusDays(1)).format(dtFormatter) + " - Yesterday";
		days[2] = (LocalDate.now().minusDays(2)).format(dtFormatter) + " - "
				+ LocalDate.now().minusDays(2).getDayOfWeek().toString().substring(0, 1).toUpperCase()
				+ LocalDate.now().minusDays(2).getDayOfWeek().toString().substring(1).toLowerCase();

		return days;
	}

	public void allwaysOnBottom() {
		JScrollBar verticalBar = this.spTableScroll.getVerticalScrollBar();
		AdjustmentListener downScroller = new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				Adjustable adjustable = e.getAdjustable();
				adjustable.setValue(adjustable.getMaximum());
				verticalBar.removeAdjustmentListener(this);
			}
		};
		verticalBar.addAdjustmentListener(downScroller);
	}
	
	private void checkForAddNew() throws Exception {
		if(this.tfLogTitle.getText().isEmpty()|| this.taLogText.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "You have to fill in all the fields!", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			this.log.setTitle(this.tfLogTitle.getText());
			this.log.setText(this.taLogText.getText());
			this.log.setDatum(this.getDate());
			System.out.println(this.log);
			
			this.lDao.addLog(this.log);
		}
	}
	
	public void enableMenu(boolean enabled) {
		this.mFile.setEnabled(enabled);
		this.mEdit.setEnabled(enabled);
	}

	private Date getDate() {
		Calendar cal = Calendar.getInstance();
		if(this.combDate.getSelectedIndex() == 0) { // date selected - today
			cal.add(Calendar.DATE, 0);
			return cal.getTime();
		} else if (this.combDate.getSelectedIndex() == 1) { // date selected - yesterday
			cal.add(Calendar.DATE, -1);
			return cal.getTime();
		} else if (MainFrame.this.combDate.getSelectedIndex() == 2) { // date selected - day before yesterday
			cal.add(Calendar.DATE, -2);
			return cal.getTime();
		} else {
			return null;
		}
	}
	
	private void viewLog() {
		if(this.tblLogs.getSelectionModel().isSelectionEmpty()) { // no selected rows
			JOptionPane.showMessageDialog(this, "There is no row selected!", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			this.logInfo[0] = this.tblLogs.getModel().getValueAt(this.tblLogs.getSelectedRow(), 0).toString(); // id
			this.logInfo[1] = this.tblLogs.getModel().getValueAt(this.tblLogs.getSelectedRow(), 1).toString(); // title
			this.logInfo[2] = this.tblLogs.getModel().getValueAt(this.tblLogs.getSelectedRow(), 2).toString(); // text
			this.logInfo[3] = this.tblLogs.getModel().getValueAt(this.tblLogs.getSelectedRow(), 3).toString(); // date
			this.logInfo[4] = this.tblLogs.getModel().getValueAt(this.tblLogs.getSelectedRow(), 4).toString(); // day
			this.logInfo[5] = this.tblLogs.getModel().getValueAt(this.tblLogs.getSelectedRow(), 5).toString(); // time

			for(String a : this.logInfo) {
				System.out.println(a);
			}
			
			LogInfoDialog liDialog = new LogInfoDialog(this, logInfo[1] + " - " + logInfo[3]);
			
			liDialog.addWindowFocusListener(new WindowFocusListener() {
				public void windowGainedFocus(WindowEvent e) {

				}

				public void windowLostFocus(WindowEvent we) { // dispose on click somewhere else than liDialog
					liDialog.dispose();
				}
			});
		}
	}
	
	public void getDatabaseMeta() throws Exception {
		DatabaseMetaData dbMd = DbConnection.getDatabaseMetaData();
		this.lblServerName.setText(dbMd.getUserName().split("@")[1]); // getUserName: user@adress
		this.lblUserName.setText(dbMd.getUserName().split("@")[0]); 
		this.lblDaysCounted.setText(this.tblLogs.getRowCount() + "");
		this.lblLastLoggedIn.setText(this.propFile.getLastLogin());
		this.pack();
	}
	
	public static String[] getLogInfo() {
		return logInfo;
	}	
}
