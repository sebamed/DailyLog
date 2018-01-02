package sebamed.main;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class Borders {

	public static CompoundBorder combineBorders(String title, int size) {
		TitledBorder tBorder = BorderFactory.createTitledBorder(title);
		tBorder.setTitleJustification(TitledBorder.LEFT);
		Border mBorder = new EmptyBorder(size, size, size, size);
		return new CompoundBorder(tBorder, mBorder);
	}

}
