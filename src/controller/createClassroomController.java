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

public class createClassroomController implements Initializable {

	//screen offsets
	private double xOffset = 0;
	private double yOffset = 0;

	private String str_studentID;
	private String str_className;
	private DynamicQueue studentqueue = new DynamicQueue();

	//db connection variables
	Connection c = null;
	ResultSet resultSet = null;

	@FXML
	ListView<String> students;

	@SuppressWarnings("rawtypes")
	ObservableList studentList = FXCollections.observableArrayList(); 

	@FXML
	ImageView backLabel = new ImageView();

	@FXML
	Label exitLabel = new Label();

	@FXML
	Text username;

	@FXML
	ImageView logoutImage = new ImageView();

	@FXML
	ScrollPane scrollPane = new ScrollPane();

	@FXML
	TextField studentID = new TextField();

	@FXML
	TextField className = new TextField();

	@FXML
	Button addStudentButton = new Button();

	@FXML
	Button addClassroomButton = new Button();

	public void addStudentToClass(MouseEvent event) {
		if (!studentID.getText().equals("")) {
			str_studentID = studentID.getText();
			studentqueue.enqueue(Integer.parseInt(str_studentID));
			studentID.setText("");
		}
		else {
			Alert pwdAlert = new Alert(AlertType.ERROR);
			pwdAlert.setTitle("Error");
			pwdAlert.setHeaderText("Input Error");
			pwdAlert.setContentText("Please choose a student to add!");
			pwdAlert.showAndWait();
		}
	}

	public void createClass(MouseEvent event) throws SQLException, QueueEmpty, ClassNotFoundException, IOException {
		if (!className.getText().equals("")) {
			try {
				str_className = className.getText();
				Classroom newClassroom = new Classroom(str_className, studentqueue);
				Class.forName("org.postgresql.Driver");
				c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
				String query = "insert into classroom (name) values (?)";
				PreparedStatement preparedStmt = c.prepareStatement(query);
				preparedStmt.setString(1, newClassroom.getName());
				preparedStmt.execute();

				query = "Select id from classroom where name = (?)";
				preparedStmt = c.prepareStatement(query);
				preparedStmt.setString(1,str_className);
				resultSet = preparedStmt.executeQuery();
				int classid = 0;
				while(resultSet.next())
				{
					classid = resultSet.getInt("id");
				}

				while (!studentqueue.isEmpty()) {
					query = "insert into classroomstudent (studentid,classid,teacherid) values (?,?,?)";
					preparedStmt = c.prepareStatement(query);
					preparedStmt.setInt(1, (int) studentqueue.dequeue());
					preparedStmt.setInt(2, classid);
					preparedStmt.setInt(3, Main.activeUserID);
					preparedStmt.execute();
				}
				className.setText("");
				displayClassroomScreen(event);
			} catch (Exception e) {
				Alert pwdAlert = new Alert(AlertType.ERROR);
				pwdAlert.setTitle("Error");
				pwdAlert.setHeaderText("Input Error");
				pwdAlert.setContentText("This class name has already exists!");
				pwdAlert.showAndWait();
			}
			
		}
		else {
			Alert pwdAlert = new Alert(AlertType.ERROR);
			pwdAlert.setTitle("Error");
			pwdAlert.setHeaderText("Input Error");
			pwdAlert.setContentText("Please enter a class name!");
			pwdAlert.showAndWait();
		}
	}

	@SuppressWarnings("unchecked")
	public void listStudents() throws ClassNotFoundException, SQLException
	{
		String table = "student";
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
		String sql = "SELECT * FROM " +  table ;
		PreparedStatement preparedStmt = c.prepareStatement(sql);
		resultSet = preparedStmt.executeQuery();



		studentList.removeAll(studentList);
		while(resultSet.next())
		{
			studentList.add(resultSet.getInt("id") + "-"  + resultSet.getString("name") + " "+  resultSet.getString("surname"));
		}
		students.getItems().addAll(studentList);
	}

	@FXML
	public void displaySelected(MouseEvent event) {
		String[] selectedStudentName = students.getSelectionModel().getSelectedItem().split("-");
		studentID.setText(selectedStudentName[0]);

	}

	public void displayClassroomScreen(MouseEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/displayClassrooms.fxml"));
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

	public void back(MouseEvent event) throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/displayClassrooms.fxml"));
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

	public void closePage()
	{
		System.exit(0);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		username.setText(Main.activeName);
		try {
			listStudents();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
