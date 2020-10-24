package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import application.*;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StudentDisplayNewQuizzesController implements Initializable {

	//screen offsets
	private double xOffset = 0;
	private double yOffset = 0;

	//db connection variables
	Connection c = null;
	ResultSet resultSet = null;
	static int activeQuizID = 0;

	@FXML
	private ListView<String> quizzes;

	@FXML
	private Text username;

	@SuppressWarnings("rawtypes")
	private ObservableList quizList = FXCollections.observableArrayList(); 

	@FXML
	private ImageView backLabel = new ImageView();

	@FXML
	private Label exitLabel = new Label();

	@FXML
	private ImageView logoutImage = new ImageView();

	@FXML
	private TextField quizTitle = new TextField();

	@FXML
	private Button solveButton = new Button();

	public void closePage()
	{
		System.exit(0);
	}

	public void back(MouseEvent event) throws IOException
	{
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

	public void solve(ActionEvent event) throws IOException, SQLException, ClassNotFoundException
	{

		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
		String sql = "SELECT id FROM quiz where title = (?)" ;
		PreparedStatement preparedStmt = c.prepareStatement(sql);
		if (!quizTitle.getText().equals("")) {
			preparedStmt.setString(1, quizTitle.getText());	
			resultSet = preparedStmt.executeQuery();
			if(resultSet.next())
				activeQuizID = resultSet.getInt("id");

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/studentSolveQuiz.fxml"));
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
		else {
			Alert pwdAlert = new Alert(AlertType.ERROR);
			pwdAlert.setTitle("Error");
			pwdAlert.setHeaderText("Input Error");
			pwdAlert.setContentText("Please choose a quiz to solve!");
			pwdAlert.showAndWait();
		}
	}

	@SuppressWarnings("unchecked")
	public void listQuiz() throws ClassNotFoundException, SQLException, QueueEmpty
	{
		////////
		for (int Quizid : QuizCache.getUnsolvedQuizID()) {
			PrototypePattern.Quiz newQuiz = QuizCache.getQuiz(Quizid);
			quizList.add(newQuiz.getQuizName());
		}
		////////
		quizzes.getItems().addAll(quizList);
	}
	@FXML
	public void displaySelected(MouseEvent event) {
		String selectedQuizName = quizzes.getSelectionModel().getSelectedItem();
		quizTitle.setText(selectedQuizName);
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		username.setText(Main.activeName);
		try {
			listQuiz();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (QueueEmpty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
