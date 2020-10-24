package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import FactoryPattern.*;
import application.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPageController implements Initializable {

	private double xOffset = 0;
	private double yOffset = 0;

	@FXML
	private RadioButton teacherCheck;
	@FXML
	private RadioButton studentCheck;

	@FXML
	private Label exitLabel;

	@FXML
	private TextField enteredEmail;
	@FXML
	private PasswordField enteredPassword;

	@FXML
	private AnchorPane rootPane;

	public void closePage()
	{
		System.exit(0);
	}

	@FXML
	private Text signUpStudentext;

	public void studentSignUpPage(MouseEvent event) throws IOException 
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/RegisterPage.fxml"));
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

	public void teacherSignUpPage(MouseEvent event) throws IOException 
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/RegisterTeacherPage.fxml"));
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
	public void login(MouseEvent event) throws ClassNotFoundException, SQLException, IOException
	{
		boolean checkTeacher = teacherCheck.isSelected();
		boolean checkStudent = studentCheck.isSelected();
		if(checkTeacher && !checkStudent)
			loginTeacher(event);
		else if (!checkTeacher && checkStudent)
			loginStudent(event);
		else
			System.out.println("ll");
	}
	public void loginStudent(MouseEvent event) throws ClassNotFoundException, SQLException, IOException
	{
		String email = enteredEmail.getText();
		String pwd = enteredPassword.getText();

		boolean loginStatus = Student.login(email, pwd);
		if(loginStatus) 
		{
			Main.isStudent = true;
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


	}
	public void loginTeacher(MouseEvent event) throws ClassNotFoundException, SQLException, IOException
	{
		String email = enteredEmail.getText();
		String pwd = enteredPassword.getText();

		boolean loginStatus = Teacher.login(email, pwd);
		if(loginStatus) 
		{
			Main.isStudent = false;
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/teacherMainMenu.fxml"));
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}


}
