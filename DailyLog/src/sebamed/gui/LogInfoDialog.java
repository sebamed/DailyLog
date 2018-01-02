package sebamed.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class LogInfoDialog extends JDialog {

	private String[] logInfo = MainFrame.getLogInfo();
	
	// SWING
	private JPanel jpInfo;
	private JLabel lblTitle, lblLogTitle, lblText, lblDate, lblLogDate, lblTime, lblLogTime, lblDay, lblLogDay;
	private JTextArea taLogText;
	private JScrollPane spLogText;
	private JButton btnClose;
	
	public LogInfoDialog(JFrame parent, String title) {
		super(parent, title);
		
		// buttons
		this.btnClose = new JButton("Close");
		this.btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				LogInfoDialog.this.dispose();
			}
		});
		
		// text area
		this.taLogText = new JTextArea(7, 30);          
		this.taLogText.setText(this.logInfo[2]);
		this.taLogText.setLineWrap(true);
		this.taLogText.setWrapStyleWord(true);
		this.taLogText.setEditable(false);
		
		// scrollpane
		this.spLogText = new JScrollPane(this.taLogText);
		this.spLogText.setPreferredSize(new Dimension(400, 200));
		
		// labels 
		this.lblTitle = new JLabel("Log title:", SwingConstants.RIGHT);
		this.lblLogTitle = new JLabel(this.logInfo[1], SwingConstants.LEFT);
		this.lblText = new JLabel("Log text:", SwingConstants.RIGHT);
		this.lblDate = new JLabel("Log date:", SwingConstants.RIGHT);
		this.lblLogDate = new JLabel(this.logInfo[3] + ", " + this.logInfo[5], SwingConstants.LEFT);
		this.lblTime = new JLabel("Log text:", SwingConstants.RIGHT);
		this.lblLogTime = new JLabel(this.logInfo[5], SwingConstants.LEFT);
		this.lblDay = new JLabel("Log text:", SwingConstants.RIGHT);
		this.lblLogDay = new JLabel(this.logInfo[4], SwingConstants.LEFT);
		this.lblLogTitle.setForeground(Color.gray);
		this.lblLogDay.setForeground(Color.gray);
		this.lblLogTime.setForeground(Color.gray);
		this.lblLogDate.setForeground(Color.gray);
		
		// panels
		this.jpInfo = new JPanel();
		this.jpInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.jpInfo.setLayout(new GridBagLayout());
		GridBagConstraints gbcInfo = new GridBagConstraints();
		gbcInfo.insets = new Insets(5, 5, 5, 5);
		gbcInfo.fill = gbcInfo.HORIZONTAL;
		
		// show all
		gbcInfo.gridx = 0;
		gbcInfo.gridy = 0;
		this.jpInfo.add(this.lblTitle, gbcInfo);
		
		gbcInfo.gridx = 1;
		gbcInfo.gridy = 0;
		this.jpInfo.add(this.lblLogTitle, gbcInfo);
		
		gbcInfo.gridx = 0;
		gbcInfo.gridy = 1;
		this.jpInfo.add(this.lblDay, gbcInfo);
		
		gbcInfo.gridx = 1;
		gbcInfo.gridy = 1;
		this.jpInfo.add(this.lblLogDay, gbcInfo);
		
		gbcInfo.gridx = 0;
		gbcInfo.gridy = 2;
		this.jpInfo.add(this.lblDate, gbcInfo);
		
		gbcInfo.gridx = 1;
		gbcInfo.gridy = 2;
		this.jpInfo.add(this.lblLogDate, gbcInfo);
		
		gbcInfo.gridx = 0;
		gbcInfo.gridy = 3;
		this.jpInfo.add(this.lblText, gbcInfo);
		
		gbcInfo.gridwidth = 2;
		gbcInfo.gridx = 0;
		gbcInfo.gridy = 4;
		this.jpInfo.add(this.spLogText, gbcInfo);
		
		gbcInfo.gridx = 0;
		gbcInfo.gridy = 5;
		this.jpInfo.add(this.btnClose, gbcInfo);
		
		this.add(this.jpInfo);
		
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setVisible(true);
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}
	
}
