package agrar.io.controller;

import agrar.io.model.Score;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAdapter {

	public Score[] getHighscores() {
		return null;
	}

	private boolean checkPassword(Score s) {
		return false;
	}

	private boolean existsInDatabase(Score s) {
		return false;
	}

	public void insert(Score s) {

	}

	public void connect() throws SQLException {
		// User: Q11_S26
		// Password: Tomatoffel12

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn = DriverManager
				.getConnection("jdbc:mysql://services.lmgu.de?user=Q_11&password=Tomatoffel12");

		Statement st = conn.createStatement();

		ResultSet resultSet = st.executeQuery("select * from feedback.comments");

		while(resultSet.next()){
			System.out.println(resultSet.getInt("score"));
		}
	}

	public boolean isConnected() {
		return false;
	}
	
	public static void main(String[] args) {
		try {
			new DatabaseAdapter().connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
