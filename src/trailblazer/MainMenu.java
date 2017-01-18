/* Class by Borna Houmani-Farahani
 * Main menu panel. Start of game and hub for all other menus/options
 *   
 * ICS4U
 * Ms. Strelkovska
 * 
 * 1/17/17
 */
package trailblazer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

public class MainMenu extends JPanel implements ActionListener, MouseListener
{

	private BufferedImage background, title, optionImage;
	private BufferedImage [] optionSprites;
	
	private JButton [] optionButtons;
	
	private TrailBlazer tb;
	
	public MainMenu(TrailBlazer tb)
	{
		this.tb=tb;

		optionSprites = new BufferedImage[8];
		loadSprites();
		
		//creates menu buttons with images, adds listeners, adds them to menu in location specified
		this.setLayout(null);
		optionButtons = new JButton[4];

		for (int i = 0; i < 4; i++)
		{
			optionButtons[i] = new JButton();
			
			optionButtons[i].setIcon(new ImageIcon(optionSprites[i*2]));
			optionButtons[i].setBorderPainted(false);
			optionButtons[i].setContentAreaFilled(false); 
			optionButtons[i].setFocusPainted(false); 
			
			optionButtons[i].addActionListener(this);
			optionButtons[i].addMouseListener(this);
			
			this.add(optionButtons[i]);
			
	        Dimension size = optionButtons[i].getPreferredSize();
	        optionButtons[i].setBounds(143, 310 + 45 * i, size.width, size.height);
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background,0,0,1024,576, null);
		g.drawImage(title, 160, 250, null);
	}
	
	//MouseListener events
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	//mouseEntered and Exited change button icon when each button is hovered over
	public void mouseEntered(MouseEvent e) 
	{
		for (int i = 0; i < optionButtons.length; i++)
		{
			if (e.getSource() == optionButtons[i])
			{
				optionButtons[i].setIcon(new ImageIcon(optionSprites[i*2+1]));
			}
		}
	}
	public void mouseExited(MouseEvent e) 
	{
		for (int i = 0; i < optionButtons.length; i++)
		{
			if (e.getSource() == optionButtons[i])
			{
				optionButtons[i].setIcon(new ImageIcon(optionSprites[i*2]));
			}
		}
	}
	//when buttons are clicked
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == optionButtons[0])//switch to level select
			tb.changeCard("level");
		else if (e.getSource() == optionButtons[1])//switch to level creator
			tb.changeCard("create");

		//Charles wrote the following condition
		else if (e.getSource()==optionButtons[2]){
			File folder = new File("resources/customlevels");
			 File[] listOfFiles = folder.listFiles();
		     String[] fileNames = new String[listOfFiles.length];
			  // Iterating array of files for printing name of all files present in the directory.
			    for (int i = 0; i < listOfFiles.length; i++) {
			    	String n = listOfFiles[i].getName();
			        n = n.substring(0, n.length()-4);
			        fileNames[i]=(n);
			    }
		     String level = (String)JOptionPane.showInputDialog(
		                         tb,
		                         "Load a custom level?",
		                         "", JOptionPane.PLAIN_MESSAGE,
		                         null,
		                         fileNames,
		                         "");
		     
		     if ((level != null) && (level.length() > 0)) {
		    	 level+=".txt";
			     tb.newLevel(level, true); //create a custom game
			     tb.changeCard("2");
		     }

		}
		else if (e.getSource() == optionButtons[3])//exit
			System.exit(0);
	}
	public void loadSprites()
	{
		try{
			background = ImageIO.read(new File("bin/mmbackground.png"));
			optionImage = ImageIO.read(new File("bin/options.png"));
		}catch(Exception e){e.printStackTrace();}
		
		title = optionImage.getSubimage(0, 0, 322, 50);
		
		//put individual sprites on the sheet into an array
		int [] optionWidth = {100,260,290,88};
		
		
		for (int i = 0; i < 8; i++)
			optionSprites[i] = optionImage.getSubimage(0, 52 + 36*i, optionWidth[i/2], 36);
	}
}
