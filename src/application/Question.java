package application;

public class Question {
	private String questionText;
	private String[] answers;
	private String trueAnswer;
	private String solutionExp;
	public Question(String questionText, String[] answers, String trueAnswer, String solutionExp) {

		this.questionText = questionText;
		this.answers = answers;
		this.trueAnswer = trueAnswer;
		this.solutionExp = solutionExp;
		this.answers = answers;
	}

	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public String[] getAnswers() {
		return answers;
	}
	public void setAnswers(String[] answers) {
		this.answers = answers;
	}
	public String getTrueAnswer() {
		return trueAnswer;
	}
	public void setTrueAnswer(String trueAnswer) {
		this.trueAnswer = trueAnswer;
	}
	public String getSolutionExp() {
		return solutionExp;
	}
	public void setSolutionExp(String solutionExp) {
		this.solutionExp = solutionExp;
	}

}
