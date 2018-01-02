package sebamed.entity;

public class Task {

	private String name;
	private String priority;

	public Task() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "Task [name=" + name + ", priority=" + priority + "]";
	}

}
