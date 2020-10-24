package PrototypePattern;

public abstract class Quiz implements Cloneable {

	private String quizName;
	private String quizLevel;
	
	private int id;
	
	public Quiz(String quizName,String quizLevel) {
		this.quizName = quizName;
		this.quizLevel = quizLevel;
	}
	
	public Object clone() {
		Object clone = null;
		try { clone = super.clone();} 
		catch (CloneNotSupportedException e) { e.printStackTrace();}
		return clone;
	}

	public String getQuizName() {
		return quizName;
	}
	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	public String getQuizLevel() {
		return quizLevel;
	}
	public void setQuizLevel(String quizLevel) {
		this.quizLevel = quizLevel;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	


}