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
	private static final long serialVersionUID = 1L;
	
	private BufferedImage background, back, backRed, tokenImages;
	private BufferedImage [] tokenSprites;
	private JButton [] tokenButtons;
	private JButton backButton;
	private TrailBlazer tb;
	private int save;
	
	public LevelSelect(TrailBlazer tb)
	{
		this.tb = tb;
		
		tokenSprites = new BufferedImage[10];
		loadSprites();
		
		this.setLayout(null);
		tokenButtons = new JButton[5];
		
		tokenButtons[0] = createButton(new ImageIcon(tokenSprites[0]), 380, 90);
		tokenButtons[1] = createButton(new ImageIcon(tokenSprites[2]), 565, 355);
		tokenButtons[2] = createButton(new ImageIcon(tokenSprites[4]), 220, 170);
		tokenButtons[3] = createButton(new ImageIcon(tokenSprites[6]), 320, 310);
		tokenButtons[4] = createButton(new ImageIcon(tokenSprites[8]), 615, 80);

		
		backButton = createButton(new ImageIcon(back), -10, 530);
		this.add(backButton);

		
		for (int i = 0; i < tokenButtons.length; i++)
			this.add(tokenButtons[i]);


	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background,0,0,1024,576, null);
		//g.drawImage(title, 160, 250, null);
		//g.drawImage(tokenImages.getSubimage(0, 0, 250, 250), 0,0, null);
		for (int i = 4; i < tokenSprites.length; i++)
			g.drawImage(tokenSprites[i], i*250, 0, null);

	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) 
	{
		if (e.getSource() == backButton)
			backButton.setIcon(new ImageIcon(backRed));
		else
			for (int i = 0; i < tokenButtons.length; i++)
				if (e.getSource() == tokenButtons[i])
					tokenButtons[i].setIcon(new ImageIcon(tokenSprites[i*2+1]));
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
		if (e.getSource() == backButton)
		{
			tb.changeCard("main");
		}
		if (e.getSource() == tokenButtons[2])
		{
			tb.newLevel("testgg.txt");
			tb.changeCard("2");
		}
	}
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
	public void loadSprites()
	{
		try{
			
			background = ImageIO.read(new File("bin/levelSelect.png"));
			tokenImages = ImageIO.read(new File("bin/levelObjects.png"));

			BufferedImage options = ImageIO.read(new File("bin/options.png"));
			back = options.getSubimage(0, 340, 100, 36);
			backRed = options.getSubimage(0, 376, 100, 36);
		}catch(Exception e){e.printStackTrace();}
		
		int [][] bounds = {{250,220},{250,120},{70,250},{250,190},{250,250}};
		for (int i = 0; i < 5; i++)
		{
			tokenSprites[i*2] = tokenImages.getSubimage(i*250, 0, bounds[i][0], bounds[i][1]);
			tokenSprites[(i+1)*2-1] = tokenImages.getSubimage(i*250, 250, bounds[i][0], bounds[i][1]);
		}
	}
}
