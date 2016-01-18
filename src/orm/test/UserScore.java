package orm.test;

public class UserScore {
	private long id;
	private String game;
	private String user;
	private int time;

	public UserScore() {
	}
	
	public UserScore(long id, String game, String user, int time) {
		super();
		this.id = id;
		this.game = game;
		this.user = user;
		this.time = time;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "UserScore [id=" + id + ", game=" + game + ", user=" + user + ", time=" + time + "]";
	}

}
