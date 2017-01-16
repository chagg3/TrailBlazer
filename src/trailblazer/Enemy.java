package trailblazer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Enemy 
{
	private int x, y;
	private int hSpeed, vSpeed;
	final private int maxVSpeed = 20;

	private int gravForce = 1;
	
	public Enemy(int x, int y)
	{
		this.x = x;
		this.y = y;

		hSpeed = 2;
	}
	public void draw(int mapX, int mapY, Graphics g, boolean anim)
	{
		if (anim)
			g.setColor(Color.GREEN);
		else
			g.setColor(Color.orange);

		g.fillRect(mapX+x,mapY+y, 38, 38);
	}

	public void gravity()
	{
		vSpeed += gravForce;
		if (vSpeed >= maxVSpeed)
	    	vSpeed = maxVSpeed;
	}
	public void travel(ArrayList<ArrayList<Character>> charMap, int mapX, int mapY)
	{
		
		gravity();
		
		Rectangle xPredict = new Rectangle(mapX + x + hSpeed, mapY + y, 38, 38);
		Rectangle yPredict = new Rectangle(mapX + x, mapY + y + vSpeed, 38, 38);
		
		Rectangle cornerPredict;
		if (hSpeed > 0)
			cornerPredict = new Rectangle(mapX + x + 38, mapY + y + 38, 38, 38);
		else
			cornerPredict = new Rectangle(mapX + x -38, mapY + y + 38, 38, 38);

		Rectangle compare;
		
		boolean flip = false;
		boolean bot = false;

		
		for (int i = 0; i < charMap.size(); i++)
		{
			for (int j = 0; j < charMap.get(i).size(); j++)
			{
				if (charMap.get(i).get(j) == '1' || charMap.get(i).get(j) >='3')
				{
					compare = new Rectangle(mapX + j*48, mapY + i*48, 48, 48);
					
					if (yPredict.intersects(compare))
						vSpeed= 0;
					if (xPredict.intersects(compare))
						flip = true;
					if (cornerPredict.intersects(compare))
						bot = true;
						
				}
			}
		}
		
		if (flip || !bot)
			hSpeed *= -1;
		
		x += hSpeed;
		y += vSpeed;
	
	}
	public Rectangle getRectangle(int mapX, int mapY)
	{
		return new Rectangle(mapX + x, mapY + y, 38, 38);
	}
	
	
}
