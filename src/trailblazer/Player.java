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
	private int hSpeed = 0, vSpeed = 0;
	  
	final private int sideLength = 38;
	final private int acceleration = 2, friction = 1, gravForce = 1;
	final private int minHSpeed = -6, maxHSpeed = 6;
	final private int minVSpeed = -20, maxVSpeed = 26;
	  
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
		vSpeed = minVSpeed;
		inAir = true;
	}
	public void gravity()
	{
		if (vSpeed >= maxVSpeed)
	    	vSpeed = maxVSpeed;
		else
			vSpeed += gravForce;
		inAir = true;
	}
	public void checkXCol(ArrayList<ArrayList<Character>> charMap)
	{
		Rectangle newY = new Rectangle(x + hSpeed, y, sideLength,sideLength);
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
							x = (j - 1)*48 + (48 - sideLength);
						}
						hSpeed= 0;
						

					}
				}
			}
		}
	}
	public void checkYCol(ArrayList<ArrayList<Character>> charMap)
	{
		//int newY = y + vSpeed;
		Rectangle newY = new Rectangle(x, y + vSpeed, sideLength,sideLength);
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
						if (vSpeed < 0)
						{
							y = (i + 1)*48 ;
						}
						else if (vSpeed > 0)
						{
							y = (i - 1)*48 + (48 - sideLength);
						}
						inAir = false;
						vSpeed= 0;
						

					}
				}
			}
		}
		
		
	}
	public void tick()
	{
		x += hSpeed;
		y += vSpeed;
	}
	public void draw(Graphics g)
	{

		g.setColor(Color.RED);
		g.fillRect(x+hSpeed, y+vSpeed, sideLength, sideLength);
		
		g.setColor(Color.BLUE);
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
