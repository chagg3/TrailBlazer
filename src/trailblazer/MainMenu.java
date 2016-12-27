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

	public MainMenu()
	{
		setFocusable(true);

		optionSprites = new BufferedImage[8];
		optionButtons = new JButton[4];

		loadSprites();
		
		this.setLayout(null);
		
		
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
	public void mouseEntered(MouseEvent e) {
		for (int i = 0; i < optionButtons.length; i++)
		{
			if (e.getSource() == optionButtons[i])
			{
				optionButtons[i].setIcon(new ImageIcon(optionSprites[i*2+1]));
			}
		}

	}
	public void mouseExited(MouseEvent e) {
		for (int i = 0; i < optionButtons.length; i++)
		{
			if (e.getSource() == optionButtons[i])
			{
				optionButtons[i].setIcon(new ImageIcon(optionSprites[i*2]));
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < optionButtons.length; i++)
		{
			if (e.getSource() == optionButtons[0])
			{
				TrailBlazer.changeCard(2);
			}
			if (e.getSource() == optionButtons[3])
			{
				System.exit(0);
			}
		}
	}
	
	
	public void loadSprites()
	{
		try{
			background = ImageIO.read(getClass().getClassLoader().getResource("mmbackground.png"));
			optionImage = ImageIO.read(getClass().getClassLoader().getResource("options.png"));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		title = optionImage.getSubimage(0, 0, 322, 50);
		
		int [] optionWidth = {100,260,290,88};
		
		if (optionImage == null) System.out.print(true);
		
		for (int i = 0; i < 8; i++)
		{
			optionSprites[i] = optionImage.getSubimage(0, 52 + 36*i, optionWidth[i/2], 36);
		}
		
	}
}
