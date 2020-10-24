package FactoryPattern;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import PrototypePattern.QuizCache;
import application.*;

public interface User <P> {

	//INSERT METHOD TO BE OVERRODE.
	void insert(P person)  throws ClassNotFoundException, SQLException;

	//LOGIN METHOD.
	public static boolean login(String email , String password, String table)  throws ClassNotFoundException, SQLException {
		//New connection.
		Connection c = null;
		ResultSet resultSet = null;

		//POSTGRESQL driver 
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
		//c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", "123");

		String sql = "SELECT * FROM " +  table +" Where email = ? and password =?";
		try
		{
			PreparedStatement preparedStmt = c.prepareStatement(sql);
			preparedStmt.setString(1,email);
			preparedStmt.setString(2,password);
			resultSet = preparedStmt.executeQuery();
			if(!resultSet.next())
			{
				return false;
			}
			else 
			{
				//change active user informations.
				Main.activeName = resultSet.getString("name");
				Main.activeSurname = resultSet.getString("surname");
				Main.activeEmail = resultSet.getString("email");
				Main.activePassword = resultSet.getString("password");
				Main.activeUserID = resultSet.getInt("id");
				QuizCache.CleanCache();
				QuizCache.loadCache(Main.activeUserID);
				return true;
			}

		}
		catch (SQLException e) 
		{

			Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,e);
		}

		return false;
	}

	//DELETE METHOD.
	public  void delete();




}
