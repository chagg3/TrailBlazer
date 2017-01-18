/* Class by Borna Houmani-Farahani
 * Used to create instances of projectiles
 *   
 * ICS4U
 * Ms. Strelkovska
 * 
 * 1/17/17
 */
package trailblazer;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Projectile 
{
	private static BufferedImage hTexture, vTexture;
	
	private int direction;//1=left, 2=right, 3=up, 4=down
	private int x, y;
	private int speed, buffer;
	
	public Projectile(int x, int y, int direction)
	{
		this.x = x;
		this.y = y;
		this.direction = direction;	
				
		speed = 8;
		buffer = 5;

		
		if (direction % 2 == 1)
			speed*=-1;
		//loads textures if they have not been loaded yet
		if (hTexture == null)
			try{
				hTexture =  ImageIO.read(new File("bin/dart.png"));
				//rotate image to be used when darts are fired up/downwards
			    AffineTransform tx = new AffineTransform();
			    tx.rotate(Math.PI/2, 0, hTexture.getHeight()/1);
			    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			    vTexture = op.filter(hTexture, null);
			}catch(Exception e){e.printStackTrace();}
	}
	public void travel()
	{
		if (direction <= 2)
			x += speed;
		else
			y += speed;
	}
	//draws dart in appropriate orientation
	public void draw(int mapX, int mapY, Graphics g)
	{
		if (direction == 1)
			g.drawImage(hTexture, mapX + x, mapY + y, mapX + x + 17, mapY + y + 7, 17, 0, 0, 7, null);
		else if (direction == 2)
			g.drawImage(hTexture, mapX + x, mapY + y, null);
		else if (direction == 3)
			g.drawImage(vTexture, mapX + x, mapY + y + 7, mapX + x + 7, mapY + y + 31, 7, 24, 0, 0, null);
		else
			g.drawImage(vTexture, mapX + x, mapY + y - 7 , null);
	}
	//checks collision with solid objects. returns true when it has collided with something
	public boolean checkCol(ArrayList<ArrayList<Integer>> intMap, int mapX, int mapY)
	{
		Rectangle current = getRectangle(mapX, mapY);
		Rectangle compare;
		
		for (int i = 0; i < intMap.size(); i++)
			for (int j = 0; j < intMap.get(i).size(); j++)
				if (intMap.get(i).get(j)<= 87)
				{
					compare = new Rectangle(mapX + j*48, mapY + i*48, 48, 48);
					if (current.intersects(compare))
						return true;
				}

		return false;
	}
	//returns rectangle based on the orientation and position of the dart
	public Rectangle getRectangle(int mapX, int mapY)
	{
		Rectangle r;
		if (direction <= 2)
			r = new Rectangle(mapX + x, mapY + y , 17 , 7 );
		else
			r = new Rectangle(mapX + x , mapY + y , 7 , 17);
		
		return r;
	}
}
