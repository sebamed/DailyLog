package sebamed.entity;

import java.util.Date;

public class Log {

	private String title;
	private String text;
	private Date datum;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	@Override
	public String toString() {
		return this.title + " | " + this.text + " | " + this.datum;
	}
	
}
