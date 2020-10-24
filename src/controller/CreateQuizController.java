package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import application.*;
import FactoryPattern.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CreateQuizController implements Initializable {
	private double xOffset = 0;
	private double yOffset = 0;

	@FXML
	private ImageView logout;
	@FXML
	private Label exitLabel;
	@FXML
	private TextField quizTitle;
	@FXML
	private TextField difficulty;
	@FXML
	private Button next;
	@FXML
	private Button back;

	@FXML
	Text username;

	private String titleString;
	private String diffString;


	public void closePage()
	{
		System.exit(0);
	}
	public void AddQuestionPage(MouseEvent event) throws IOException {
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

	public void BackPage(MouseEvent event) throws IOException {
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

	public void newQuiz(MouseEvent event) throws ClassNotFoundException, SQLException, IOException {
		titleString = quizTitle.getText();
		diffString = difficulty.getText();

		if(titleString.equals("") || diffString.equals(""))
		{
			Alert pwdAlert = new Alert(AlertType.ERROR);
			pwdAlert.setTitle("Error");
			pwdAlert.setHeaderText("Input Error");
			pwdAlert.setContentText("Re-enter quiz informations correctly!");
			pwdAlert.showAndWait();
		}
		else {
			int diff = 0;
			try {

				diff = Integer.parseInt(diffString);
			}
			catch(Exception e)
			{
				Alert pwdAlert = new Alert(AlertType.ERROR);
				pwdAlert.setTitle("YUHH");
				pwdAlert.setHeaderText("Level Error");
				pwdAlert.setContentText("Quiz difficulty must be 1 or 2 or 3 or 4 or 5!");
				pwdAlert.showAndWait();
			}
			if(diff>5 || diff<1)
			{
				{
					Alert pwdAlert = new Alert(AlertType.ERROR);
					pwdAlert.setTitle("Error");
					pwdAlert.setHeaderText("Level Error");
					pwdAlert.setContentText("Quiz difficulty must be 1 or 2 or 3 or 4 or 5!");
					pwdAlert.showAndWait();
				}
			}

			Teacher.AddQuiz(titleString, diffString);
			AddQuestionPage(event);
		}
	}

	@FXML
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		username.setText(Main.activeName);
	}
	public String getTitleString() {
		return titleString;
	}
	public void setTitleString(String titleString) {
		this.titleString = titleString;
	}
	public String getDiffString() {
		return diffString;
	}
	public void setDiffString(String diffString) {
		this.diffString = diffString;
	}



}
