package trailblazer;

import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.*;

public class GamePlay extends JPanel implements KeyListener, ActionListener
{
	private Timer time;
	public GamePlay()
	{
		setFocusable(true);

		addKeyListener(this);
		time= new Timer(17,this);
		time.start();
		
		
	    this.addComponentListener( new ComponentAdapter() {
	        public void componentShown( ComponentEvent e ) {
	            GamePlay.this.requestFocusInWindow();
	        }
	    });
	    
	}
	public void actionPerformed(ActionEvent arg0) 
	{
		
	}
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		g.drawLine(0, 0, 1024, 576);
	}
	public void keyPressed(KeyEvent e) 
	{
		System.out.println(0);
		TrailBlazer.changeCard(1);
	}
	public void keyReleased(KeyEvent e) 
	{
	}
	public void keyTyped(KeyEvent e) {}
	
	public void endLevel()
	{
		
	}
}
