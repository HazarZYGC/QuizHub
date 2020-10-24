package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import application.Main;
import FactoryPattern.*;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class addStudentPageController implements Initializable {


	//screen offsets
	private double xOffset = 0;
	private double yOffset = 0;

	//db connection variables
	Connection c = null;
	ResultSet resultSet = null;
	ResultSet resultSetTemp = null;

	@FXML
	private ListView<String> students;

	@SuppressWarnings("rawtypes")
	private ObservableList studentList= FXCollections.observableArrayList(); 

	@FXML
	private ImageView backLabel = new ImageView();

	@FXML
	private Label exitLabel = new Label();

	@FXML
	private Text username;

	@FXML
	private ImageView logoutImage = new ImageView();

	@FXML
	private ScrollPane scrollPane = new ScrollPane();

	@FXML
	private TextField classID = new TextField();

	@FXML
	private Button addStudentButton = new Button();

	@FXML
	private Button createNewButton = new Button();

	@SuppressWarnings("unchecked")
	public void displayStudents() throws SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
		String sql = "SELECT * FROM student AS stu where NOT EXISTS (SELECT * From classroomstudent as classstu where stu.id = classstu.studentid and classstu.classid = (?))";
		PreparedStatement preparedStmt = c.prepareStatement(sql);
		preparedStmt.setInt(1, AddQuizToClassroomController.ActiveClassID);
		resultSet = preparedStmt.executeQuery();

		studentList.removeAll(studentList);
		while (resultSet.next()) {
			studentList.add(resultSet.getInt("id")+ " - " + resultSet.getString("name") + " " + resultSet.getString("surname"));
		}
		students.getItems().addAll(studentList);

	}
	
	@FXML
	public void displaySelected(MouseEvent event) {
		String selectedClassName = students.getSelectionModel().getSelectedItem();
		classID.setText(selectedClassName);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		username.setText(Main.activeName);
		try {
			displayStudents();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public void addStudent(MouseEvent event) throws IOException, ClassNotFoundException, SQLException
	{
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
			String query = "insert into classroomstudent (studentid,classid,teacherid) values (?,?,?)";
			PreparedStatement preparedStmt = c.prepareStatement(query);
			String [] splittedName = classID.getText().split(" ");
			preparedStmt.setInt(1, Integer.parseInt(splittedName[0]));
			preparedStmt.setInt(2, AddQuizToClassroomController.ActiveClassID);
			preparedStmt.setInt(3, Main.activeUserID);
			preparedStmt.execute();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/addStudentPage.fxml"));
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
		} catch (Exception e) {
			Alert pwdAlert = new Alert(AlertType.ERROR);
			pwdAlert.setTitle("Error");
			pwdAlert.setHeaderText("Input Error");
			pwdAlert.setContentText("Please choose a student to add!");
			pwdAlert.showAndWait();
		}
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
