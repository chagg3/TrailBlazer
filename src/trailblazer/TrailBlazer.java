/* Class by Borna Houmani-Farahani
 * Creates frame and cardlayout onto which panels are placed and switched between
 *   
 * ICS4U
 * Ms. Strelkovska
 * 
 * 1/17/17
 */

package trailblazer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class TrailBlazer extends JFrame  
{
	
	private JPanel cardPanel;
    private JPanel mainMenu, levelSelect, levelCreator, gamePlay;
    private CardLayout cardLayout = new CardLayout();

    public TrailBlazer()
    {
    	super();
    	
    	cardPanel = new JPanel();
    	cardPanel.setLayout(cardLayout);
        
    	//adding different menus to cardpanel
    	//passes TrailBlazer class in so that each card can switch between panels on its own
    	levelSelect = new LevelSelect(this);
        cardPanel.add(levelSelect, "level");
        
    	mainMenu = new MainMenu(this);
        cardPanel.add(mainMenu, "main");

        levelCreator = new LevelCreator(this);
        cardPanel.add(levelCreator, "create");

        add(cardPanel);
        changeCard("main");//start game with main menu
    }

    public void newLevel(String k, boolean custom)//method adds new level to cardpanel and switches to it
    {
    	gamePlay = new GamePlay(k, this, custom);
    	cardPanel.add(gamePlay, "2");
    }
    public void newLevel(String k)
    {
    	newLevel(k, false);
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