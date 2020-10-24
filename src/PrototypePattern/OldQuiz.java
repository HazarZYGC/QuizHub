package PrototypePattern;

public class OldQuiz extends Quiz {
	
	private boolean isSolved;
	private int score = -1;
	
	public OldQuiz(String quizName, String quizLevel, boolean isSolved, int score) {
		super(quizName, quizLevel);
		this.setSolved(isSolved);
		
		if(isSolved)
			this.setScore(score);
			
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isSolved() {
		return isSolved;
	}

	public void setSolved(boolean isSolved) {
		this.isSolved = isSolved;
	}

}
