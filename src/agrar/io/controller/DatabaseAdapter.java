package agrar.io.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import agrar.io.model.Score;
import agrar.io.util.Utility;

@SuppressWarnings("unused") // TODO remove

public class DatabaseAdapter {

	Connection conn;

	/**
	 * Get the 5 highest scoring players from the DB
	 * 
	 * @return An array of 5 (or less) Score objects representing the highest
	 *         scores
	 * @throws SQLException
	 */
	public Score[] getHighscores() throws SQLException {

		Score[] scores = new Score[5];
		int count = 0;

		scores[0] = new Score(1234, "Horst", "aöldkfjasdlödkfj");
		scores[1] = new Score(222, "Peter", "löksdjflöasdkfjk");
		scores[2] = new Score(199, "Hans", "lökjlkkjölkjk");
		scores[3] = new Score(99, "Deine Mudda", "öksjfölkasjflökakj");
		scores[4] = new Score(1, "Flo", "lökjsödflkjaslökjf");

		return scores;
		/*
		 * if (!isConnected()) { connect(); }
		 * 
		 * PreparedStatement stmt = conn .prepareStatement(
		 * "SELECT score, name FROM Highscores ORDER BY score DESC LIMIT 5");
		 * ResultSet results = stmt.executeQuery();
		 * 
		 * while (results.next()) { scores[count] = new
		 * Score(results.getInt("score"), results.getString("name"), "");
		 * count++; }
		 * 
		 * return scores;
		 */

	}

	/**
	 * Checks if a password entered by a user is equal to the one stored in the
	 * database for the same name
	 * 
	 * @param s
	 *            the name / password to check
	 * @return true if the password is the same
	 * @throws SQLException
	 *             When an exception occurs during the execution of the query
	 */
	public boolean checkPassword(Score s) throws SQLException {
		// Reconnect if necessary
		if (!isConnected()) {
			connect();
		}

		// Get the hash of the password from the database
		PreparedStatement stmt = conn.prepareStatement("SELECT password FROM Highscores WHERE name = ?");
		stmt.setString(1, s.getName());
		ResultSet res = stmt.executeQuery();

		// The hash of the password, retrieved from the DB
		byte[] databasePassword = res.getBytes("password");

		// Compare the hashes of the entered password and the password from the
		// DB
		return Arrays.equals(databasePassword, s.getPasswordHash());
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
	public boolean existsInDatabase(Score s) throws SQLException {

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

	/**
	 * Inserts a score into the database. If the score is already in the DB,
	 * does nothing
	 * 
	 * @param s
	 *            the score to insert
	 * @throws SQLException
	 * @throws InvalidPasswordException
	 *             When the given password is invalid
	 */
	public void insert(Score s) throws SQLException, InvalidPasswordException {

		// Reconnect if necessary
		if (!isConnected()) {
			connect();
		}

		// If the score does not yet exist in the database, we can just insert
		// it without checking the password
		if (!existsInDatabase(s)) {
			PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO Highscores (score, name, password) VALUES (?,?,?);");
			stmt.setInt(1, s.getScore());
			stmt.setString(2, s.getName());
			stmt.setBytes(3, s.getPasswordHash());

			stmt.executeUpdate();

		} else {

			// The user already exists in the DB

			// Check if the password entered by the user is identical to the one
			// in the DB
			if (checkPassword(s)) {// Password is valid

				update(s); // Update the highscore

			} else { // Password is invalid
				throw new InvalidPasswordException();
			}
		}
	}

	/**
	 * Changes the score value for a player name. This method does not check if
	 * the password is valid!
	 * 
	 * @param s
	 *            The new score
	 * @throws SQLException
	 */
	private void update(Score s) throws SQLException {

		// Reconnect if necessary
		if (!isConnected()) {
			connect();
		}

		PreparedStatement stmt = conn.prepareStatement("UPDATE Highscores SET score=? WHERE name=?;");
		stmt.setInt(1, s.getScore());
		stmt.setString(2, s.getName());

		stmt.executeQuery();

	}

	/**
	 * (Re)establishes a connection to the database
	 * 
	 * @throws SQLException
	 *             When establishing a connection fails somehow
	 */
	public void connect() throws SQLException {
		conn = DriverManager.getConnection("jdbc:mysql://192.168.103.250/Q11_S26?user=Q11_S26&password=start");
	}

	/**
	 * Checks whether the connection is closed or timed out / invalid
	 * 
	 * @return true if the connection is still usable
	 */
	public boolean isConnected() {
		try {
			return !(conn == null) && !conn.isClosed() && conn.isValid(3);
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Exception that indicates that a password is wrong
	 */
	public class InvalidPasswordException extends Exception {

		private static final long serialVersionUID = -6113477982164953412L;

	}

	/**
	 * Disconnects from the DB
	 */
	public void disconnect() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
