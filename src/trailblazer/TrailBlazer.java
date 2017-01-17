package trailblazer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class TrailBlazer extends JFrame  
{

	private static final long serialVersionUID = 1L;
	
	private JPanel cardPanel;
    private JPanel mainMenu, levelSelect, levelCreator, gamePlay;
    private CardLayout cardLayout = new CardLayout();

    public TrailBlazer()
    {
    	super();
    	
    	cardPanel = new JPanel();
    	cardPanel.setLayout(cardLayout);
        
    	levelSelect = new LevelSelect(this);
        cardPanel.add(levelSelect, "level");
        
    	mainMenu = new MainMenu(this);
        cardPanel.add(mainMenu, "main");

        levelCreator = new LevelCreator(this);
        cardPanel.add(levelCreator, "create");

        add(cardPanel);
        changeCard("main");
    }
    
    public void newLevel(String k)
    {
    	gamePlay = new GamePlay(k, this);
    	cardPanel.add(gamePlay, "2");
    }
    
    
    public  void removeLevel()
    {
    	cardLayout.removeLayoutComponent(gamePlay);
    }
    
    
	public  void changeCard(String k)
	{
		cardLayout.show(cardPanel, k);
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