package trailblazer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class TrailBlazer extends JFrame  
{
    private static JPanel cardPanel;
    private JPanel mainMenu, levelSelect, gamePlay;
    private static CardLayout cardLayout = new CardLayout();

    public TrailBlazer()
    {
    	super();
    	
    	cardPanel = new JPanel();
    	cardPanel.setLayout(cardLayout);
    	
    	mainMenu = new MainMenu();
        cardPanel.add(mainMenu, "1");
        
    	gamePlay = new GamePlay();
    	cardPanel.add(gamePlay, "2");
    	
        add(cardPanel);
    }
    /*
    public void newLevel(int k)
    {
    	gamePlay = new GamePlay();
    	cardPanel.add(gamePlay, "2");
    }
    public void removeLevel()
    {
    	cardLayout.removeLayoutComponent(gamePlay);
    }
    */
	public static void changeCard(int k)
	{
		if (k==1)
			cardLayout.show(cardPanel, "1");
		else if (k==2)
			cardLayout.show(cardPanel, "2");

	}
    public static void main(String args[])
    {
        TrailBlazer app = new TrailBlazer();
    	
        app.setSize(1030,604);
        app.setLayout(new CardLayout());

        app.setVisible(true);
        app.setResizable(false);
        app.setTitle("TrailBlazer");
        app.setLocationRelativeTo(null); 
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}