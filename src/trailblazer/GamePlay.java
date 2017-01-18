package trailblazer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePlay extends JPanel implements KeyListener, ActionListener
{

	private static final long serialVersionUID = 1L;
	
	private Timer timer;
	private Timer turretTimer;

	private TrailBlazer tb;
	private ArrayList<ArrayList<Integer>> intMap;
	private ArrayList<Turret> turrets;
	
	private ArrayList<Projectile> projectiles;
	private ArrayList<Enemy> enemies;
	
	private boolean cheat;
	private String custom;
	BufferedImage textures;
	
	private Player louis;
	private int initMapX, initMapY;
	private int mapX, mapY;
	private long deathInitTime = -1L;
		
	final private int spawnX = 384, spawnY = 288;

	public GamePlay(String k, TrailBlazer tb)
	{
		this.tb = tb;
		
		cheat = false;
		
		turrets = new ArrayList<Turret>();
		projectiles = new ArrayList<Projectile>();
		enemies = new ArrayList<Enemy>();

		
		loadMap(k);
		louis = new Player(spawnX, spawnY);
		
		mapX = initMapX;
		mapY = initMapY;
		
		addKeyListener(this);
				
		timer = new Timer(11, this);
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
	public GamePlay(String k, TrailBlazer tb, String custom)
	{
		this(k,tb);
		this.custom = custom;
	}
	public void actionPerformed(ActionEvent e) 
	{
		
		if (louis.isDead && !cheat)
		{
			  if (deathInitTime < 0) 
		            deathInitTime = System.currentTimeMillis();
			  else 
			  {
		            long currTime = System.currentTimeMillis();
		            if (currTime - deathInitTime > 750) 
		            {
		                mapX = initMapX;
		                mapY = initMapY;
		                projectiles.clear();
		                louis = new Player(spawnX, spawnY);
		                deathInitTime = -1L;
			            for (int i = 0; i < enemies.size(); i++)
			            	enemies.get(i).respawn();

		            }
			  }
		}
		else //if (!louis.isDead)
		{
			louis.isDead = false;
			if (louis.right) louis.moveR();
			else if (louis.left) louis.moveL();
			//if the player is not actively trying to move, apply friction
			if (!louis.left && !louis.right) louis.friction();
			
			louis.gravity();
			
			louis.checkCol(intMap, mapX, mapY);
		
			mapX -= louis.moveX();
			mapY -= louis.moveY();
			
			for (int i = projectiles.size()-1; i >= 0; i--)
			{
				if (projectiles.get(i).checkCol(intMap, mapX, mapY))
					projectiles.remove(i);
				else
					projectiles.get(i).travel();
			}
			for (int i = 0; i < enemies.size(); i++)
				enemies.get(i).travel(intMap, mapX, mapY);
			
			louis.checkEvents(intMap, mapX, mapY, projectiles, enemies);
		}
		
		
		repaint();
	}
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		int vLeft, vRight;
		int hLeft, hRight;
		vLeft = 0;
		hLeft = 0;
		vRight =intMap.size();
		//hRight 
		
		if (mapX/48 <= 0) hLeft = -mapX/48;
		else hLeft = 0;
		hRight = hLeft + 23;
		
		if (mapY/48 <= 0) vLeft = -mapY/48;
		else vLeft = 0;
		vRight = vLeft + 13;
		
		if (vRight > intMap.size()) vRight = intMap.size();

		for (int i = vLeft; i < vRight; i++)
		{
			if (hRight > intMap.get(i).size()) hRight = intMap.get(i).size();
			for (int j = hLeft; j < hRight; j++)
			{ 
				if (intMap.get(i).get(j) <= 87 || (intMap.get(i).get(j) >= 93 && intMap.get(i).get(j) <= 97) || intMap.get(i).get(j) >= 101 )
				{
					g.drawImage(textures, mapX + j * 48, mapY + i*48, mapX + (j+1) * 48, mapY + (i+1) * 48, 
						    ((intMap.get(i).get(j))%12)*48, ((intMap.get(i).get(j))/12)*48, 
						    ((intMap.get(i).get(j))%12+1)*48, ((intMap.get(i).get(j))/12+1)*48, null);
				}
			}
		}
		
		louis.draw(g);
		
		for (int i = 0; i < projectiles.size(); i++)
			projectiles.get(i).draw(mapX, mapY, g);
		
		if (louis.isDead && !cheat)
			for (int i = 0; i < enemies.size(); i++)
				enemies.get(i).draw(mapX, mapY, g);
		else
			for (int i = 0; i < enemies.size(); i++)
				enemies.get(i).draw(mapX, mapY, g, louis.anim);		
		
		if (cheat)
			g.drawString("CHEATING", 0, 10);
	}
	
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			endLevel();
		else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
			louis.left = true;
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
			louis.right = true;
		else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_W)
			if (!louis.inAir || cheat)
				louis.jump();
		
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
			if (louis.hover)
				endLevel();
	}
	public void keyReleased(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
			louis.left = false;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
			louis.right = false;
		if (e.getKeyCode() == KeyEvent.VK_C)
			cheat = !cheat;
	}
	public void keyTyped(KeyEvent e){}
	
	public void endLevel()
	{
		
		timer.stop();
		turretTimer.stop();
		louis.stopTimer();
		if (custom != null) tb.changeCard("main");
		tb.removeLevel();

	}
	
	public void loadMap(String k)
	{
		BufferedImage [] enemyImages = new BufferedImage[4] ;
		try {
			textures = ImageIO.read(new File("bin/BlockTextures.png"));
			enemyImages[0] = ImageIO.read(new File("bin/robbie.png"));
			enemyImages[1] = ImageIO.read(new File("bin/labenemy.png"));
			enemyImages[2] = ImageIO.read(new File("bin/louisenemy.png"));
			enemyImages[3] = ImageIO.read(new File("bin/cityenemy.png"));

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		intMap = new ArrayList<ArrayList<Integer>>();
		
        try{
            ObjectInputStream is = new ObjectInputStream(new FileInputStream("resources/customlevels/"+k));
            Map m = (Map)is.readObject();
            intMap = m.getIntGrid();
            is.close();
        }
        catch(Exception er){
            er.printStackTrace();
        }
        
        for (int i = 0; i < intMap.size(); i++)
        	System.out.println(intMap.get(i));
        		
        for (int i = 0; i < intMap.size(); i++)
        	for (int j = 0; j < intMap.get(i).size(); j++)
        	{
        		if (intMap.get(i).get(j) >= 72 && intMap.get(i).get(j) <=87)
        			turrets.add(new Turret(i, j, (intMap.get(i).get(j)-72)%4+1));
        		else if (intMap.get(i).get(j) >= 88 && intMap.get(i).get(j) <= 91)
        			enemies.add(new Enemy(j*48, i* 48, enemyImages[intMap.get(i).get(j)-88]));
        		else if (intMap.get(i).get(j) == 92)
        		{
        			initMapX = spawnX - (j) * 48;
        			initMapY = spawnY - (i) * 48;
        		}
        	}
	}
}

