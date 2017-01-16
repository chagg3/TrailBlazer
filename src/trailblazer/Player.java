package trailblazer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Player 
{
	//animation related variables
	private Timer animTimer;
	private int animTimerC;
	public boolean anim, show, faceRight;
	
	public boolean left, right, inAir, isDead, hover;
	
	private BufferedImage spriteSheet, toolTip;
	
	//collision related variables
	private int xColMod, yColMod;
	private Rectangle xPredict, yPredict;
	final private Rectangle central = new Rectangle(296, 180, 96, 216);
	final private int sideLength = 38, buffer = 6;
	
	//position and movement related variables
	private int x, y;
	private int hSpeed = 0, vSpeed = 0;
	final private int acceleration = 2, friction = 1, gravForce = 1;
	final private int minHSpeed = -6, maxHSpeed = 6;
	final private int minVSpeed = -20, maxVSpeed = 26;
	  
	public Player(int x, int y)
	{
		this.x = x;
		this.y = y;
		loadImages();
		
		faceRight = true;
		isDead = false;
		
		show = true;
		animTimerC = 0;
		anim = true;

		animTimer = new Timer(100, new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
	             if (animTimerC<6)
	             {
	            	 show = !show;
		             animTimerC++;
	             }
	             anim = !anim;
	            }
	        });
		

		animTimer.start();
	
	}
	public void moveL() 
	{
		faceRight = false;
		hSpeed -= acceleration;
	    if (hSpeed < minHSpeed)
	      hSpeed = minHSpeed;
	}
	public void moveR()
	{
		faceRight = true;
		hSpeed += acceleration;
	    if (hSpeed > maxHSpeed)
	      hSpeed = maxHSpeed;
	}
	public void friction()//tend hSpeed towards 0
	{
		if (hSpeed > 0) 
	      hSpeed -= friction;
	    else if (hSpeed < 0) 
	      hSpeed += friction;
	}
	public void jump()
	{
		vSpeed = minVSpeed;
		inAir = true;
	}
	public void gravity()
	{
		vSpeed += gravForce;
		if (vSpeed >= maxVSpeed)
	    	vSpeed = maxVSpeed;
		inAir = true;
	}
	public void checkCol(ArrayList<ArrayList<Character>> charMap, int mapX, int mapY)
	{
		xPredict = new Rectangle(x + hSpeed, y, sideLength,sideLength);
		yPredict = new Rectangle(x, y + vSpeed, sideLength, sideLength);
		Rectangle compare;
		
		xColMod = 0;
		yColMod = 0;

		for (int i = 0; i < charMap.size(); i++)
		{
			for (int j = 0; j < charMap.get(i).size(); j++)
			{
				if (charMap.get(i).get(j) == '1' || charMap.get(i).get(j) >='3')
				{
					compare = new Rectangle(mapX + j*48, mapY + i*48, 48, 48);
					if (yPredict.intersects(compare))
					{
						if (vSpeed < 0)
							yColMod +=(mapY + (i + 1) * 48) - y; 
						else if (vSpeed > 0)
							yColMod +=(mapY + (i * 48)) - y - sideLength;
						
						inAir = false;
						vSpeed= 0;
					}
					else if (xPredict.intersects(compare))
					{
						
						if (hSpeed < 0)
							xColMod += mapX + (j + 1) * 48 - x; 
						else if (hSpeed > 0)
							xColMod += mapX + j * 48 - x - sideLength;
						
						hSpeed= 0;
					}
				}
			}
		}
	}
	
	public void checkEvents(ArrayList<ArrayList<Character>> charMap, int mapX, int mapY, 
			                ArrayList<Projectile> projectiles, 
			                ArrayList<Enemy> enemies, 
			                Token token)
	{
		Rectangle current = new Rectangle(x + buffer, y + buffer, sideLength-buffer, sideLength-buffer);
		Rectangle compare;
		
		for (int i = 0; i < charMap.size(); i++)
		{
			for (int j = 0; j < charMap.get(i).size(); j++)
			{
				if (charMap.get(i).get(j) == '2')
				{
					compare = new Rectangle(mapX + j*48, mapY + i*48, 48, 48);
					
					if (current.intersects(compare))
					{
						isDead = true;
					}
				}
			}
		}
		
		if (current.intersects(token.getRectangle(mapX, mapY)))	hover = true;
		else hover = false;
		
		for (int i = 0; i < projectiles.size(); i++)
		{
			compare = projectiles.get(i).getRectangle(mapX, mapY);
			
			if (current.intersects(compare))
				isDead = true;
		}
		
		for (int i = 0; i < enemies.size(); i++)
		{
			compare = enemies.get(i).getRectangle(mapX, mapY);
			
			if (current.intersects(compare))
				isDead = true;
		}
	}
	public int moveX()
	{
		if (xPredict.intersects(central))
		{
			x += hSpeed + xColMod;
			return 0;
		}
		return hSpeed + xColMod;
	}
	public int moveY()
	{
		if (yPredict.intersects(central))
		{
			y += vSpeed + yColMod;
			return 0;
		}
		return vSpeed + yColMod;
	}
	public void draw(Graphics g)
	{
		//g.setColor(Color.CYAN);

		//g.drawRect((int)central.getX(), (int)central.getY(),(int) central.getWidth(), (int)central.getHeight());
		//g.fillRect(x+hSpeed, y+vSpeed, sideLength, sideLength);
		
		if (show)
		{
			if (isDead)
				g.drawImage(spriteSheet, x, y, x+38, y+38 ,0,152,38,190,null);
			else if(inAir)
			{
				if (faceRight)
					g.drawImage(spriteSheet, x, y, x+38, y+38 ,38,114,0,152,null);
				else
					g.drawImage(spriteSheet, x, y, x+38, y+38 ,0,114,38,152,null);
			}
			else if (hSpeed < 0)
			{
				if (anim)
					g.drawImage(spriteSheet, x, y, x+38, y+38 ,0,38,38,76,null);
				else
					g.drawImage(spriteSheet, x, y, x+38, y+38 ,0,76,38,114,null);
			}
			else if (hSpeed > 0)
			{
				if (anim)
					g.drawImage(spriteSheet, x, y, x+38, y+38 ,38,38,0,76,null);
				else
					g.drawImage(spriteSheet, x, y, x+38, y+38 ,38,76,0,114,null);
			}
			else
			{
				if (faceRight)
					g.drawImage(spriteSheet, x, y, x+38, y+38, 38,0,0,38,null);
				else
					g.drawImage(spriteSheet, x, y, x+38, y+38 ,0,0,38,38,null);
			}
		}
		if (hover)
			g.drawImage(toolTip, x- 45, y-45, null);
	}
	
	public void setLeft(boolean b)
	{
		left = b;
	}
	public void setRight(boolean b)
	{
		right = b;
	}
	public void stopTimer()
	{
		animTimer.stop();
	}
	public void loadImages()
	{
		try {
			spriteSheet = ImageIO.read(new File("bin/louis.png"));
			toolTip = ImageIO.read(new File("bin/tooltip.png"));
		} catch (IOException e1) {e1.printStackTrace();}
	}
	
}
