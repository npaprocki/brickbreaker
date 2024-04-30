import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Timer;

import org.apache.commons.lang3.time.StopWatch;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener{

	// keeps track if the game is in progress or not
	private boolean play = false;
	
	// the players score
	private int score = 0;
	
	// keeps track of if the player won the current level
	private boolean win = false;
	private boolean loose = false;
	
	// the current level
	private int level = 1;
	
	// the hits needed to break a brick
	private static int hitsToBreak = 1;
	
	// a map that maps an int to a color. Used for coloring the bricks depending on how
	// may hits are needed to break them
	private static Map<Integer, Color> colors = new HashMap<>();
	
	// Map<String, String> users = new HashMap<>();
	
	// a map for the leaderboard. it maps a userID to a score
	//Map<String, Integer> highScores = new HashMap<>();
	List<User> highScores = new ArrayList<>();
	
	// userID of current player
	private String player;
	
	private int totalBricks = 21;
	
	private StopWatch time = new StopWatch();
	private Timer timer;
	private int delay = 8;
	
	// starting position of the player
	private int playerX = 310;
	
	// variables for the ball's x and y position
	private double ballposX;
	private double ballposY;
	
	// variables for the ball's speed in x and y direction
	private double ballXdir = -2;
	private double ballYdir = -2;
	
	// an instance of Random for making random starting location for ball
	Random rnd = new Random();
	
	// generates the bricks
	private MapGenerator map;
	
	/*
	 * constructor that takes in the current player's userID as a parameter.
	 * Sets ball position to a reasonable random position and generates the bricks.
	 */
	public Gameplay(String person) {
		player = person;
		ballposX = rnd.nextInt(20, 600);
		ballposY = rnd.nextInt(250, 300);
		map = new MapGenerator(3, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		// reads the highscores from leaderboard.txt and puts them into the map
		readHighScores(highScores);
		
		// puts some colors into the color map for different values of hitsToBreak
		colors.put(1,  Color.gray);
		colors.put(2,  Color.blue);
		colors.put(3,  Color.green);
		colors.put(4, Color.yellow);
		colors.put(5, Color.orange);
		colors.put(6,  Color.red);
		
		timer = new Timer(delay, this);
		timer.start();
	}
	
	// reads in the high scores from leaderboard.txt and puts them in the map
	public void readHighScores(List<User> scoreList) {
		String userName;
		int highScore;
		long playerTime;
		try {
			Scanner fileRead = new Scanner(new File("leaderboard.txt"));
			while (fileRead.hasNext()) {
				userName = fileRead.next();
				highScore = fileRead.nextInt();
				playerTime = fileRead.nextLong();
				scoreList.add(new User(userName, highScore, playerTime));
			}
			fileRead.close();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open file for reading");
		}
		if (scoreList.size() < 5) {
			scoreList.add(new User("John", 30, 100000));
			scoreList.add(new User("Sam", 20, 100000));
			scoreList.add(new User("Tom", 15, 100000));
			scoreList.add(new User("Greg", 10, 100000));
			scoreList.add(new User("Smith", 5, 100000));
		}
	}
	
	public void paint(Graphics g) {
		//background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		//draw map
		map.draw((Graphics2D)g);
		
		//borders
		g.setColor(Color.yellow);
		g.fillRect(0,  0,  3, 592);
		g.fillRect(0,  0,  692, 3);
		g.fillRect(689,  0,  3, 592);
		
		//scores
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("" + score, 590, 30);
		
		//current level
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Level " + level, 210, 30);
		
		//time
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString(time.toString(), 300, 30);
		
		//paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		//ball
		g.setColor(Color.yellow);
		g.fillOval((int)ballposX, (int)ballposY, 20, 20);
		
		// if the player breaks all bricks, turns win to true , stops play, stops ball,
		// shows message that the player won, and displays the current score.
		if (totalBricks <= 0) {
			win = true;
			if (!time.isSuspended()) {
				time.suspend();
			}
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You Won. Score: " + score, 260, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Continue or E to Exit", 220, 350);
		}
		
		// if ball goes off the screen (player loses), stops the game, stops ball,
		// displays message and score, and displays leaderboard
		if (ballposY > 570) {
			if (!time.isStopped()) {
				time.stop();
				if (score <= highScores.get(0).getHighScore()) {
					highScores.add(new User(player, score, time.getTime()));
				}
			}
			loose = true;
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over. Score: " + score, 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 370);
			
			// uses extra variables and stuff to update the leaderboard and display it.
			// Also writes updated leaderboard to leaderboard.txt
			// needs changed to also save player's personal best to that file
			int index = getIndex(highScores, player);
			if (index > -1) {
				highScores.get(index).setHighScore(score, time.getTime());
			} else {
				highScores.add(new User(player, score, time.getTime()));
				index = getIndex(highScores, player);
			}
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Personal Best: " + highScores.get(index).getHighScore(), 250, 340);
			
			File outFile = new File("leaderBoard.txt");
			PrintWriter output = null;
			try {
				output = new PrintWriter(outFile);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
			Collections.sort(highScores);
			
			for (int i = 0; i < highScores.size(); i++) {
				output.println(highScores.get(i).getName() + " " + highScores.get(i).getHighScore() + 
						" " + highScores.get(i).getBestTime());
			}
			output.close();
			
			//leaderboard
			g.setColor(Color.white);
			g.fillRect(450, 350 , 692 - 450, 592 - 375);
			g.setColor(Color.black);
			g.drawString("Leaderboard", 500, 375);
			for (int i = 0; i < 5; i++) {
				long iTime = highScores.get(i).getBestTime();
				String formattedTime = "";
				String minutes = new Long(iTime / 60000).toString();
				String seconds = new Long((iTime % 60000) / 1000).toString();
				String milSeconds = new Long(iTime - ((iTime / 1000) * 1000)).toString();
				formattedTime = minutes + "." + seconds + "." + milSeconds;
				g.drawString(1 + i + ". " + highScores.get(i).getName() + " " + highScores.get(i).getHighScore() +
						" " + formattedTime, 450, 400 + ((1 + i) * 25));
			}
		}
		
		g.dispose();
	}
	
	public int getIndex(List<User> list, String s) {
		int index = -1;
		for (int i = 0; i < highScores.size(); i++) {
			if (highScores.get(i).getName().equals(s)) {
				return i;
			}
		}
		return index;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// if the game is playing
		if(play) {
			if (!time.isStarted()) {
				time.start();
			}
			// if ball intersects paddle, inverts Y direction of ball to make it bounce off
			if (new Rectangle((int)ballposX, (int)ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
				ballYdir = -ballYdir;
			}
			
			// loops through all bricks
			A: for (int i = 0; i < map.map.length; i++) {
				for (int j = 0; j < map.map[0].length; j++) {
					
					// if the brick's value is > 0 (not destroyed yet), then it makes a rectangle
					// and draws brick
					if (map.map[i][j] > 0) {
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle((int)ballposX, (int)ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						// if ball hits brick, then it decreases its value by one (lessens the hits
						// needed to break it by 1)
						if (ballRect.intersects(brickRect)) {
							map.setBrickValue(map.map[i][j] - 1, i, j);
							
							// if the bricks value reaches 0 (its broken), remove brick from total
							// and add to score
							if (map.map[i][j] == 0) {
								totalBricks--;
								score += 5;
							}
							
							// if ball hits brick side, make ball bounce off side
							if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							// if ball hits brick top/bottom, make ball bounce off top/bottom
							} else {
								ballYdir = -ballYdir;
							}
							// goes back to breakpoint A if nothing else happens
							break A;
						}
					}
				}
			}
			// make ball move in its x and y direction
			ballposX += ballXdir;
			ballposY += ballYdir;
			
			// make it bounce off sides of frame it it hits them
			if(ballposX < 0) {
				ballXdir = -ballXdir;
			}
			if(ballposY < 0) {
				ballYdir = -ballYdir;
			}
			if(ballposX > 670) {
				ballXdir = -ballXdir;
			}
			
		}
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static int getHitsToBreak() {
		return hitsToBreak;
	}
	
	public static Color getColor(int i) {
		if (i > 6) {
			i = 1;
		}
		return colors.get(i);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		// if player hits right arrow, move paddle to the right
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			// if paddle hitting edge of frame, don't move it
			if(playerX >= 600) {
				playerX = 600;
			} else {
				if (!loose) {
					moveRight();
				}
			}
		}
		
		// if player hits left arrow, move paddle left
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			// if player is at edge of frame, don't move it
			if(playerX < 10) {
				playerX = 10;
			} else {
				if (!loose) {
					moveLeft();
				}
			}
		}
		
		// if player hits enter...
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			// and the game is not active...
			if (!play) {
				// and the player didn't win, then reset the game and start again
				// (the player lost and hit enter to play again)
				if (!win) {
					time = new StopWatch();	
					time.start();
					play = true;
					loose = false;
					ballposX = rnd.nextInt(20, 600);
					ballposY = rnd.nextInt(250, 300);
					ballXdir = -2;
					ballYdir = -2;
					playerX = 310;
					score = 0;
					level = 1;
					hitsToBreak = 1;
					totalBricks = 21;
					map = new MapGenerator(3, 7);
					repaint();
				
				// and the player did win, then increment level, hitsToBreak and add another row
				// of bricks. Then start game with that stuff
				} else if (win) {
					time.resume();
					win = false;
					level++;
					hitsToBreak++;
					play = true;
					ballposX = rnd.nextInt(20, 600);
					ballposY = rnd.nextInt(250, 300);
					ballXdir = -2 * ((level - 1) * 1.10);
					ballYdir = -2 * ((level - 1) * 1.10);
					playerX = 310;
					//score = 0;
					totalBricks = 21 + ((level - 1) * 7);
					map = new MapGenerator(3 + (level - 1), 7);
					repaint();
				}
			}
		}
		// if player hits E...
		if(e.getKeyCode() == KeyEvent.VK_E) {
			// and the player won, exit the JFrame
			// (the player won and hit E to exit)
			if (win) {
				System.exit(0);
			}
		}
		
	}
	
	// moves paddle to the right
	public void moveRight() {
		play = true;
		playerX += 20;
	}
	
	// moves paddle to the left
	public void moveLeft() {
		play = true;
		playerX -= 20;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}