package PrototypePattern;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import application.Main;

public class QuizCache {
	static Connection c = null;
	static ResultSet resultSet = null;
	static ResultSet resultSetTemp = null;
	
	private static Hashtable <Integer,Quiz> quizMap = new Hashtable<Integer,Quiz>();
	private static ArrayList<Integer> unsolvedQuizID = new ArrayList<Integer>(); 
	private static ArrayList<Integer> solvedQuizID = new ArrayList<Integer>(); 

	public static Quiz getQuiz(int quizID) {
		Quiz cachedQuiz = quizMap.get(quizID);
		return (Quiz) cachedQuiz.clone();
	}
	
	public static void loadCache(int UserID) throws ClassNotFoundException, SQLException{
		
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/QuizHub", "postgres", Main.databasePassword);
		if(Main.isStudent) {
			String sql = "SELECT * FROM quizstudent where studentid = (?)";
			PreparedStatement preparedStmt = c.prepareStatement(sql);
			preparedStmt.setInt(1, UserID);
			resultSet = preparedStmt.executeQuery();
	
			String query = "SELECT * FROM quiz where id = (?)";
			while (resultSet.next()) {
				preparedStmt = c.prepareStatement(query);
				preparedStmt.setInt(1, resultSet.getInt("quizid"));
				resultSetTemp = preparedStmt.executeQuery();
				if (resultSetTemp.next()) {
					if (resultSet.getInt("score") != -1) {
						OldQuiz oldOne = new OldQuiz(resultSetTemp.getString("title"),resultSetTemp.getString("level"),true,resultSet.getInt("score"));
						oldOne.setId(resultSetTemp.getInt("id"));
						solvedQuizID.add(oldOne.getId());
						quizMap.put(oldOne.getId(), oldOne);
					}
					else {
						OldQuiz oldOne = new OldQuiz(resultSetTemp.getString("title"),resultSetTemp.getString("level"),false,-1);
						oldOne.setId(resultSetTemp.getInt("id"));
						unsolvedQuizID.add(oldOne.getId());
						quizMap.put(oldOne.getId(), oldOne);
					}
				}
			}
		}
		else {
			String query = "SELECT * FROM quiz where teacherid = (?)";
			PreparedStatement preparedStmt = c.prepareStatement(query);
			preparedStmt.setInt(1, UserID);
			resultSet = preparedStmt.executeQuery();
			while(resultSet.next()) {
				OldQuiz oldOne = new OldQuiz(resultSet.getString("title"),resultSet.getString("level"),false,-1);
				oldOne.setId(resultSet.getInt("id"));
				unsolvedQuizID.add(oldOne.getId());
				quizMap.put(oldOne.getId(), oldOne);
			}
		}
	}
	
	public static void CleanCache() {
		quizMap = new Hashtable<Integer,Quiz>();
		unsolvedQuizID = new ArrayList<Integer>();
		solvedQuizID = new ArrayList<Integer>();
	}
	
	
	public static Hashtable <Integer,Quiz> getQuizMap() {
		return quizMap;
	}
	public static void setQuizMap(Hashtable <Integer,Quiz> quizMap) {
		QuizCache.quizMap = quizMap;
	}
	public static ArrayList<Integer> getUnsolvedQuizID() {
		return unsolvedQuizID;
	}
	public static void setUnsolvedQuizID(ArrayList<Integer> unsolvedQuizID) {
		QuizCache.unsolvedQuizID = unsolvedQuizID;
	}
	public static ArrayList<Integer> getSolvedQuizID() {
		return solvedQuizID;
	}
	public static void setSolvedQuizID(ArrayList<Integer> solvedQuizID) {
		QuizCache.solvedQuizID = solvedQuizID;
	}
	
	

}
