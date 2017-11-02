package agrar.io.model;

import agrar.io.util.Utility;

/**
 * Represents a Score, combined of score, name and password
 * @author Matthias
 *
 */
public class Score {

	private int score;
	private String name;
	private byte[] pwdHash;

	public Score(int s, String n) {
		this.score = s;
		this.name = n;
	}

	public String getName() {
		return this.name;
	}

	public int getScore() {
		return this.score;
	}

}
