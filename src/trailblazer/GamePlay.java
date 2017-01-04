package trailblazer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePlay extends JPanel implements KeyListener, ActionListener
{
	private Timer timer;
	private TrailBlazer tb;
	private ArrayList<ArrayList<Character>> charMap;
	private Player louis;
	private int initMapX, initMapY;
	private int mapX, mapY;
	private long deathInitTime = -1L;
		
	final private int spawnX = 384, spawnY = 288;

	public GamePlay(String k, TrailBlazer tb)
	{
		this.tb = tb;
		
		loadMap(k);
		louis = new Player(spawnX, spawnY);
		
		mapX = initMapX;
		mapY = initMapY;
		
		addKeyListener(this);
				
		timer = new Timer(17, this);
		timer.start();
		
	    this.addComponentListener( new ComponentAdapter() {
	        public void componentShown( ComponentEvent e ) {
	            GamePlay.this.requestFocusInWindow();
	        }
	    });
	    
	}
	public void actionPerformed(ActionEvent e) 
	{
		if (!louis.isDead)
		{
			if (louis.right)
			{
				louis.moveR();
			}
			if (louis.left)
			{
				louis.moveL();
			}
			if (!louis.left && !louis.right) //if the player is not actively trying to move, apply friction
				louis.friction();
			
			louis.gravity();
			
			louis.checkCol(charMap, mapX, mapY);
		
			mapX -= louis.moveX();
			mapY -= louis.moveY();
			
			louis.checkDeath(charMap, mapX, mapY);
		}
		else
		{
			  if (deathInitTime < 0) 
			  {
		            deathInitTime = System.currentTimeMillis();
			  } 
			  else 
			  {
		            long currTime = System.currentTimeMillis();
		            if (currTime - deathInitTime > 1000) 
		            {
		                mapX = initMapX;
		                mapY = initMapY;
		                louis = new Player(spawnX, spawnY);
		                deathInitTime = -1L;
		            }
			  }
		}
		repaint();
	}
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		for (int i = 0; i < charMap.size(); i++)
		{
			for (int j = 0; j < charMap.get(i).size(); j++)
			{
				if (charMap.get(i).get(j) == '1')
				{
					g.setColor(Color.DARK_GRAY);
					g.fillRect(mapX + j * 48, mapY + i * 48, 48, 48);
				}
				if (charMap.get(i).get(j) == '2')
				{
					g.setColor(Color.RED);
					g.fillRect(mapX + j * 48, mapY + i * 48, 48, 48);					
				}
			}
		}
		
		louis.draw(g);
		
	}
	
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			timer.stop();
			tb.removeLevel();
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
		{
			louis.setLeft(true);
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
		{
			louis.setRight(true);
		}
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_W)
		{
			if (!louis.inAir)
				louis.jump();
		}
	}
	public void keyReleased(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
		{
			louis.setLeft(false);
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
		{
			louis.setRight(false);
		}
	}
	public void keyTyped(KeyEvent e){}
	
	
	public void loadMap(String k)
	{
		charMap = new ArrayList<ArrayList<Character>>();
		try{
			File file = new File("bin/test.txt");//temporary
			System.out.println(file);
			Scanner input = new Scanner(file);
			while (input.hasNext())
			{
				String l = input.nextLine();
				ArrayList<Character> e = new ArrayList<Character>();
				
				for (int i = 0; i < l.length(); i++)
				{
					e.add(l.charAt(i));
					if (l.charAt(i) == '*')
					{
						initMapX = spawnX - l.indexOf('*') * 48;
						initMapY = spawnY - charMap.size() * 48;
					}
				}
				charMap.add(e);
			}
			input.close();

		}catch(Exception e){e.printStackTrace();}
		System.out.println(charMap);
	}
}
