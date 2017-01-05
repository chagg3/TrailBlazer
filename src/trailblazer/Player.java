package trailblazer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Player 
{
	private Timer timer;
	private int timerC;
	
	public boolean left, right, inAir, isDead;
	private boolean show,
					crouch, 
                    hasWon, 
                    displayRed, 
                    faceRight,
                    isDeadScreen, hasWonScreen, 
                    cheat;
	
	private int x, y;
	private int hSpeed = 0, vSpeed = 0;
	private Rectangle xPredict, yPredict;
	  
	final private Rectangle central = new Rectangle(296, 180, 96, 216);
	private int xColMod, yColMod;
	final private int sideLength = 38;
	
	final private int acceleration = 2, friction = 1, gravForce = 1;
	final private int minHSpeed = -6, maxHSpeed = 6;
	final private int minVSpeed = -20, maxVSpeed = 20;
	  
	public Player(int x, int y)
	{
		this.x = x;
		this.y = y;
		isDead = false;
		
		show = true;
		timerC = 0;
		
		timer = new Timer(125, new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
	             if (timerC<6)
	            	 show = !show;
	             else
	            	 timer.stop();
	             timerC++;

	            }
	        });
		timer.start();
	
	}
	public void moveL() 
	{
		hSpeed -= acceleration;
	    if (hSpeed < minHSpeed)
	      hSpeed = minHSpeed;
	}
	public void moveR()
	{
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
	public void checkEvents(ArrayList<ArrayList<Character>> charMap, int mapX, int mapY, ArrayList<Projectile> projectiles)
	{
		Rectangle current = new Rectangle(x, y, sideLength, sideLength);
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
	}
	public int moveX()
	{
		if (xPredict.intersects(central))
		{
			x += hSpeed + xColMod;
			return 0;
		}
		else
		{
			return hSpeed + xColMod;
		}
	}
	public int moveY()
	{
		if (yPredict.intersects(central))
		{
			y += vSpeed + yColMod;
			return 0;
		}
		else
		{
			return vSpeed + yColMod;
		}
	}
	public void draw(Graphics g)
	{
		//g.drawRect((int)central.getX(), (int)central.getY(),(int) central.getWidth(), (int)central.getHeight());
		
		//g.drawLine(1024/2, 0, 1024/2, 576);
		//g.setColor(Color.CYAN);
		//g.fillRect(x+hSpeed, y+vSpeed, sideLength, sideLength);
		
		g.setColor(Color.BLUE);
		if (isDead)
			g.setColor(Color.BLACK);
		if (show)
		g.fillRect(x, y, sideLength, sideLength);
	}
	
	public void setLeft(boolean b)
	{
		left = b;
	}
	public void setRight(boolean b)
	{
		right = b;
	}
	
}
