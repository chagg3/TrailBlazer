/* Class by Borna Houmani-Farahani
 * Used to create instances of enemies
 *   
 * ICS4U
 * Ms. Strelkovska
 * 
 * 1/17/17
 */

package trailblazer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Enemy 
{
	private int x, y;
	private int originX, originY;
	private int hSpeed, vSpeed;
	private BufferedImage texture;
	final private int maxVSpeed = 20;

	private int gravForce = 1;
	
	public Enemy(int x, int y, BufferedImage texture)
	{
		originX = this.x = x;
		originY = this.y = y;
		this.texture = texture;
		
		hSpeed = 2;
	}
	//draws enemy in appropriate orientation and position
	public void draw(int mapX, int mapY, Graphics g, boolean anim)//drawing appropriate sprite
	{
		if (hSpeed > 0)
		{
			if (anim)
				g.drawImage(texture, mapX + x, mapY + y, mapX + x + 38, mapY+y+38, 38,0,0,38,null);
			else
				g.drawImage(texture, mapX + x, mapY + y, mapX + x + 38, mapY+y+38, 38,38,0,76,null);
		}
		else
		{
			if (anim)
				g.drawImage(texture, mapX + x, mapY + y, mapX + x + 38, mapY+y+38, 0,0,38,38,null);
			else
				g.drawImage(texture, mapX + x, mapY + y, mapX + x + 38, mapY+y+38, 0,38,38,76,null);
		}
	}
	public void draw(int mapX, int mapY, Graphics g)//triggered when game is paused, stops sprites from switching
	{
		draw(mapX, mapY, g, true);
	}
	public void gravity()
	{
		vSpeed += gravForce;
		if (vSpeed >= maxVSpeed)
	    	vSpeed = maxVSpeed;
	}
	public void travel(ArrayList<ArrayList<Integer>> intMap, int mapX, int mapY)
	{
		gravity();//apply gravity
		
		//if predicted location collides with a solid block or enemy is about to walk off edge, turn around
		Rectangle xPredict = new Rectangle(mapX + x + hSpeed, mapY + y, 38, 38);
		Rectangle yPredict = new Rectangle(mapX + x, mapY + y + vSpeed, 38, 38);
		
		Rectangle cornerPredict;//detects when enemy is about to walk off edge
		if (hSpeed > 0)
			cornerPredict = new Rectangle(mapX + x + 38, mapY + y + 38, 38, 38);
		else
			cornerPredict = new Rectangle(mapX + x -38, mapY + y + 38, 38, 38);

		Rectangle compare;
		
		boolean flip = false;
		boolean bot = false;

		
		for (int i = 0; i < intMap.size(); i++)
			for (int j = 0; j < intMap.get(i).size(); j++)
				if (intMap.get(i).get(j) <= 87)
				{
					compare = new Rectangle(mapX + j*48, mapY + i*48, 48, 48);
					
					if (yPredict.intersects(compare))//stops enemy from falling through block
						vSpeed= 0;
					if (xPredict.intersects(compare))//stops enemy from walking through wall
						flip = true;
					if (cornerPredict.intersects(compare))//stops enemy from walking off edge
						bot = true;
				}
		
		if (flip || !bot)//flip speed if required
			hSpeed *= -1;
		
		x += hSpeed;
		y += vSpeed;
	
	}
	public Rectangle getRectangle(int mapX, int mapY)
	{
		return new Rectangle(mapX + x, mapY + y, 38, 38);
	}
	public void respawn()
	{
		x = originX;
		y = originY;
	}
	
	
}
