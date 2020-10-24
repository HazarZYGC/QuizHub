package application;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class Main extends Application {

	//ACTIVE USER INFORMATIONS.
	public static String activeName = null;
	public static String activeSurname = null;
	public static String activeEmail = null;
	public static String activePassword = null;
	public static int activeUserID = 0;
	public static String databasePassword = "123";
	public static boolean isStudent = true;

	//SCENE X-Y COORDINATES.
	private double xOffset = 0;
	private double yOffset = 0;

	//MAIN
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException 
	{
		Parent root = FXMLLoader.load(getClass().getResource("/gui/LoginPage.fxml"));

		//SCENE LONGTITUDE & LATITUDE.
		Scene scene = new Scene(root,1280,700);
		primaryStage.setScene(scene);

		//MOUSE PRESS & DRAG METHODS
		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
			}
		});
		//DRAG END.

		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
		primaryStage.setResizable(true);
	}
}
