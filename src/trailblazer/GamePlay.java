package trailblazer;

import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.*;

public class GamePlay extends JPanel implements KeyListener, ActionListener
{
	private Timer time;
	private TrailBlazer tb;
	
	private int h = (int)(Math.random() * 1000);
	public GamePlay(int k, TrailBlazer tb)
	{
		this.tb = tb;
		setFocusable(true);

		addKeyListener(this);
		
		
		time = new Timer(17,this);
		time.start();
		
		
	    this.addComponentListener( new ComponentAdapter() {
	        public void componentShown( ComponentEvent e ) {
	            GamePlay.this.requestFocusInWindow();
	        }
	    });
	    
	}
	public void actionPerformed(ActionEvent arg0) 
	{
		System.out.println(h);
	}
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		g.drawLine(h, 0, 1024, 576);
	}
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			time.stop();
			tb.removeLevel();
		}
		
	}
	public void keyReleased(KeyEvent e) 
	{
		
	}
	public void keyTyped(KeyEvent e) {}
	
	
	
}
