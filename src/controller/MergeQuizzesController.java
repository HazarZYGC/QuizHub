package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import application.DynamicQueue;
import application.Main;
import application.Question;
import application.QueueEmpty;
import FactoryPattern.*;
import PrototypePattern.QuizCache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MergeQuizzesController implements Initializable {

	//screen offsets
	private double xOffset = 0;
	private double yOffset = 0;

	//db connection variables
	Connection c = null;
	ResultSet resultSet = null;
	ResultSet resultSetTemp = null;

	@FXML
	private ImageView backLabel = new ImageView();

	@FXML
	Text username;

	@FXML
	private Label exitLabel = new Label();

	@FXML
	private ImageView logoutImage = new ImageView();

	@FXML
	private ListView<String> Quizzes1;

	@FXML
	private ListView<String> Quizzes2;

	@FXML
	private TextField firstQuizName = new TextField();

	@FXML
	private TextField secondQuizName = new TextField();

	@FXML
	private TextField newQuizName = new TextField();

	@FXML
	private Button merge = new Button();

	@FXML
	private ScrollPane scrollPane1 = new ScrollPane();

	@FXML
	private ScrollPane scrollPane2 = new ScrollPane();

	private DynamicQueue questions = new DynamicQueue();



	@SuppressWarnings("rawtypes")
	ObservableList quizList = FXCollections.observableArrayList();

	public void closePage()
	{
		System.exit(0);
	}

	public void back(MouseEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/EditQuizPage.fxml"));
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

	@SuppressWarnings("unchecked")
	public void displayQuizzes() 
	{
		for (int Quizid : QuizCache.getUnsolvedQuizID()) {
			PrototypePattern.Quiz newQuiz = QuizCache.getQuiz(Quizid);
			quizList.add(newQuiz.getQuizName());
		}
		Quizzes1.getItems().addAll(quizList);
		Quizzes2.getItems().addAll(quizList);
	}

	@FXML
	public void displaySelectedFirst(MouseEvent event) {
		String selectedClassName = Quizzes1.getSelectionModel().getSelectedItem();
		firstQuizName.setText(selectedClassName);

	}

	@FXML
	public void displaySelectedSecond(MouseEvent event) {
		String selectedClassName = Quizzes2.getSelectionModel().getSelectedItem();
		secondQuizName.setText(selectedClassName);

	}


	public void mergeQuizzes(ActionEvent event) throws SQLException, ClassNotFoundException, QueueEmpty, IOException
	{
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
		String sql = "SELECT * FROM question WHERE quizid IN (SELECT id FROM quiz WHERE title = (?))";
		PreparedStatement preparedStmt = c.prepareStatement(sql);
		preparedStmt.setString(1, firstQuizName.getText());
		resultSet = preparedStmt.executeQuery();
		String answers[] = new String[4];
		while (resultSet.next()) 
		{
			answers[0] = resultSet.getString("answer1");
			answers[1] = resultSet.getString("answer2");
			answers[2] = resultSet.getString("answer3");
			answers[3] = resultSet.getString("answer4");
			Question newQuestion = new Question(resultSet.getString("text"),answers,resultSet.getString("trueanswer"),resultSet.getString("explanation"));
			questions.enqueue(newQuestion);
		}
		sql = "SELECT * FROM question WHERE quizid IN (SELECT id FROM quiz WHERE title = (?))";
		preparedStmt = c.prepareStatement(sql);
		preparedStmt.setString(1, secondQuizName.getText()); 
		resultSetTemp = preparedStmt.executeQuery();
		while(resultSetTemp.next())
		{
			answers[0] = resultSetTemp.getString("answer1");
			answers[1] = resultSetTemp.getString("answer2");
			answers[2] = resultSetTemp.getString("answer3");
			answers[3] = resultSetTemp.getString("answer4");
			Question newQuestion = new Question(resultSetTemp.getString("text"),answers,resultSetTemp.getString("trueanswer"),resultSetTemp.getString("explanation"));
			questions.enqueue(newQuestion);
		}

		if (!firstQuizName.getText().equals("") && !secondQuizName.getText().equals("")) {
			String query = "insert into quiz (title,level, teacherid) values (?,?,?)";
			preparedStmt = c.prepareStatement(query);
			if(!newQuizName.getText().equals(""))
				preparedStmt.setString(1, newQuizName.getText());
			else
				preparedStmt.setString(1, firstQuizName.getText() + " - " + secondQuizName.getText());

			preparedStmt.setString(2, "3");
			preparedStmt.setInt(3, Main.activeUserID);
			preparedStmt.execute();

			query = "SELECT MAX(id) AS max FROM quiz WHERE teacherid = (?)";
			preparedStmt = c.prepareStatement(query);
			preparedStmt.setInt(1,  Main.activeUserID);
			resultSet = preparedStmt.executeQuery();

			int newID = -1;
			if(resultSet.next())
				newID = resultSet.getInt("max");

			int questionNumber = 1;
			while(!questions.isEmpty()) {
				query = "insert into question (quizid, text, answer1, answer2, answer3, answer4, trueanswer, explanation, number) values (?,?,?,?,?,?,?,?,?)";
				preparedStmt = c.prepareStatement(query);
				Question temp = (Question) questions.dequeue();
				preparedStmt.setInt(1,newID);
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
					System.out.println("loooool");
				}
			}
			//////////
			QuizCache.CleanCache();
			QuizCache.loadCache(Main.activeUserID);
			/////////
			questions = new DynamicQueue();
		}
		else {
			Alert pwdAlert = new Alert(AlertType.ERROR);
			pwdAlert.setTitle("Error");
			pwdAlert.setHeaderText("Input Error");
			pwdAlert.setContentText("Please choose quizzes to merge!");
			pwdAlert.showAndWait();
		}

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/MergeQuizzes.fxml"));
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		username.setText(Main.activeName);
		displayQuizzes();
	}


	public DynamicQueue getQuestions() {
		return questions;
	}

	public void setQuestions(DynamicQueue quizzes) {
		this.questions = quizzes;
	}

}
