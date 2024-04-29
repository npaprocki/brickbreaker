
public class User implements Comparable<User> {
	
	public String name;
	public int highScore;
	public long bestTime;

	public User() {
		this.name = "";
		this.highScore = 0;
		this.bestTime = 0;
	}
	
	public User(String name, int highScore, long bestTime) {
		this.name = name;
		this.highScore = highScore;
		this.bestTime = bestTime;
	}
	
	public String getName() {
		return this.name;
	}

	public int getHighScore() {
		return this.highScore;
	}
	
	public long getBestTime() {
		return this.bestTime;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
	public void setHighScore(int n, long t) {
		if (n > this.highScore) {
			this.highScore = n;
			this.bestTime = t;
		}
	}
	
	public void setBestTime(long n) {
		if (n < this.bestTime) {
			this.bestTime = n;
		}
	}
	
	@Override
	public int compareTo(User u) {
		if (this.highScore > u.getHighScore()) {
			return -1;
		} else if (this.highScore < u.getHighScore()) {
			return 1;
		} else {
			return 0;
		}
	}
	
}
