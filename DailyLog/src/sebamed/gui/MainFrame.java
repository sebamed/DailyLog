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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

public class MainFrame extends JFrame implements ActionListener {

	// Instances
	private LogDAO lDao;

	// SWING
	private JLabel lblAddTitle, lblAddText, lblAddDate;
	private JTabbedPane tpMain;
	private JPanel jpDataView, jpInfoView, jpServerInfo, jpToDo, jpTable, jpNew, jAddNewTab, jpTableTab,
			jpTableTabButtons;
	private JTable tblLogs;
	private JScrollPane spTableScroll;
	private JButton btnRefresh, btnAddNew, btnDelete, btnEdit, btnView;
	private JTextField tfLogTitle;
	private JTextArea taLogText;
	private JComboBox combDate;

	public MainFrame() {

		this.lDao = new LogDAO();

		// combo boxes
		this.combDate = new JComboBox(this.getDates());
		this.combDate.setSelectedItem(this.combDate.getItemAt(0));

		// lables
		this.lblAddTitle = new JLabel("Log title:", SwingConstants.RIGHT);
		this.lblAddText = new JLabel("Log text:", SwingConstants.RIGHT);
		this.lblAddDate = new JLabel("Choose date:", SwingConstants.RIGHT);

		// buttons
		this.btnRefresh = new JButton("Refresh");
		this.btnAddNew = new JButton("Add new");
		this.btnDelete = new JButton("Delete");
		this.btnEdit = new JButton("Edit");
		this.btnView = new JButton("View");

		// text fields
		this.tfLogTitle = new JTextField(15);
		this.taLogText = new JTextArea(5, 15);

		// Main panels
		this.jpDataView = new JPanel();
		this.jpDataView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpDataView.setBackground(Color.YELLOW);
		this.jpDataView.setLayout(new GridLayout(1, 1));

		this.jpInfoView = new JPanel();
		this.jpInfoView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpInfoView.setBackground(Color.BLUE);
		this.jpInfoView.setLayout(new BoxLayout(this.jpInfoView, BoxLayout.Y_AXIS));

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
		this.jpNew.add(this.taLogText, gbcAddNew);

		gbcAddNew.gridx = 0;
		gbcAddNew.gridy = 2;
		this.jpNew.add(this.lblAddDate, gbcAddNew);

		gbcAddNew.gridx = 1;
		gbcAddNew.gridy = 2;
		this.jpNew.add(this.combDate, gbcAddNew);

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

		// scrollable
		this.spTableScroll = new JScrollPane(this.tblLogs);

		// tab 1
		this.jpTableTab.add(this.spTableScroll);
		this.jpTableTab.add(this.jpTableTabButtons);

		// Info layout panel
		// todo panel
		this.jpToDo = new JPanel();
		this.jpToDo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpToDo.setBackground(Color.PINK);

		// server info panel
		this.jpServerInfo = new JPanel();
		this.jpServerInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpServerInfo.setBackground(Color.CYAN);

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

		// disabling all components if user is not connected
		this.setComponentsEnabled(false);

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
		}
	}

	public void refreshTable() throws SQLException {
		this.tblLogs.setModel(this.lDao.getDataSet());
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

}
