package agrar.io.model;

import agrar.io.util.Utility;

public class Score {

	private int score;
	private String name;
	private String password;
	private byte[] pwdHash;

	public Score(int s, String n, String p) {
		this.score = s;
		this.name = n;
		this.password = p;
	}

	public String getName() {
		return this.name;
	}

	public String getPassword() {
		return this.password;
	}

	public int getScore() {
		return this.score;
	}

	public byte[] getPasswordHash() {
		if (pwdHash == null) {
			pwdHash = Utility.getHash(this.password);
		}
		return pwdHash;
	}
}
