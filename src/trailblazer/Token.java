package trailblazer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Token 
{
	
	private BufferedImage texture;
	private int x, y;
	final private int sideLength = 20;
	
	public Token(int x, int y, int direction)
	{
		this.x = x;
		this.y = y;
	}
	public void draw(int mapX, int mapY, Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(mapX+x + (24 - sideLength/2), mapY+y+(24 - sideLength/2), sideLength, sideLength);
	}
	public Rectangle getRectangle(int mapX, int mapY)
	{
		return new Rectangle(mapX+x + (24 - sideLength/2), mapY+y+(24 - sideLength/2), sideLength, sideLength);
	}
}
