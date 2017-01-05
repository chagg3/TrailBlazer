package trailblazer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Projectile 
{
	private int direction;//1=left, 2=right, 3=up, 4=down
	private int x, y;
	private int speed;
	
	public Projectile(int x, int y, int direction)
	{
		this.x = x;
		this.y = y;
		this.direction = direction;	
		
		speed = 8;
		
		if (direction % 2 == 1)
			speed*=-1;
		
	}
	public void travel()
	{
		if (direction <= 2)
			x += speed;
		else
			y += speed;
	}
	public void draw(int mapX, int mapY, Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(mapX + x, mapY + y, 6, 6);
	}
	public boolean checkCol(ArrayList<ArrayList<Character>> charMap, int mapX, int mapY)
	{
		Rectangle current = new Rectangle(mapX+x,mapY+ y, 6, 6);
		Rectangle compare;
		
		for (int i = 0; i < charMap.size(); i++)
		{
			for (int j = 0; j < charMap.get(i).size(); j++)
			{
				if (charMap.get(i).get(j) == '1' || charMap.get(i).get(j) >='3')
				{
					compare = new Rectangle(mapX + j*48, mapY + i*48, 48, 48);

					if (current.intersects(compare))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}
