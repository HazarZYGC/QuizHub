package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import application.*;
import FactoryPattern.*;
import PrototypePattern.QuizCache;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddQuestionController implements Initializable {

	@FXML
	private ToggleGroup answers;
	private Connection c = null;
	private double xOffset = 0;
	private double yOffset = 0;

	private String textString = null;
	private String answer1String = null;
	private String answer2String = null;
	private String answer3String = null;
	private String answer4String = null;
	private String trueanswerString = null;
	private String explanationString = null;
	
	public static DynamicQueue questionQueue = new DynamicQueue();

	@FXML
	private TextArea text;

	@FXML
	private TextField answer1;

	@FXML
	private TextField answer2;

	@FXML
	private TextField answer3;

	@FXML
	private TextField answer4;

	@FXML
	private RadioButton answerOne;
	
	@FXML
	private RadioButton answerTwo;
	
	@FXML
	private RadioButton answerThree;
	
	@FXML
	private RadioButton answerFour;

	@FXML
	private TextField explanation;

	@FXML
	private Button back;

	@FXML
	private Button finish;



	public void addQuestion()
	{
		textString = text.getText();
		answer1String = answer1.getText();
		answer2String = answer2.getText();
		answer3String = answer3.getText();
		answer4String = answer4.getText();
		if (!textString.equals("") && !answer1String.equals("") && !answer2String.equals("") && !answer3String.equals("") && !answer4String.equals(""))
		{
			if(answerOne.isSelected())
				trueanswerString =  answer1.getText();
			else if(answerTwo.isSelected())
				trueanswerString =  answer2.getText();
			else if(answerThree.isSelected())
				trueanswerString =  answer3.getText();
			else if(answerFour.isSelected())
				trueanswerString =  answer4.getText();

			explanationString = explanation.getText();
			String[] answers = {answer1String,answer2String,answer3String,answer4String};
			Question newQuestion = new Question(textString,answers,trueanswerString,explanationString);
			questionQueue.enqueue(newQuestion);
		}
		else 
		{
			Alert pwdAlert = new Alert(AlertType.ERROR);
			pwdAlert.setTitle("Error");
			pwdAlert.setHeaderText("Input Error");
			pwdAlert.setContentText("Please fill question text and answers correctly!");
			pwdAlert.showAndWait();
		}
	}
	
	public void NewQuestion(MouseEvent event) throws IOException, ClassNotFoundException, SQLException {
		addQuestion();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/AddQuestion.fxml"));
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

	@FXML
	public void finish(MouseEvent event) throws IOException, ClassNotFoundException, SQLException, QueueEmpty 
	{
		queueToDatabase();
		Teacher.newQuizID = 0;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/TeacherMainMenu.fxml"));
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

	public void BackPage(MouseEvent event) throws IOException {
		questionQueue = new DynamicQueue();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/TeacherMainMenu.fxml"));
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

	public void queueToDatabase() throws SQLException, ClassNotFoundException, QueueEmpty {
		int questionNumber = 1;
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
		while(!questionQueue.isEmpty()) {
			String query = "insert into question (quizid, text, answer1, answer2, answer3, answer4, trueanswer, explanation, number) values (?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStmt = c.prepareStatement(query);
			Question temp = (Question) questionQueue.dequeue();
			preparedStmt.setInt(1,Teacher.newQuizID);
			preparedStmt.setString(2,temp.getQuestionText());
			preparedStmt.setString(3,temp.getAnswers()[0]);
			preparedStmt.setString(4,temp.getAnswers()[1]);
			preparedStmt.setString(5,temp.getAnswers()[2]);
			preparedStmt.setString(6,temp.getAnswers()[3]);
			preparedStmt.setString(7,temp.getTrueAnswer());
			preparedStmt.setString(8,temp.getSolutionExp());
			preparedStmt.setInt(9, questionNumber);
			questionNumber++;
			try {
				preparedStmt.execute();
			}
			catch(Exception e) {
				Alert pwdAlert = new Alert(AlertType.INFORMATION);
				pwdAlert.setTitle("Database Error");
				pwdAlert.setHeaderText("Dat");
				pwdAlert.setContentText("Database is not available now or your quiz informations are wrong!");
				pwdAlert.showAndWait();
			}
		}
		/////////
		QuizCache.CleanCache();
		QuizCache.loadCache(Main.activeUserID);
		/////////
		questionQueue = new DynamicQueue();
	}

	public void quit(MouseEvent event) throws ClassNotFoundException, SQLException, IOException, QueueEmpty {
		questionQueue = new DynamicQueue();
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
		String query = "delete from quiz where id = (?)";
		PreparedStatement preparedStmt = c.prepareStatement(query);
		preparedStmt.setInt(1, Teacher.newQuizID);
		preparedStmt.execute();
		Teacher.newQuizID = 0;
		BackPage(event);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

}
