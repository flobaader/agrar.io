package agrar.io.controller;

import agrar.io.model.Score;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.SwingWorker;

@SuppressWarnings("unused") //TODO remove


public class DatabaseAdapter {

	Connection conn;

	public Score[] getHighscores() throws SQLException {

		Score[] scores = new Score[5];
		int count = 0;

		if (!isConnected()) {
			connect();
		}

		PreparedStatement stmt = conn
				.prepareStatement("SELECT score, name FROM Highscores ORDER BY score DESC LIMIT 5");
		ResultSet results = stmt.executeQuery();

		while (results.next()) {
			scores[count] = new Score(results.getInt("score"), results.getString("name"), "");
			count++;
		}

		return scores;

	}

	private boolean checkPassword(Score s) {
		return false;
	}

	/**
	 * Checks if a given Score entry exits in the database (based on the primary
	 * key name)
	 * 
	 * @param s
	 *            the score to find in the database
	 * @return true if the score exists, false otherwise
	 * @throws SQLException
	 *             When the query fails in some way
	 */
	private boolean existsInDatabase(Score s) throws SQLException {

		// Reconnect if necessary
		if (!isConnected()) {
			connect();
		}

		// The name might be entered by a user, protect against SQL injection
		// with prepared statements
		PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(name) AS count FROM Highscores WHERE name = ?;");
		stmt.setString(1, s.getName());
		ResultSet results = stmt.executeQuery();
		
		// One row with the same name -> exists
		return results.getInt("count") == 1; 
	}

	public void insert(Score s) {

	}

	public static void main(String[] args) {
		try {
			new DatabaseAdapter().connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void connect() throws SQLException {
		conn = DriverManager.getConnection("jdbc:mysql://192.168.103.250/Q11_S26?user=Q11_S26&password=start");
	}

	public boolean isConnected() {
		try {
			return !conn.isClosed() && conn.isValid(3);
		} catch (SQLException e) {
			return false;
		}
	}

}
