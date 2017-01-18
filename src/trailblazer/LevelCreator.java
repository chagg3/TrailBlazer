package trailblazer;
/*
 * Charles
 * ICS SUMMATIVE TRAIL BLAZER
 * Panel for GUI that will hold the level creator. This panel will listen to all the buttons
 * mouse, and key actions that will occur within the creator. 
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class LevelCreator extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener, ItemListener{

//declaration for needed variables and objects.	
	private JButton increaseDown, decreaseUp, increaseRight, decreaseLeft, save, load, exit;
	private JScrollPane mapScroller;
	private JTextField nameField;
	private JComboBox<String> tileType, levelLoad; 
	private JPanel imagePanel, functionPanel, saveLoadPanel, tilePanel;
	private BlockPanel blockPanel, blockPanel2, dangerPanel, entityPanel;
	private MapPanel mapPanel;
	private Dimension area;
	private CardLayout cl;
	private int blockID;
	private ImageIcon louisIcon, finishIcon;
	private TrailBlazer tb;

//constructor than passes in the main frame
	public LevelCreator(TrailBlazer tb){
		super();
		this.tb = tb;
	
	//blockID is the variable that determines which block is placed on the map
	//when a block is placed
		blockID = 0;
		
	//constructs the left portion of the levelCreator and adds listeners to things that require
	//it, includes imagePanel that holds the map, and adjustable buttons. Object sizes are
	//adjusted accordingly.
		imagePanel = new JPanel();
		area = new Dimension(661,481);
		mapPanel = new MapPanel();
		mapPanel.setPreferredSize(area);
		mapPanel.addMouseListener(this);
		mapPanel.addMouseMotionListener(this);
		mapScroller = new JScrollPane(mapPanel);
		increaseDown = new JButton("v");
		increaseDown.setPreferredSize(new Dimension(696,45));
		increaseDown.addActionListener(this);
		decreaseUp = new JButton("^");
		decreaseUp.setPreferredSize(new Dimension(696,46));
		decreaseUp.addActionListener(this);
		increaseRight = new JButton(">");
		increaseRight.setPreferredSize(new Dimension(45, 480));
		increaseRight.addActionListener(this);
		decreaseLeft = new JButton("<");
		decreaseLeft.setPreferredSize(new Dimension(45, 480));
		decreaseLeft.addActionListener(this);
		
	//adds all components of the left side to imagePanel in border layout. 	
		imagePanel.setLayout(new BorderLayout());
		imagePanel.add(mapScroller, BorderLayout.CENTER);
		imagePanel.add(increaseDown, BorderLayout.SOUTH);
		imagePanel.add(increaseRight, BorderLayout.EAST);
		imagePanel.add(decreaseUp, BorderLayout.NORTH);
		imagePanel.add(decreaseLeft, BorderLayout.WEST);
	
		
	//the right side of the level creator	
			
	
		
	//finds all of the custom levels, puts them into a string array and removes the last
	//four characters of the file name, in our case ".txt". Array is then loaded into 
	//JComboBox
		File folder = new File("resources/customlevels");
	    File[] listOfFiles = folder.listFiles();
	    String[] fileNamesShown = new String[listOfFiles.length];
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	String n = listOfFiles[i].getName();
	        n = n.substring(0, n.length()-4);
	        fileNamesShown[i]=n;
	    }
		levelLoad = new JComboBox<>(fileNamesShown);
		
	
	//constructs the bottom right of the level creator, a panel that holds all the objects
	//used for saving and loading as well as exit. It consists of two more panels, nameLoad,
	//and saveLoad, that organizes the other objects in a gridLayout.
	//Listeners are added, and sizes are adjusted accordingly.
		saveLoadPanel = new JPanel();
		saveLoadPanel.setPreferredSize(new Dimension(269,79));
		saveLoadPanel.setLayout(new GridLayout(2,1));
		save = new JButton("Save"); 
		save.addActionListener(this);
		load = new JButton("Load");
		load.addActionListener(this);
		JPanel nameLoad = new JPanel(new GridLayout(1,2));
		saveLoadPanel.add(nameLoad);
		nameField = new JTextField("DefaultFileName");
		nameLoad.add(nameField);
		nameLoad.add(levelLoad);
		JPanel saveLoad = new JPanel(new GridLayout(1,3));
		saveLoadPanel.add(saveLoad);
		saveLoad.add(save);
		saveLoad.add(load);
		exit = new JButton("Exit");
		exit.addActionListener(this);
		saveLoad.add(exit);
		
		
	//Constructs the panel where the user may select which block they are going to place.
	//Everything is put in a card layout that will switch between the panels. Selects
	//first panel shown in the card layout and adds listeners.
		cl = new CardLayout();
		tilePanel = new JPanel();
		blockPanel = new BlockPanel(0);
		blockPanel.addMouseListener(this);
		blockPanel2 = new BlockPanel(28);
		blockPanel2.addMouseListener(this);
		dangerPanel = new BlockPanel(56);
		dangerPanel.addMouseListener(this);
		entityPanel = new BlockPanel(84);
		entityPanel.addMouseListener(this);
	//JCombobox that will help user select the different panels, item listener is added so
	//panel will switch when user changes item in combo box.
		String[] type = {"Blocks","More Blocks","Dangers", "Dangers and Special"};
		tileType = new JComboBox<>(type);
		tileType.addItemListener(this);
		tilePanel.setLayout(cl);
		tilePanel.add(blockPanel, "1");
		tilePanel.add(blockPanel2, "2");
		tilePanel.add(dangerPanel, "3");
		tilePanel.add(entityPanel, "4");
		cl.show(tilePanel, "1");
		
	//Constructs panel that holds everything on the right side of the level creator	in
	//border layout
		functionPanel = new JPanel();
		functionPanel.setPreferredSize(new Dimension(269,576));
		functionPanel.setLayout(new BorderLayout());
		functionPanel.add(tileType, BorderLayout.NORTH);
		functionPanel.add(tilePanel, BorderLayout.CENTER);
		functionPanel.add(saveLoadPanel, BorderLayout.SOUTH);
	
	//adds the left side and the right side of the level creator togther.		
		setLayout(new BorderLayout());	
		add(imagePanel, BorderLayout.CENTER);
		add(functionPanel, BorderLayout.EAST);
	
	}
	
//action events for buttons
	@Override
	public void actionPerformed(ActionEvent e) {
	//changes back to the main menu when level creator is excited by switching the cards
		if (e.getSource().equals(exit))
			tb.changeCard("main");
	//when the buttons that manipulate the map are increased.	
		if(e.getSource().equals(increaseRight)||e.getSource().equals(increaseDown)||e.getSource().equals(decreaseLeft)||e.getSource().equals(decreaseUp)){
		
	//calls methods of the map that will either increase/decrease the width/height 		
			if(e.getActionCommand().equals("v")){
				mapPanel.map.addRow();
			}
			else if(e.getActionCommand().equals(">")){
				mapPanel.map.addColumn();
			}
			else if(e.getActionCommand().equals("^")){
				mapPanel.map.removeRow();
			}
			else if(e.getActionCommand().equals("<")){
				mapPanel.map.removeColumn();
			}
		
			
		//changes area of the map panel, by taking into account number of rows and columns are 
		//in the current loaded map. Scrollbar is then updated so that appears if map is larger
		//than panel, and disappears when it is the same size.
			area.width = (mapPanel.map.getColumns()*30+1);
	        area.height = (mapPanel.map.getRows()*30+1);       
			mapPanel.setPreferredSize(area);
			mapPanel.revalidate();
			
			repaint();
		}
		
	//when the save or load button is pressed	
		if(e.getSource().equals(save)||e.getSource().equals(load)){
		
		//when save is pressed, number of player blocks are counted and number of finish blocks
		//are counted, sends out appropriate error message.
			if(e.getSource().equals(save)){
			
				int start = 0;
				int finish = 0;
				
				for(int i = 0; i < mapPanel.map.getRows(); i++){
					for(int j = 0; j < mapPanel.map.getColumns();j++){
						int id = mapPanel.map.getGrid().get(i).get(j).getID();
						if (id == 92)
							start++;
						if(93 <= id && id <= 97)
							finish++;
					}
				}
				
			//loads image icons for error messages	
				if(louisIcon == null)
					louisIcon = new ImageIcon("bin/louisIcon.png");
				if(finishIcon == null)
					finishIcon = new ImageIcon("bin/finishIcon.png");
				
				if(start > 1){
					popUp(louisIcon, "You cannot have more than one player on your level.",
							"Cannot Save.");
				}
				else if (start == 0){
					popUp(louisIcon, "You must have one player on your level.", "Cannot Save.");
				}
				else if(finish == 0){
					popUp(finishIcon, "You must have at least one finish point on your level.",
							"Cannot Save.");
				}
			
			//when player has required blocks to save, new file is created by getting 
			//the current entry in the name text field and adding ".txt".
				else{
					
					
					String fileName = nameField.getText()+".txt";
					File newFile = new File("resources/customlevels/"+fileName);
					
				//boolean for if file already exists, and int overwrite for if user wishes to 
				//overwrite an already existing file
					 boolean exists = false;
					 int overwrite = 0;
				
				//checks each item in jcombobox and compares it with string in name field
				//to find out wheter file already exists, if it does not, add name to
				//combobox and serialize the map
					 for (int index = 0; index < levelLoad.getItemCount() && !exists; index++) {
					   if (nameField.getText().equals(levelLoad.getItemAt(index))) {
					     exists = true;
					   }
					 }
					 if (!exists) {
					   levelLoad.addItem(fileName.substring(0,fileName.length()-4));
					 }
				//get user input to overwrite if file already exists 
					 else{
							overwrite = JOptionPane.showConfirmDialog(tb,
									    "This file exists, would you like to overwrite it?",
									    "Overwrite?",
									    JOptionPane.YES_NO_OPTION);
						 }
					 
					 if(overwrite == 0){	 
						//Serializing the map object
						try{
							ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(newFile));
							os.writeObject(mapPanel.map);
							os.close();
						}
						catch(Exception er){
							er.printStackTrace();
						}
					}			
				}
			}
		
	//when file is loaded, reads name of file from jcombobox and unserializes the file back into
	//a map which is then loaded into the mapPanel, it is then repainted and readjusted.
		if(e.getSource().equals(load)){
			
			String level = levelLoad.getSelectedItem().toString()+".txt";
	
	        try{
	        	ObjectInputStream is = new ObjectInputStream(new FileInputStream("resources/customlevels/"+level));
	        	Map m = (Map)is.readObject();
	        	mapPanel.map = m;
				is.close();
			}
	        catch(Exception er){
	        	er.printStackTrace();
	        }
	        area.width = mapPanel.map.getColumns()*30+1;
	        area.height = mapPanel.map.getRows()*30+1;
	        mapPanel.setPreferredSize(area);
	        mapPanel.revalidate();
	        repaint();
			}
		}
		
	}
	
//calls on mouse pressed	
	@Override	
	public void mouseDragged(MouseEvent e) {
		mousePressed(e);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	//gets the x and y of the mouse	
		int x = e.getX();
		int y = e.getY();
	
	//if mouse is on map panel, first makes sure mouse is within the mapPanel range, as if mouse
	//is dragged and leaves the panel, will not get correct spot on map. 
		if(e.getSource().equals(mapPanel)){
			if(x/30>=mapPanel.map.getColumns()||x/30<0||y/30>=mapPanel.map.getRows()||y/30<0){
			}
		//gets position of row and column of mouse on map by dividing x and y by size of the 
		//blocks. replaces block with a blank if mouse is right clicked and with current 
		//blockID if it is left clicked.
			else{
				if(SwingUtilities.isRightMouseButton(e)){
					mapPanel.map.changeBlock(x/30,y/30, new Block());
				}
				else{
					mapPanel.map.changeBlock(x/30,y/30, new Block(blockID));
				}
			}
		}
		
		
	//updates the block panels and blockIDs.
		if(e.getSource().equals(blockPanel)){
			blockID = updateID(blockPanel, x, y);
		}
		
		if(e.getSource().equals(blockPanel2)){
			blockID = updateID(blockPanel2, x, y);
		}
		
		if(e.getSource().equals(dangerPanel)){
			blockID = updateID(dangerPanel, x, y);
		}
		
		if(e.getSource().equals(entityPanel)){
			blockID = updateID(entityPanel, x, y);
		}
		
		repaint();
		}
	

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

//when different block panel is selected from combo box, changes panel, and updates blockID
	@Override
	public void itemStateChanged(ItemEvent e) {
			if(tileType.getSelectedItem().equals("Blocks")){
				cl.show(tilePanel, "1");
				blockID = updateID(blockPanel);
			}
			else if (tileType.getSelectedItem().equals("More Blocks")){
				cl.show(tilePanel, "2");
				blockID = updateID(blockPanel2);
			}
			else if (tileType.getSelectedItem().equals("Dangers")){
				cl.show(tilePanel, "3");
				blockID = updateID(dangerPanel);
			}
			else if(tileType.getSelectedItem().equals("Dangers and Special")){
				cl.show(tilePanel, "4");
				blockID = updateID(entityPanel);
			}
	}
	
	public void popUp(ImageIcon icon, String error, String title){
		JOptionPane.showMessageDialog(tb, error, title, JOptionPane.ERROR_MESSAGE, icon);
	}

	public int updateID(BlockPanel bp, int x, int y){
		bp.setSelect(x/67*67, y/67*67);
		return bp.getBlock(x/67,y/67).getID();
	}

//overloaded method that will select and update block id to the first block of the panel
	public int updateID(BlockPanel bp){
		return updateID(bp,0,0);
	}
	
}

//jpanel that the map is drawn and which size is dynamic so that a jscroll pane can be used
class MapPanel extends JPanel{
	
	Map map;

	MapPanel(){
		super();
		map = new Map();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		map.draw(g);
	}
}

//panel that contains block selections
class BlockPanel extends JPanel{
	
	private Block[][] blockImages;
	private Dimension select;
	
	BlockPanel(int id){
		super();
		select = new Dimension(0,0);
		
 		blockImages = new Block[7][4];
 		
 		for(int c =id, i = 0; i < 7; i++){
 			for(int j = 0; j<4; j++, c++){
 				blockImages[i][j] = new Block(c);
 			}
 		}
	}
	
//set position of red outline
	public void setSelect(int x, int y){
		select.setSize(x, y);;
	}
	
	public Block getBlock(int x, int y){
		return blockImages[y][x];
	}
	
//draws blocks, as well as grid, and red outline for currently selected block	
	public void paintComponent(Graphics g){
		for(int i = 0; i < 7; i++){
 			for(int j = 0; j<4; j++){
 				blockImages[i][j].drawBlock(g, j, i, 67);
 				g.drawRect(j*67, i*67, 67, 67);
 			}
 		}
		g.setColor(Color.RED);
		g.draw3DRect(select.width, select.height, 67, 67, true);
	}
}
