import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
	public int map[][];
	public int brickWidth;
	public int brickHeight;
	
	/*
	 * constructor that takes in dimensions of bricks to be added. It also calls
	 * getHitsToBreak from Gameplay Class to set the bricks value to value of hitsToBreak
	 */
	public MapGenerator(int row, int col) {
		map = new int[row][col];
		for (int i = 0; i < map.length; i++ ) {
			for (int j = 0; j < map[0].length; j++) {
				//map[i][j] = 1;
				map[i][j] = Gameplay.getHitsToBreak();
			}
		}
		brickWidth = 540 / col;
		brickHeight = 150 / row;
	}
	
	// for every brick, this gets the color from the Map of colors in Gameplay Class, draws the bricks
	// that color, and gives them a black outline (so that they don't look connected)
	public void draw(Graphics2D g) {
		for (int i = 0; i < map.length; i++ ) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] > 0) {
					//g.setColor(Color.white);
					g.setColor(Gameplay.getColor(map[i][j]));
					g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
					
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.black);
					g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
				}
			}
		}
	}
	
	// sets brick values
	public void setBrickValue(int value, int row, int col) {
		map[row][col] = value;
	}
	
}
