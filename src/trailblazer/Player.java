package trailblazer;

import java.awt.*;
import java.util.ArrayList;

public class Player 
{
	public boolean left, right, 
	                crouch, 
                    inAir, 
                    isDead, hasWon, 
                    displayRed, 
                    faceRight,
                    isDeadScreen, hasWonScreen, 
                    cheat;
	
	public int x, y;
	private int hSpeed = 0, ySpeed = 0;
	  
	private double acceleration = 1, friction = 1, gravForce = 1;
	private int maxHSpeed = 8, minHSpeed = -8;
	private int minYSpeed = -20, maxYSpeed = 26;
	  
	public Player(int x, int y)
	{
		this.x = x;
		this.y = y;
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
		ySpeed = minYSpeed;
		inAir = true;
	}
	public void gravity()
	{
		if (ySpeed >= maxYSpeed)
	    	ySpeed = maxYSpeed;
		else
			ySpeed += gravForce;
	}
	public void checkXCol(ArrayList<ArrayList<Character>> charMap)
	{
		Rectangle newY = new Rectangle(x + hSpeed, y, 40,40);
		Rectangle compare;
		for (int i = 0; i < charMap.size(); i++)
		{
			for (int j = 0; j < charMap.get(i).size(); j++)
			{
				if (charMap.get(i).get(j) != '0')
				{
					compare = new Rectangle(j*48, i*48, 48, 48);
					
					if (newY.intersects(compare))
					{
						if (hSpeed < 0)
						{
							x = (j + 1)*48 ;
						}
						else if (hSpeed > 0)
						{
							x = (j - 1)*48 + 8;
						}
						hSpeed= 0;
						

					}
				}
			}
		}
	}
	public void checkYCol(ArrayList<ArrayList<Character>> charMap)
	{
		//int newY = y + ySpeed;
		Rectangle newY = new Rectangle(x, y + ySpeed, 40,40);
		Rectangle compare;
		for (int i = 0; i < charMap.size(); i++)
		{
			for (int j = 0; j < charMap.get(i).size(); j++)
			{
				if (charMap.get(i).get(j) != '0')
				{
					compare = new Rectangle(j*48, i*48, 48, 48);
					
					if (newY.intersects(compare))
					{
						if (ySpeed < 0)
						{
							y = (i + 1)*48 ;
						}
						else if (ySpeed > 0)
						{
							y = (i - 1)*48 + 8;
						}
						inAir = false;
						ySpeed= 0;
						

					}
				}
			}
		}
		
		
	}
	public void tick()
	{

		x += hSpeed;
		y += ySpeed;
	}
	public void draw(Graphics g)
	{

		g.setColor(Color.RED);
		g.fillRect(x+hSpeed, y+ySpeed, 40, 40);
		
		g.setColor(Color.BLUE);
		g.fillRect(x, y, 40, 40);
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
