package trailblazer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePlay extends JPanel implements KeyListener, ActionListener
{
	private Timer timer;
	private TrailBlazer tb;
	private ArrayList<ArrayList<Character>> charMap;
	private ArrayList<Turret> turrets;
	private Timer turretTimer;
	private ArrayList<Projectile> projectiles;
	private ArrayList<Enemy> enemies;

	BufferedImage jungle;
	
	private Player louis;
	private int initMapX, initMapY;
	private int mapX, mapY;
	private long deathInitTime = -1L;
		
	final private int spawnX = 384, spawnY = 288;

	public GamePlay(String k, TrailBlazer tb)
	{
		this.tb = tb;
		
		turrets = new ArrayList<Turret>();
		projectiles = new ArrayList<Projectile>();
		enemies = new ArrayList<Enemy>();

		
		loadMap(k);
		louis = new Player(spawnX, spawnY);
		
		mapX = initMapX;
		mapY = initMapY;
		
		addKeyListener(this);
				
		timer = new Timer(17, this);
		timer.start();

		
		turretTimer = new Timer(2000, new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 	for (int i = 0; i < turrets.size(); i++)
				 	{
				 		projectiles.add(turrets.get(i).fire());
				 	}

	            }
	        });
		turretTimer.start();
		
		
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
			if (louis.right) louis.moveR();
			if (louis.left) louis.moveL();
			//if the player is not actively trying to move, apply friction
			if (!louis.left && !louis.right) louis.friction();
			
			louis.gravity();
			
			louis.checkCol(charMap, mapX, mapY);
		
			mapX -= louis.moveX();
			mapY -= louis.moveY();
			
			for (int i = projectiles.size()-1; i >= 0; i--)
			{
				if (projectiles.get(i).checkCol(charMap, mapX, mapY))
					projectiles.remove(i);
				else
					projectiles.get(i).travel();
			}
			for (int i = 0; i < enemies.size(); i++)
			{
				enemies.get(i).travel(charMap, mapX, mapY);
			}
			
			louis.checkEvents(charMap, mapX, mapY, projectiles);
		}
		else
		{
			  if (deathInitTime < 0) 
		            deathInitTime = System.currentTimeMillis();
			  else 
			  {
		            long currTime = System.currentTimeMillis();
		            if (currTime - deathInitTime > 1000) 
		            {
		                mapX = initMapX;
		                mapY = initMapY;
		                projectiles.clear();
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
					g.drawImage(jungle, mapX + j * 48, mapY + i * 48, null);//(mapX + j * 48, mapY + i * 48, 48, 48);
				}
				else if (charMap.get(i).get(j) == '2')
				{
					g.setColor(Color.RED);
					g.fillRect(mapX + j * 48, mapY + i * 48, 48, 48);					
				}
				else if (charMap.get(i).get(j) >='3')
				{
					g.setColor(Color.ORANGE);
					g.fillRect(mapX + j * 48, mapY + i * 48, 48, 48);					
				}
			}
		}
		
		louis.draw(g);
		for (int i = 0; i < projectiles.size(); i++)
		{
			projectiles.get(i).draw(mapX, mapY, g);
		}
		for (int i = 0; i < enemies.size(); i++)
		{
			enemies.get(i).draw(mapX, mapY, g);
		}
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
			louis.left = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
		{
			louis.right = true;
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
			louis.left = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
		{
			louis.right = false;
		}
	}
	public void keyTyped(KeyEvent e){}
	
	
	public void loadMap(String k)
	{
		try {
			jungle = ImageIO.read(new File("bin/blocktestgrass.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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
					else if (l.charAt(i) >= '3')
					{
						turrets.add(new Turret(charMap.size(), i, l.charAt(i)-'2'));
					}
					else if (l.charAt(i) == '/')
					{
						enemies.add(new Enemy(i*48, charMap.size()* 48));
					}
				}
				charMap.add(e);
			}
			input.close();

		}catch(Exception e){e.printStackTrace();}
		System.out.println(charMap);
	}
}
