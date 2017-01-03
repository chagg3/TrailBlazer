package trailblazer;

import java.awt.Graphics;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class GamePlay extends JPanel implements KeyListener, ActionListener
{
	private Timer time;
	private TrailBlazer tb;
	private ArrayList<ArrayList<Character>> charMap;
	private Player louis;
	private int mapX, mapY;
		
	final private int spawnX = 384, spawnY = 288;

	public GamePlay(String k, TrailBlazer tb)
	{
		this.tb = tb;
		
		loadMap(k);
		louis = new Player(spawnX, spawnY);
		
		addKeyListener(this);
				
		time = new Timer(17, this);
		time.start();
		
	    this.addComponentListener( new ComponentAdapter() {
	        public void componentShown( ComponentEvent e ) {
	            GamePlay.this.requestFocusInWindow();
	        }
	    });
	    
	}
	public void actionPerformed(ActionEvent e) 
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
					g.fillRect(mapX + j * 48, mapY + i * 48, 48, 48);
			}
		}
		
		louis.draw(g);
		
	}
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			time.stop();
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
			File file = new File("C:/Users/borna/Documents/New folder/TrailBlazer/resources/test.txt");//temporary
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
						mapX = spawnX - l.indexOf('*') * 48;
						mapY = spawnY - charMap.size() * 48;
					}
				}

				charMap.add(e);
			}
			input.close();

		}catch(Exception e){e.printStackTrace();}
		System.out.println(charMap);
	}
}
