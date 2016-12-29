package trailblazer;

import java.awt.Color;
import java.awt.Graphics;

public class Player 
{
	private boolean left, right, 
	                crouch, 
                    inAir, 
                    isDead, hasWon, 
                    displayRed, 
                    faceRight,
                    isDeadScreen, hasWonScreen, 
                    cheat;
	
	private int x, y;
	private int hSpeed = 0, ySpeed = 0;
	  
	public int acceleration = 3, friction = 1, gravForce = 1;
	private int maxHSpeed = 8, minHSpeed = -8;
	private int minYSpeed = -16, maxYSpeed = 30;
	  
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
	}
	public void gravity()
	{
		if (ySpeed >= maxYSpeed)
	    	ySpeed = maxYSpeed;
		else
			ySpeed += gravForce;
	}
	public void tick()
	{
		if (right)
		{
			moveR();
		}
		if (left)
		{
			moveL();
		}
		
		//gravity();
		if (!left && !right) //if the player is not actively trying to move, apply friction
			friction();
		x += hSpeed;
		y += ySpeed;
	}
	public void draw(Graphics g)
	{
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
