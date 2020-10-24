package application;

public class Classroom {
	private String name;
	private DynamicQueue studentqueue = new DynamicQueue();
	public Classroom(String name, DynamicQueue studentqueue) {
		this.name = name;
		this.studentqueue = studentqueue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DynamicQueue getStudentqueue() {
		return studentqueue;
	}
	public void setStudentqueue(DynamicQueue studentqueue) {
		this.studentqueue = studentqueue;
	}
}
