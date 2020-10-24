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

public class AddQuizToClassroomController implements Initializable{
	//screen offsets
	private double xOffset = 0;
	private double yOffset = 0;

	public static int ActiveClassID = 0;
	private String str_quizID;

	//db connection variables
	Connection c = null;
	ResultSet resultSet = null;

	@FXML
	private ListView<String> quizzes;

	@SuppressWarnings("rawtypes")
	private ObservableList quizList = FXCollections.observableArrayList(); 

	@FXML
	private ImageView backLabel = new ImageView();

	@FXML
	private Text username;

	@FXML
	private Label exitLabel = new Label();

	@FXML
	private ImageView logoutImage = new ImageView();

	@FXML
	private ScrollPane scrollPane = new ScrollPane();

	@FXML
	private TextField quizID = new TextField();

	@FXML
	private Button addQuizButton = new Button();

	
	@SuppressWarnings("unchecked")
	public void displayQuizzes() {
		
		for (int Quizid : QuizCache.getUnsolvedQuizID()) {
			PrototypePattern.Quiz newQuiz = QuizCache.getQuiz(Quizid);
			quizList.add(newQuiz.getId() + "-"  + newQuiz.getQuizName());
		}
		
		quizzes.getItems().addAll(quizList);
	}

	@FXML
	public void displaySelected(MouseEvent event) {
		String[] selectedQuizName = quizzes.getSelectionModel().getSelectedItem().split("-");
		quizID.setText(selectedQuizName[0]);
	}

	public void addQuiz(MouseEvent event) throws SQLException, ClassNotFoundException {
		try {
			str_quizID = quizID.getText();
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
			String query = "insert into classroomquiz (quizid, classid) values (?,?)";
			PreparedStatement preparedStmt = c.prepareStatement(query);
			preparedStmt.setInt(1, Integer.parseInt(str_quizID));
			preparedStmt.setInt(2, AddQuizToClassroomController.ActiveClassID);
			preparedStmt.execute();
			query = "select studentid from classroomstudent where classid = (?)";
			preparedStmt = c.prepareStatement(query);
			preparedStmt.setInt(1, AddQuizToClassroomController.ActiveClassID);
			resultSet = preparedStmt.executeQuery();
			query = "insert into quizstudent (quizid,studentid,score) values (?,?,?)";
			preparedStmt = c.prepareStatement(query);
			while(resultSet.next())
			{
				preparedStmt.setInt(1, Integer.parseInt(str_quizID));
				preparedStmt.setInt(2, resultSet.getInt("studentid"));
				preparedStmt.setInt(3, -1);
				preparedStmt.execute();
			}
		} catch (Exception e) {
			Alert pwdAlert = new Alert(AlertType.ERROR);
			pwdAlert.setTitle("Error");
			pwdAlert.setHeaderText("Input Error");
			pwdAlert.setContentText("Please choose a quiz to add!");
			pwdAlert.showAndWait();
		}
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		username.setText(Main.activeName);
	    displayQuizzes();
	}

	public void closePage()
	{
		System.exit(0);
	}

	public void back(MouseEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/displayStudentsOfClass.fxml"));
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
}
