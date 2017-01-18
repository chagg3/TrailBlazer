/* Class by Borna Houmani-Farahani
 * Level select with buttons leading to 5 premade levels
 *    
 * ICS4U
 * Ms. Strelkovska
 * 
 * 1/17/17
 */
package trailblazer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class LevelSelect extends JPanel implements ActionListener, MouseListener
{
	private BufferedImage background, back, backRed, tokenImages;
	private BufferedImage [] tokenSprites;
	
	private JButton [] tokenButtons;
	private JButton backButton;
	
	private TrailBlazer tb;
	
	public LevelSelect(TrailBlazer tb)
	{
		this.tb = tb;
		
		tokenSprites = new BufferedImage[10];
		loadSprites();
		
		this.setLayout(null);
		
		//creates 5 level buttons with images at specific locations
		tokenButtons = new JButton[5];
		
		tokenButtons[0] = createButton(new ImageIcon(tokenSprites[0]), 380, 90);
		tokenButtons[1] = createButton(new ImageIcon(tokenSprites[2]), 565, 355);
		tokenButtons[2] = createButton(new ImageIcon(tokenSprites[4]), 220, 170);
		tokenButtons[3] = createButton(new ImageIcon(tokenSprites[6]), 320, 310);
		tokenButtons[4] = createButton(new ImageIcon(tokenSprites[8]), 615, 80);

		//creates back button
		backButton = createButton(new ImageIcon(back), -10, 530);
		//adds buttons to panel
		this.add(backButton);
		for (int i = 0; i < tokenButtons.length; i++)
			this.add(tokenButtons[i]);


	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background,0,0,1024,576, null);
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	//mouseEntered and Exited change the button icons when the mouse hovers over a button
	public void mouseEntered(MouseEvent e) 
	{
		if (e.getSource() == backButton)
			backButton.setIcon(new ImageIcon(backRed));
		else
			for (int i = 0; i < tokenButtons.length; i++)
				if (e.getSource() == tokenButtons[i])
				{
					tokenButtons[i].setIcon(new ImageIcon(tokenSprites[i*2+1]));
				}
	}
	public void mouseExited(MouseEvent e) 
	{
		if (e.getSource() == backButton)
			backButton.setIcon(new ImageIcon(back));
		else
			for (int i = 0; i < tokenButtons.length; i++)
				if (e.getSource() == tokenButtons[i])
					tokenButtons[i].setIcon(new ImageIcon(tokenSprites[i*2]));

	}
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == backButton)//main menu
			tb.changeCard("main");
		//start level when the appropriate button is pressed
		if (e.getSource() == tokenButtons[0])
		{
			tb.newLevel("LevelTwo.txt");
			tb.changeCard("2");
		}
		if (e.getSource() == tokenButtons[1])
		{
			tb.newLevel("LevelFour.txt");
			tb.changeCard("2");
		}
		if (e.getSource() == tokenButtons[2])
		{
			tb.newLevel("LevelOne.txt");
			tb.changeCard("2");
		}
		if (e.getSource() == tokenButtons[3])
		{
			tb.newLevel("LevelThree.txt");
			tb.changeCard("2");
		}
		if (e.getSource() == tokenButtons[4])
		{
			tb.newLevel("LevelFive.txt");
			tb.changeCard("2");
		}
	}
	//creates a in invisible button with an image in it at a specific position
	public JButton createButton(ImageIcon iI, int x, int y)
	{
		JButton b = new JButton();
		
		b.setIcon(iI);
		b.setBorderPainted(false);
		b.setContentAreaFilled(false); 
		b.setFocusPainted(false); 
		
		b.addActionListener(this);
		b.addMouseListener(this);
		
        Dimension size = b.getPreferredSize();
        b.setBounds(x, y, size.width, size.height);

		return b;
	}
	//load sprites from sprite sheet
	public void loadSprites()
	{
		try{
			
			background = ImageIO.read(new File("bin/levelSelect.png"));
			tokenImages = ImageIO.read(new File("bin/levelObjects.png"));

			BufferedImage options = ImageIO.read(new File("bin/options.png"));
			back = options.getSubimage(0, 340, 100, 36);
			backRed = options.getSubimage(0, 376, 100, 36);
		}catch(Exception e){e.printStackTrace();}
		
		//add images to list in order to be places on buttons later
		int [][] bounds = {{250,220},{250,120},{70,250},{250,190},{250,250}};
		for (int i = 0; i < 5; i++)
		{
			tokenSprites[i*2] = tokenImages.getSubimage(i*250, 0, bounds[i][0], bounds[i][1]);
			tokenSprites[(i+1)*2-1] = tokenImages.getSubimage(i*250, 250, bounds[i][0], bounds[i][1]);
		}
	}
}
