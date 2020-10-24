package controller;

import application.*;
import FactoryPattern.*;
import PrototypePattern.QuizCache;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class studentSolveQuizController implements Initializable {


	private ResultSet resultSet = null;
	private double xOffset = 0;
	private double yOffset = 0;

	static ArrayList<Question> questionList = new ArrayList<Question>();
	static ArrayList<String> studentAnswers = new ArrayList<String>();
	static ListIterator<Question> it;
	static int index;
	static int questionNum = 1;
	static String control = "";
	private String studentAnswer = "";
	@SuppressWarnings("unused")
	private int selectedQuizId = 0;
	@FXML
	ImageView back;

	@FXML
	Text username;

	@FXML
	ImageView logout;

	@FXML
	Label exit;

	@FXML
	Label questionNumber;

	@FXML
	TextArea question;

	@FXML
	RadioButton answer1;

	@FXML
	RadioButton answer2;

	@FXML
	RadioButton answer3;

	@FXML
	RadioButton answer4;

	@FXML
	ToggleGroup selection;

	@FXML
	Button backQuestion;

	@FXML
	Button nextQuestion;



	public void closePage()
	{
		System.exit(0);
	}

	public void BackPage(MouseEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/studentDisplayNewQuizzes.fxml"));
		Parent signUp = loader.load();
		Scene signUprez = new Scene(signUp);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		signUp.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		//set mouse drag
		signUp.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				window.setX(event.getScreenX() - xOffset);
				window.setY(event.getScreenY() - yOffset);
			}
		});
		window.setScene(signUprez);
		window.show();
	}


	public void logout(MouseEvent event) throws IOException 
	{
		Main.activeEmail = null;
		Main.activeName = null;
		Main.activePassword = null;
		Main.activeSurname = null;
		Main.activeUserID = 0;
		Teacher.newQuizID = 0;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/LoginPage.fxml"));
		Parent signUp = loader.load();
		Scene signUprez = new Scene(signUp);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		signUp.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		//set mouse drag
		signUp.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				window.setX(event.getScreenX() - xOffset);
				window.setY(event.getScreenY() - yOffset);
			}
		});
		window.setScene(signUprez);
		window.show();
	}

	public void questionList() throws SQLException, ClassNotFoundException {

		//New connection.
		Connection c = null;
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
		String query = "select id from quiz where title = (?)";
		PreparedStatement preparedStmt = c.prepareStatement(query);
		preparedStmt.setString(1, OldQuizzesController.quizName);
		resultSet = preparedStmt.executeQuery();

		if(resultSet.next())
			selectedQuizId = resultSet.getInt("id");

		query = "select * from question where quizid = (?)";
		preparedStmt = c.prepareStatement(query);
		preparedStmt.setInt(1,StudentDisplayNewQuizzesController.activeQuizID);
		resultSet = preparedStmt.executeQuery();
		while(resultSet.next())
		{
			String[] answerArr = {resultSet.getString("answer1"),resultSet.getString("answer2"),resultSet.getString("answer3"),resultSet.getString("answer4")};
			Question newQuestion = new Question(resultSet.getString("text"),answerArr,resultSet.getString("trueanswer"),resultSet.getString("explanation"));
			questionList.add(newQuestion);
		}
		it = questionList.listIterator();
		index = 0;
		if(it.hasNext()) {
			Question temp = it.next();
			System.out.println(temp.getQuestionText());
			questionNumber.setText("Question Number : " + Integer.toString(questionNum));
			question.setText(temp.getQuestionText());
			answer1.setText(temp.getAnswers()[0]);
			answer2.setText(temp.getAnswers()[1]);
			answer3.setText(temp.getAnswers()[2]);
			answer4.setText(temp.getAnswers()[3]);


		}

	}

	public void scoring() throws ClassNotFoundException, SQLException {
		Connection c = null;
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);

		double questionScore = 100/questionList.size();
		double grade = 0;
		int index = 0;
		String query = "insert into answer (text, quizid, studentid, questionNum, istrue) values (?,?,?,?,?)";
		for (Question q : questionList) {
			PreparedStatement preparedStmt = c.prepareStatement(query);
			preparedStmt.setString(1, studentAnswers.get(index));
			preparedStmt.setInt(2, StudentDisplayNewQuizzesController.activeQuizID);
			preparedStmt.setInt(3, Main.activeUserID);
			preparedStmt.setInt(4, index);
			if (q.getTrueAnswer().equals(studentAnswers.get(index))) {
				grade += questionScore;
				preparedStmt.setBoolean(5, true);
			}
			else {
				preparedStmt.setBoolean(5, false);
			}
			preparedStmt.execute();
			index++;
		}

		query = "update quizstudent set score = (?) where studentid = (?) and quizid = (?)";
		PreparedStatement preparedStmt = c.prepareStatement(query);
		preparedStmt.setInt(1, (int)grade);
		preparedStmt.setInt(2, Main.activeUserID);
		preparedStmt.setInt(3, StudentDisplayNewQuizzesController.activeQuizID);
		preparedStmt.execute();
		System.out.println(StudentDisplayNewQuizzesController.activeQuizID);
		
		/////////
		QuizCache.CleanCache();
		QuizCache.loadCache(Main.activeUserID);
		/////////
	}

	public void next(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {

		if(answer1.isSelected())
			studentAnswer = answer1.getText();
		else if (answer2.isSelected())
			studentAnswer = answer2.getText();
		else if (answer3.isSelected())
			studentAnswer = answer3.getText();
		else if (answer4.isSelected())
			studentAnswer = answer4.getText();

		studentAnswers.add(questionNum-1, studentAnswer);

		if(nextQuestion.getText().equals("Finish"))
		{	
			scoring();
			questionNum = 1;
			questionList = new ArrayList<Question>();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/studentMainMenu.fxml"));
			Parent signUp = loader.load();
			Scene signUprez = new Scene(signUp);
			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

			signUp.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					xOffset = event.getSceneX();
					yOffset = event.getSceneY();
				}
			});
			//set mouse drag
			signUp.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					window.setX(event.getScreenX() - xOffset);
					window.setY(event.getScreenY() - yOffset);
				}
			});
			window.setScene(signUprez);
			window.show();
		}

		if(it.hasNext()) {
			Question temp = it.next();

			if(control.equals("back"))
			{
				temp=it.next();
			}

			System.out.println(temp.getQuestionText());
			questionNumber.setText("Question Number : " + Integer.toString(++questionNum));
			question.setText(temp.getQuestionText());
			answer1.setText(temp.getAnswers()[0]);
			answer2.setText(temp.getAnswers()[1]);
			answer3.setText(temp.getAnswers()[2]);
			answer4.setText(temp.getAnswers()[3]);
			control = "next";
		}

		if(!it.hasNext())
			nextQuestion.setText("Finish");




	}


	public void back() {

		if(answer1.isSelected())
			studentAnswer = answer1.getText();
		else if (answer2.isSelected())
			studentAnswer = answer2.getText();
		else if (answer3.isSelected())
			studentAnswer = answer3.getText();
		else if (answer4.isSelected())
			studentAnswer = answer4.getText();

		studentAnswers.add(questionNum-1, studentAnswer);

		if(it.hasPrevious()) {

			Question temp = it.previous();

			if(control.equals("next")) 
			{
				temp=it.previous();
			}



			questionNumber.setText("Question Number : " +Integer.toString(--questionNum));
			question.setText(temp.getQuestionText());
			answer1.setText(temp.getAnswers()[0]);
			answer2.setText(temp.getAnswers()[1]);
			answer3.setText(temp.getAnswers()[2]);
			answer4.setText(temp.getAnswers()[3]);
			nextQuestion.setText("Next");
			control = "back";

		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		username.setText(Main.activeName);
		try {
			questionList();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
