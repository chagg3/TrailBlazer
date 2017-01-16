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
		
		if (hTexture == null)
			try{
				hTexture =  ImageIO.read(new File("bin/dart.png"));
			    AffineTransform tx = new AffineTransform();
			    tx.rotate(Math.PI/2, 0, hTexture.getHeight()/1);
			    AffineTransformOp op = new AffineTransformOp(tx,
			            AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
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
	public boolean checkCol(ArrayList<ArrayList<Character>> charMap, int mapX, int mapY)
	{
		Rectangle current = getRectangle(mapX, mapY);
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
	public Rectangle getRectangle(int mapX, int mapY)
	{
		Rectangle r;
		if (direction <= 2)
			r = new Rectangle(mapX + x + buffer, mapY + y + buffer, 17 - buffer, 7 - buffer);
		else
			r = new Rectangle(mapX + x + buffer, mapY + y + buffer, 7 - buffer, 17- buffer);
		
		return r;
	}
}
