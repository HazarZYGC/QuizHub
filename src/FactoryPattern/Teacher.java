package FactoryPattern;

import application.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import PrototypePattern.NewQuiz;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Teacher  extends Person implements User<Teacher>{

	//TEACHER EXTRA INFORMATIONS.
	static public int newQuizID;
	private String serialKey = null;

	//CONSTRUCTOR
	public Teacher(String name, String surname, String gender, String birthday, String email, String password) 
	{
		super(name,surname,gender,birthday,email, password);
	}

	//OVERRIDE FROM ACCOUNT INTERFACE.
	@Override
	public void insert(Teacher newTeacher) throws ClassNotFoundException, SQLException {
		try
		{
			//New connection.
			Connection c = null;
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);

			String query = "insert into Teacher (serial_key,name,surname,password,email,gender,birthdate) values (?,?,?,?,?,?,?)";
			PreparedStatement preparedStmt = c.prepareStatement(query);

			//settting teacher informations.
			preparedStmt.setString(1,newTeacher.getSerialKey());
			preparedStmt.setString(2,newTeacher.getName());
			preparedStmt.setString(3,newTeacher.getSurname());
			preparedStmt.setString(4,newTeacher.getPassword());
			preparedStmt.setString(5,newTeacher.getEmail());
			preparedStmt.setString(6,newTeacher.getGender());
			preparedStmt.setString(7,newTeacher.getBirthday());
			preparedStmt.execute();

			//Alert for confirmation
			Alert pwdAlert = new Alert(AlertType.CONFIRMATION);
			pwdAlert.setTitle("Creating Account");
			pwdAlert.setHeaderText("Done");
			pwdAlert.setContentText("Your account created succesfully");
			pwdAlert.showAndWait();
		}
		catch(Exception e)
		{

			//Alert fot exception.
			System.err.println("Got an exception!");
			System.err.println(e.getMessage());
			Alert pwdAlert = new Alert(AlertType.ERROR);
			pwdAlert.setTitle("Database Error");
			pwdAlert.setHeaderText("Sorry for that");
			pwdAlert.setContentText("There is a database connection error, please try again later.");
			pwdAlert.showAndWait();
		}

	}

	public static boolean login(String email, String password) throws ClassNotFoundException, SQLException 
	{
		Main.isStudent = false;
		return User.login(email, password, "teacher");
	}


	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}
	public static void AddQuiz(String title, String difficulty) throws SQLException, ClassNotFoundException
	{
		@SuppressWarnings("unused")
		/////////////
		NewQuiz newQuiz = new NewQuiz(title,difficulty);
		////////////
		Connection c = null;
		ResultSet resultSet = null;
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
		String query = "insert into quiz (title,level,teacherid) values (?,?,?)";
		PreparedStatement preparedStmt = c.prepareStatement(query);
		preparedStmt.setString(1,title);
		preparedStmt.setString(2,difficulty);
		preparedStmt.setInt(3,Main.activeUserID);
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
		query = "select id from quiz where title = ?";
		preparedStmt = c.prepareStatement(query);
		preparedStmt.setString(1,title);
		resultSet = preparedStmt.executeQuery();
		if(!resultSet.next())
		{
			System.out.println("gg");
		}
		else 
		{
			newQuizID = resultSet.getInt("id");
			System.out.println(newQuizID);
		}	
	}

	public String getSerialKey() {
		return serialKey;
	}
	public void setSerialKey(String serialKey) {
		this.serialKey = serialKey;
	}
}
