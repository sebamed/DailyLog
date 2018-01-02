package sebamed.gui;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import sebamed.dao.TaskDAO;
import sebamed.entity.Task;

public class TodoAddDialog extends JDialog {

	private Task task;
	private TaskDAO tDao;
	
	private String[] priorities = {"1 - High", "2 - Medium", "3 - Low", "4 - Very low" };
	
	// SWING
	private JPanel jpMain;
	private JButton btnAdd;
	private JLabel lblName, lblPriority;
	private JTextField tfName;
	private JComboBox<String> cbPriority;
	
	public TodoAddDialog(JFrame parent, String title) {
		super(parent, title);
		
		this.task = new Task();
		this.tDao = new TaskDAO();
		
		// text fields
		this.tfName = new JTextField(15);
		
		// combo box
		this.cbPriority = new JComboBox(this.priorities);
		
		// Labels
		this.lblName = new JLabel("Task name:");
		this.lblPriority = new JLabel("Task priority:");
		
		// buttons
		this.btnAdd = new JButton("Add");
		
		// pannels
		this.jpMain = new JPanel();
		this.jpMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpMain.setLayout(new GridBagLayout());
		GridBagConstraints gbcMain = new GridBagConstraints();
		gbcMain.insets = new Insets(5, 5, 5, 5);
		gbcMain.fill = GridBagConstraints.HORIZONTAL;
		
		// show all
		gbcMain.gridx = 0;
		gbcMain.gridy = 0;
		this.jpMain.add(this.lblName, gbcMain);
		
		gbcMain.gridx = 1;
		gbcMain.gridy = 0;
		this.jpMain.add(this.tfName, gbcMain);
		
		gbcMain.gridx = 0;
		gbcMain.gridy = 1;
		this.jpMain.add(this.lblPriority, gbcMain);
		
		gbcMain.gridx = 1;
		gbcMain.gridy = 1;
		this.jpMain.add(this.cbPriority, gbcMain);
		
		gbcMain.gridwidth = 2;
		gbcMain.gridx = 0;
		gbcMain.gridy = 2;
		this.jpMain.add(this.btnAdd, gbcMain);
		
		this.btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					TodoAddDialog.this.checkBeforeAdding();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		this.add(this.jpMain);
		
		this.setLayout(new CardLayout());
		this.setVisible(true);
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}

	protected void checkBeforeAdding() throws SQLException {
		if(this.tfName.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "You need to specify the task name!", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			this.task.setName(this.tfName.getText());
			this.task.setPriority(this.getCheckBoxResult());
			
			tDao.addTask(this.task);
			
			System.out.println("Added: " + this.task);
			
			this.dispose();
		}	
	}
	
	protected String getCheckBoxResult() {
		if(this.cbPriority.getSelectedIndex() == 0) { // High
			return "1";
		} else if (this.cbPriority.getSelectedIndex() == 1) { // Medium
			return "2";
		} else if (this.cbPriority.getSelectedIndex() == 2) { // Low
			return "3";
		} else { // VeryLow
			return "4";
		}
 	}
	
}
