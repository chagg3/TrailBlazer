package trailblazer;
/*
 * Charles
 * ICS SUMMATIVE TRAIL BLAZER
 * Panel for GUI with scrollbars that will change based on map size
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class LevelCreator extends JPanel implements KeyListener, MouseListener, MouseMotionListener, ActionListener, ItemListener{

	
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
	private TrailBlazer tb;
	
	public LevelCreator(TrailBlazer tb){
		super();
		this.tb = tb;
		
		blockID = 0;
		
		area = new Dimension(661,481);
		
		imagePanel = new JPanel();
		mapPanel = new MapPanel();
		mapPanel.setPreferredSize(area);
		mapPanel.addMouseListener(this);
		mapPanel.addMouseMotionListener(this);
		increaseDown = new JButton("v");
		increaseDown.setPreferredSize(new Dimension(696,31));
		increaseDown.addActionListener(this);
		decreaseUp = new JButton("^");
		decreaseUp.setPreferredSize(new Dimension(696,31));
		decreaseUp.addActionListener(this);
		increaseRight = new JButton(">");
		increaseRight.setPreferredSize(new Dimension(42, 480));
		increaseRight.addActionListener(this);
		decreaseLeft = new JButton("<");
		decreaseLeft.setPreferredSize(new Dimension(42, 480));
		decreaseLeft.addActionListener(this);
		mapScroller = new JScrollPane(mapPanel);
		System.out.println(imagePanel.getWidth() +" "+ imagePanel.getHeight());

		imagePanel.setLayout(new BorderLayout());
		imagePanel.add(mapScroller, BorderLayout.CENTER);
		imagePanel.add(increaseDown, BorderLayout.SOUTH);
		imagePanel.add(increaseRight, BorderLayout.EAST);
		imagePanel.add(decreaseUp, BorderLayout.NORTH);
		imagePanel.add(decreaseLeft, BorderLayout.WEST);
		
		String[] type = {"Blocks","More Blocks","Dangers", "Dangers and Special"};
		tileType = new JComboBox<>(type);
		save = new JButton("Save"); 
		save.addActionListener(this);
		load = new JButton("Load");
		load.addActionListener(this);
		File folder = new File("resources/customlevels");
	    File[] listOfFiles = folder.listFiles();
	    String[] fileNames = new String[listOfFiles.length];
	    String[] fileNamesShown = new String[listOfFiles.length];
	  // Iterating array of files for printing name of all files present in the directory.
	    for (int i = 0; i < listOfFiles.length; i++) {
	    	String n = listOfFiles[i].getName();
	    	fileNames[i]=n;
	        n = n.substring(0, n.length()-4);
	        fileNamesShown[i]=n;
	        System.out.println(n);
	    }
		levelLoad = new JComboBox<>(fileNamesShown);
		nameField = new JTextField("DefaultFileName");
		
		saveLoadPanel = new JPanel();
		saveLoadPanel.setPreferredSize(new Dimension(269,50));
		saveLoadPanel.setLayout(new GridLayout(2,1));
		JPanel nameLoad = new JPanel(new GridLayout(1,2));
		saveLoadPanel.add(nameLoad);
		nameLoad.add(nameField);
		nameLoad.add(levelLoad);
		JPanel saveLoad = new JPanel(new GridLayout(1,3));
		saveLoadPanel.add(saveLoad);
		saveLoad.add(save);
		saveLoad.add(load);
		exit = new JButton("Exit");
		exit.addActionListener(this);
		saveLoad.add(exit);
		
		
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
		tilePanel.setLayout(cl);
		tilePanel.add(blockPanel, "1");
		tilePanel.add(blockPanel2, "2");
		tilePanel.add(dangerPanel, "3");
		tilePanel.add(entityPanel, "4");
		cl.show(tilePanel, "1");
		
		functionPanel = new JPanel();
		functionPanel.setPreferredSize(new Dimension(269,576));
		functionPanel.setLayout(new BorderLayout());
		functionPanel.add(tileType, BorderLayout.NORTH);
		tileType.addItemListener(this);
		functionPanel.add(tilePanel, BorderLayout.CENTER);
		functionPanel.add(saveLoadPanel, BorderLayout.SOUTH);
		
		setLayout(new BorderLayout());	
		add(imagePanel, BorderLayout.CENTER);
		add(functionPanel, BorderLayout.EAST);
	
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(exit))
			tb.changeCard("main");
		if(e.getSource().equals(increaseRight)||e.getSource().equals(increaseDown)||e.getSource().equals(decreaseLeft)||e.getSource().equals(decreaseUp)){
			boolean changed = false;
			
			if(e.getActionCommand().equals("v")){
				System.out.println("addDown");
				mapPanel.map.addRow();
			}
			else if(e.getActionCommand().equals(">")){
				System.out.println("addRight");
				mapPanel.map.addColumn();
			}
			else if(e.getActionCommand().equals("^")){
				System.out.println("removeUp");
				mapPanel.map.removeRow();
			}
			else if(e.getActionCommand().equals("<")){
				mapPanel.map.removeColumn();
			}
			
		    int this_width = (mapPanel.map.getColumns()*30+1);
	       // if (this_width > area.width) {
	            area.width = this_width; 
	            changed=true;
	        //}
	        int this_height = (mapPanel.map.getRows()*30+1);
	        //if (this_height > area.height) {
	            area.height = this_height; 
	            changed=true;
	        //}
			
			if(changed){
				mapPanel.setPreferredSize(area);
				mapPanel.revalidate();
			}
			repaint();
		}
		
		
		if(e.getSource().equals(save)||e.getSource().equals(load)){
			
			int ybuffer = 4;
			int xbuffer = 10;
			
			if(e.getSource().equals(save)){
				
				String fileName = nameField.getText()+".txt";
				File newFile = new File("resources/customlevels/"+fileName);
				System.out.println(newFile.getAbsolutePath());
				
				try{
					newFile.createNewFile();
					FileWriter fw = new FileWriter(newFile);
					BufferedWriter bw = new BufferedWriter(fw);
					
					int rows = mapPanel.map.getRows();
					int columns =  mapPanel.map.getColumns();
					
					
				//write spikes
					for (int j = 0; j < columns+2*xbuffer+2; j++)
						bw.write(Character.toString((char)60));
					bw.newLine();
				
				//air buffer	
					for(int i = 0; i < ybuffer; i++){
						bw.write(Character.toString((char)60));
						for (int j = 0; j < columns+2*xbuffer; j++)
							bw.write(Character.toString((char)100));
						bw.write(Character.toString((char)60));
						bw.newLine();
					}
		
					for(int i = 0; i < rows; i++){
					//wall then spike then air
						bw.write(Character.toString((char)60));
						
						for(int k = 0; k < xbuffer; k++){
							bw.write(Character.toString((char)100));
						}
						
						for(int j = 0; j < columns; j++){
							char c = (char)(mapPanel.map.getGrid().get(i).get(j).getID()+1);
							bw.write(Character.toString(c));
						}
						
						for(int k = 0; k < xbuffer; k++){
							bw.write(Character.toString((char)(100)));
						}
			
						bw.write(Character.toString((char)60));
						
						bw.newLine();
					}
					
				//air buffer	
					for(int i = 0; i < ybuffer; i++){
						bw.write(Character.toString((char)60));
						for (int j = 0; j < columns+xbuffer*2; j++)
							bw.write(Character.toString((char)100));
						bw.write(Character.toString((char)60));
						bw.newLine();
					}
					
				//write spikes
					for (int j = 0; j < columns+xbuffer*2+2; j++)
						bw.write(Character.toString((char)60));
					bw.newLine();
					
					bw.close();
				}
				catch(Exception er){
					System.out.println(er);
				}
				
				boolean exists = false;
				 for (int index = 0; index < levelLoad.getItemCount() && !exists; index++) {
				   if (nameField.getText().equals(levelLoad.getItemAt(index))) {
				     exists = true;
				   }
				 }
				 if (!exists) {
				   levelLoad.addItem(fileName.substring(0,fileName.length()-4));
				 }
			/*	
				 boolean exists = false;
				 for (int index = 0; index < levelLoad.getItemCount() && !exists; index++) {
				   if (fileName.equals(levelLoad.getItemAt(index))) {
				     exists = true;
				   }
				 }
				 if (!exists) {
				   levelLoad.addItem(fileName);
				 }
				 */
			}
			
			if(e.getSource().equals(load)){
				
				String level = levelLoad.getSelectedItem().toString()+".txt";
				
				ArrayList<ArrayList<Character>> charMap = new ArrayList<ArrayList<Character>>();
		        try
		        {
		        	System.out.println("hello");
		            File file = new File("resources/customlevels/"+level);//temporary
		            System.out.println(file);
		            Scanner input = new Scanner(file);
		            while (input.hasNext())
		            {
		                String l = input.nextLine();
		                ArrayList<Character> r = new ArrayList<Character>();
	
		                for (int i = 0; i < l.length(); i++)
		                {
		                    r.add(l.charAt(i));
		                }
	
		                charMap.add(r);
		            }
		            input.close();	
		            
		            System.out.println("greetings");
		           
		       //removes top and bottom spikes and buffer
		            for(int i = 0; i < ybuffer+1; i++){
		            	charMap.remove(0);
		            	charMap.remove(charMap.size()-1);
		            }
		            for (int j = 0; j<charMap.size(); j++){
		            	for(int k = 0; k<xbuffer+1; k++){
		            		charMap.get(j).remove(0);
		            		charMap.get(j).remove(charMap.get(j).size()-1);
		            	}
		           	}
		            
		            mapPanel.map = new Map(charMap);
		            System.out.println("goodbye");
		            repaint();
		        }
		        catch(Exception er){
		        	er.getStackTrace();
		        }
			}
		}	
		/*
			//Serializing the map object
			try{
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(newFile));
				os.writeObject(mapPanel.map);
				os.close();
			}
			catch(Exception er){
				er.printStackTrace();
			}
			
			 boolean exists = false;
			 for (int index = 0; index < levelLoad.getItemCount() && !exists; index++) {
			   if (nameField.getText().equals(levelLoad.getItemAt(index))) {
			     exists = true;
			   }
			 }
			 if (!exists) {
			   levelLoad.addItem(fileName.substring(0,fileName.length()-4));
			 }
		}
		
		if(e.getSource().equals(load)){
			
			String level = levelLoad.getSelectedItem().toString()+".txt";
			System.out.println(level);
	
	        try{
	        	ObjectInputStream is = new ObjectInputStream(new FileInputStream("resources/customlevels/"+level));
	        	Map m = (Map)is.readObject();
	        	mapPanel.map = m;
				is.close();
			}
	        catch(Exception er){
	        	er.printStackTrace();
	        }
	        repaint();
		}
		*/
	}
	
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
		
		int x = e.getX();
		int y = e.getY();
		
		if(e.getSource().equals(mapPanel)){
			if(x/30>=mapPanel.map.getColumns()||x/30<0||y/30>=mapPanel.map.getRows()||y/30<0){
				System.out.println(false);
			}
			else{
				if(SwingUtilities.isRightMouseButton(e)){
					mapPanel.map.changeBlock(x/30,y/30, new Block());
				}
				else{
					mapPanel.map.changeBlock(x/30,y/30, new Block(blockID));
				}
			}
		}
		
		
	//make this code more efficient	
		if(e.getSource().equals(blockPanel)){
			blockPanel.setSelect(x/67*67, y/67*67);
			blockID = blockPanel.getBlock(x/67,y/67).getID();
		}
		
		if(e.getSource().equals(blockPanel2)){
			blockPanel2.setSelect(x/67*67, y/67*67);
			blockID = blockPanel2.getBlock(x/67,y/67).getID();
			System.out.println(blockID);
		}
		
		if(e.getSource().equals(dangerPanel)){
			dangerPanel.setSelect(x/67*67, y/67*67);
			blockID = dangerPanel.getBlock(x/67,y/67).getID();
		}
		
		if(e.getSource().equals(entityPanel)){
			entityPanel.setSelect(x/67*67, y/67*67);
			blockID = entityPanel.getBlock(x/67,y/67).getID();
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


	@Override
	public void itemStateChanged(ItemEvent e) {
			if(tileType.getSelectedItem().equals("Blocks"))
				cl.show(tilePanel, "1");
			else if (tileType.getSelectedItem().equals("More Blocks"))
				cl.show(tilePanel, "2");
			else if (tileType.getSelectedItem().equals("Dangers"))
				cl.show(tilePanel, "3");
			else if(tileType.getSelectedItem().equals("Dangers and Special"))
				cl.show(tilePanel, "4");
	}

}

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
	

	public void setSelect(int x, int y){
		select.setSize(x, y);;
	}
	
	public Block getBlock(int x, int y){
		return blockImages[y][x];
	}
	
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
