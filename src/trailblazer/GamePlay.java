/* Class by Borna Houmani-Farahani
 * Gameplay class is created and removed whenever a game begins and finishes
 * Handles all in-game related events
 *   
 * ICS4U
 * Ms. Strelkovska
 * 
 * 1/17/17
 */
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
	
	private Timer timer;//fps timer
	private Timer turretTimer;//determines when turrets fire darts

	private TrailBlazer tb;
	
	private ArrayList<ArrayList<Integer>> intMap;//2d int arraylist representing map
	private ArrayList<Turret> turrets;
	private ArrayList<Projectile> projectiles;
	private ArrayList<Enemy> enemies;
	
	private boolean cheat;
	private boolean custom;
	
	BufferedImage textures;
	
	private Player louis;
	private int initMapX, initMapY;
	private int mapX, mapY;
	private long deathInitTime = -1L;
		
	final private int spawnX = 384, spawnY = 288;

	public GamePlay(String k, TrailBlazer tb, boolean custom)
	{
		this.tb = tb;
		this.custom = custom;
		
		cheat = false;
		
		turrets = new ArrayList<Turret>();
		projectiles = new ArrayList<Projectile>();
		enemies = new ArrayList<Enemy>();
		
		load(k);
		louis = new Player(spawnX, spawnY);
		
		mapX = initMapX;
		mapY = initMapY;
		
		addKeyListener(this);
				
		timer = new Timer(15, this);//new frame every 15 milliseconds
		timer.start();
		
		//turrets fire every 2 seconds
		turretTimer = new Timer(2000, new ActionListener(){
			 public void actionPerformed(ActionEvent e) {
				 	for (int i = 0; i < turrets.size(); i++) //spawn a projectile for each turret
				 	{
				 		projectiles.add(turrets.get(i).fire());
				 	}

	            }
	        });
		turretTimer.start();
		
		//get focus
	    this.addComponentListener( new ComponentAdapter() {
	        public void componentShown( ComponentEvent e ) {
	            GamePlay.this.requestFocusInWindow();
	        }
	    });
	 
	}

	public void actionPerformed(ActionEvent e) 
	{
		
		if (louis.getIsDead() && !cheat)
		{
			//pause game for 750 milliseconds
			//then respawn and reset player, enemies, projectiles
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
		else //player is alive, run various game functions
		{
			louis.setIsDead(false);
			
			//movement
			if (louis.getRight()) louis.moveR();
			else if (louis.getLeft()) louis.moveL();
			if (!louis.getLeft() && !louis.getRight()) louis.friction();
			louis.gravity();
			
			louis.checkCol(intMap, mapX, mapY);
		
			mapX -= louis.moveX();
			mapY -= louis.moveY();
			
			for (int i = projectiles.size()-1; i >= 0; i--)
			{
				if (projectiles.get(i).checkCol(intMap, mapX, mapY)) //if projectiles hit a wall, remove
					projectiles.remove(i);
				else 
					projectiles.get(i).travel();
			}
			for (int i = 0; i < enemies.size(); i++)
				enemies.get(i).travel(intMap, mapX, mapY);
			
			louis.checkEvents(intMap, mapX, mapY, projectiles, enemies);//check for death and win event
		}
		repaint();
	}
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		//identify the subset of the map that will be showing on the screen
		int vLeft, vRight;
		int hLeft, hRight;
		
		if (mapX/48 <= 0) hLeft = -mapX/48;
		else hLeft = 0;
		
		if (mapY/48 <= 0) vLeft = -mapY/48;
		else vLeft = 0;
		
		vRight = vLeft + 13;
		hRight = hLeft + 22;

		if (vRight >= intMap.size()) vRight = intMap.size()-1;
		if (hRight >= intMap.get(0).size()) hRight = intMap.get(0).size()-1;

		//calls recursive method to draw the subset of the array onto the screen
		paintMap(vLeft, hLeft, vRight, hRight, g);
		
		
		//draw players and various other entities
		louis.draw(g);
		
		for (int i = 0; i < projectiles.size(); i++)
			projectiles.get(i).draw(mapX, mapY, g);
		
		if (louis.getIsDead() && !cheat)
			for (int i = 0; i < enemies.size(); i++)
				enemies.get(i).draw(mapX, mapY, g);
		else
			for (int i = 0; i < enemies.size(); i++)
				enemies.get(i).draw(mapX, mapY, g, louis.getAnim());		
		
		if (cheat)//indicates with text whether you are in cheat mode
			g.drawString("CHEATING", 0, 10);
	}
	public void paintMap(int startRow, int startCol, int endRow, int endCol, Graphics g)
	{
		paintMap(startRow, startCol, startRow, startCol, endRow, endCol, g);
	}
	public void paintMap(int startRow, int startCol, int curRow, int curCol, int endRow, int endCol, Graphics g)
	{
	    if (curRow == endRow) return; //base case 
	    //if the element is to be draw, draw the corresponding image on the spritesheet
		if (intMap.get(curRow).get(curCol) <= 87 || (intMap.get(curRow).get(curCol) >= 93 && intMap.get(curRow).get(curCol) <= 99) || intMap.get(curRow).get(curCol) >= 105)
		{
			g.drawImage(textures, mapX + curCol * 48, mapY + curRow*48, mapX + (curCol+1) * 48, mapY + (curRow+1) * 48, 
				    ((intMap.get(curRow).get(curCol))%12)*48, ((intMap.get(curRow).get(curCol))/12)*48, 
				    ((intMap.get(curRow).get(curCol))%12+1)*48, ((intMap.get(curRow).get(curCol))/12+1)*48, null);
		}
		
		//move onto next row if column is complete
	    if (curCol == endCol)
	    {
	        curCol = startCol;
	        paintMap(startRow, startCol, curRow+1, curCol, endRow, endCol,g);    
	    }
	    else if (curCol != endCol)//keep moving through column if incomplete
	    {
	    	paintMap(startRow, startCol, curRow, curCol+1, endRow, endCol,g);    
	    }
	}
	//keylistener events
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			endLevel();
		else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
			louis.setLeft(true); 
		else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
			louis.setRight(true);
		else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_W)
			if (!louis.getInAir() || cheat)//allows cheating player to jump even if he is in the air
				louis.jump();
		
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
			if (louis.getHover())//if down is pressed while player is hovering over the finish line, end the level
				endLevel();
	}
	public void keyReleased(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
			louis.setLeft(false); 
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
			louis.setRight(false); 
		if (e.getKeyCode() == KeyEvent.VK_C)
			cheat = !cheat;
	}
	public void keyTyped(KeyEvent e){}
	
	public void endLevel()
	{
		timer.stop();
		turretTimer.stop();
		louis.stopTimer();
		//switch to main menu if it is a custom game, otherwise it will default switch to level select
		if (custom) tb.changeCard("main");
		tb.removeLevel();

	}
	//load serializable level and images
	public void load(String k)
	{
		BufferedImage [] enemyImages = new BufferedImage[4] ;
		try {
			textures = ImageIO.read(new File("bin/BlockTextures.png"));
			enemyImages[0] = ImageIO.read(new File("bin/robbie.png"));
			enemyImages[1] = ImageIO.read(new File("bin/labenemy.png"));
			enemyImages[2] = ImageIO.read(new File("bin/louisenemy.png"));
			enemyImages[3] = ImageIO.read(new File("bin/cityenemy.png"));

		} catch (IOException e1) {e1.printStackTrace();}
		
		//obtain 2d integer arraylist from serializable map file
		intMap = new ArrayList<ArrayList<Integer>>();
		
        try{
        	ObjectInputStream is;
        	if (custom)
        		 is = new ObjectInputStream(new FileInputStream("resources/customlevels/"+k));
        	else
        		is = new ObjectInputStream(new FileInputStream("resources/"+k));
            Map m = (Map)is.readObject();
            intMap = m.getIntGrid();
            is.close();
        } catch(Exception er){er.printStackTrace();}
        
        //run through map checking for entities
        for (int i = 0; i < intMap.size(); i++)
        	for (int j = 0; j < intMap.get(i).size(); j++)
        	{
        		//when code for certain entity is come across, add the entity at appropriate position relative to map origin
        		if (intMap.get(i).get(j) >= 72 && intMap.get(i).get(j) <=87)
        			turrets.add(new Turret(i, j, (intMap.get(i).get(j)-72)%4+1));
        		else if (intMap.get(i).get(j) >= 88 && intMap.get(i).get(j) <= 91)
        			enemies.add(new Enemy(j*48, i* 48, enemyImages[intMap.get(i).get(j)-88]));
        		else if (intMap.get(i).get(j) == 92)//set spawn at ID = 92
        		{
        			initMapX = spawnX - (j) * 48;
        			initMapY = spawnY - (i) * 48;
        		}
        	}
	}
}

