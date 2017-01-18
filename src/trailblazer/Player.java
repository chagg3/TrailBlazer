/* Class by Borna Houmani-Farahani
 * Class represents player. interacts with game through various functions
 *   
 * ICS4U
 * Ms. Strelkovska
 * 
 * 1/17/17
 */

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
	private boolean anim, show, faceRight;
	
	
	private BufferedImage spriteSheet, toolTip;
	
	//collision related variables
	private int xColMod, yColMod;
	private Rectangle xPredict, yPredict;
	final private Rectangle central = new Rectangle(296, 180, 96, 216);
	final private int sideLength = 38, buffer = 8;
	
	//position and movement related variables
	private boolean left, right, inAir, isDead, hover;

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
		hover = false;
		
		show = true;
		animTimerC = 0;
		anim = true;

		//flip animation booleans every 125 milliseconds
		animTimer = new Timer(125, new ActionListener(){ 
			 public void actionPerformed(ActionEvent e) {
	             if (animTimerC<4)//causes blinking animation when spawned in
	             {
	            	 show = !show;
		             animTimerC++;
	             }
	             anim = !anim;
	            }
	        });
		

		animTimer.start();
	
	}
	//movement methods
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
	//collision check
	public void checkCol(ArrayList<ArrayList<Integer>> intMap, int mapX, int mapY)
	{
		//creates 2 prediction rectangles because x and y movement are independent
		xPredict = new Rectangle(x + hSpeed, y, sideLength,sideLength);
		yPredict = new Rectangle(x, y + vSpeed, sideLength, sideLength);
		Rectangle compare;
		
		//modifiers that are added to movement to make collision smoother looking
		xColMod = 0;
		yColMod = 0;

		//run through map
		for (int i = 0; i < intMap.size(); i++)
			for (int j = 0; j < intMap.get(i).size(); j++)
				//check solid blocks
				if (intMap.get(i).get(j)<=55 || (intMap.get(i).get(j)>=72 && intMap.get(i).get(j)<=87))
				{
					compare = new Rectangle(mapX + j*48, mapY + i*48, 48, 48);
					if (yPredict.intersects(compare))//halts y movement and snaps player to block they are colliding with
					{
						if (vSpeed < 0)//approaching block from below
							yColMod +=(mapY + (i + 1) * 48) - y; 
						else if (vSpeed > 0)//approaching block from above
							yColMod +=(mapY + (i * 48)) - y - sideLength;
						
						inAir = false;
						vSpeed= 0;
					}
					else if (xPredict.intersects(compare))//halts x movement and snaps player to block
					{
						if (hSpeed < 0)//approaching from right
							xColMod += mapX + (j + 1) * 48 - x; 
						else if (hSpeed > 0)//approaching from left
							xColMod += mapX + j * 48 - x - sideLength;
						
						hSpeed= 0;
					}
				}
	}
	//check for winning condition and death
	public void checkEvents(ArrayList<ArrayList<Integer>> intMap, int mapX, int mapY, 
			                ArrayList<Projectile> projectiles, 
			                ArrayList<Enemy> enemies)
	{
		//determines if player is colliding with a spike or finish line block
		//buffer gives player some wiggle room. makes hitbox slightly smaller than box seen on screen
		Rectangle current = new Rectangle(x + buffer, y + buffer, sideLength-buffer, sideLength-buffer);
		Rectangle compare;
		hover = false;
		for (int i = 0; i < intMap.size(); i++)
		{
			for (int j = 0; j < intMap.get(i).size(); j++)
			{
				if (intMap.get(i).get(j) <= 71 && intMap.get(i).get(j) >= 56)//spike block
				{
					compare = new Rectangle(mapX + j*48 + buffer, mapY + i*48 + buffer, 48 - buffer, 48 - buffer);
					
					if (current.intersects(compare))
						isDead = true;
				}
				else if (intMap.get(i).get(j) >= 93 && intMap.get(i).get(j) <= 97)//finish line block
				{
					compare = new Rectangle(mapX + j*48, mapY + i*48, 48, 48);
					if (current.intersects(compare))
						hover = true;
				}
			}
		}
		//checks collision with projectiles and enemies
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
	//if player is within bounds, change position of player
	//else, move the map around the player
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
		
		//draws player in whatever state he may be in
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
		//draws hover tooltip when the player is hovering over finish line
		if (hover)
			g.drawImage(toolTip, x- 45, y-45, null);
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
	//getter methods
	public boolean getLeft(){return left;}
	public boolean getRight(){return right;}
	public boolean getInAir(){return inAir;}
	public boolean getIsDead(){return isDead;}
	public boolean getHover(){return hover;}
	public boolean getAnim(){return anim;}
	//setter methods
	public void setLeft(boolean a){left = a;} 
	public void setRight(boolean a){right = a;} 
	public void setIsDead(boolean a){isDead = a;} 
}
