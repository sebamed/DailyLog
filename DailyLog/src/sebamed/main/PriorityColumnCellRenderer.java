package sebamed.main;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class PriorityColumnCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	
	public PriorityColumnCellRenderer() {

	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);

		if(value.equals("1")) { // red
			cell.setBackground(Color.RED);
			cell.setForeground(Color.WHITE);
		} else if (value.equals("2")) { // yellow
			cell.setBackground(Color.ORANGE);
			cell.setForeground(Color.WHITE);
		} else if (value.equals("3")) { // green
			cell.setBackground(Color.GREEN);
			cell.setForeground(Color.WHITE);
		} else { // white
			cell.setBackground(Color.WHITE);
			cell.setForeground(Color.GRAY);
		}
		
		return cell;
	}
}
