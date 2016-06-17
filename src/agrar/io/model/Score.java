package agrar.io.model;

public class Score {

	private int score;
	private String name;
	private String password;

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
}
