package trailblazer;
/*
 * Charles
 * ICS SUMMATIVE TRAIL BLAZER
 * Serializable, Dynamic map of level
 */

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Map implements Serializable{

	private ArrayList<ArrayList<Block>> grid;
	private int rows, columns, size;
	
//default map constructor with default default values
	public Map(){
		rows=16;
		columns=22;
		size = 30;
		
	//arrayl ist of array lists of blocks is of size rows and columns is created and 
	//represents the map
		grid = new ArrayList<ArrayList<Block>>();
		for(int i =0; i <rows; i++){
			ArrayList<Block> blocks = new ArrayList<Block>();
			for(int j = 0; j < columns; j++)
				blocks.add(new Block());
			grid.add(blocks);
		}
	}

//unimplemented method for reading character array maps	from previous char arry level files
//unable to remove as it will mess up current serializable files
	public Map(ArrayList<ArrayList<Character>> charArr){
		columns = charArr.get(0).size();
		rows = charArr.size();
		size = 30;
		grid = new ArrayList<ArrayList<Block>>();
		
		for(int i=0; i<rows; i++){
			ArrayList<Block> blocks = new ArrayList<Block>();
			for(int j = 0; j<columns; j++){
			//offset of ascii values to block values is 11	
				blocks.add(new Block((int)charArr.get(i).get(j)-11));
			}
			grid.add(blocks);
		}
	}
	
//add one new array list of blocks to 2d grid in order to add row
	public void addRow(){
		ArrayList<Block> blocks = new ArrayList<Block>();
		for(int i = 0; i < columns; i++)
			blocks.add(new Block());
		grid.add(blocks);
		rows++;
	}
	
//add a new block for in array list in grid
	public void addColumn(){
		for(int i = 0; i < rows; i++){
			grid.get(i).add(new Block());
		}
		columns++;
	}
	
//remove the last row of the grid, displays error when rows is at a minimum 
	public void removeRow(){
		if(rows<=16)
			JOptionPane.showMessageDialog(new JFrame(), "Minimum Row Size Reached", "Error",
					JOptionPane.ERROR_MESSAGE, null);
		else{
			grid.remove(rows-1);
			rows--;
		}
	}
	
//removes last element in each block array list, displays error when columns is at a minimum	
	public void removeColumn(){
		if(columns<=22)
			JOptionPane.showMessageDialog(new JFrame(), "Minimum Column Size Reached", "Error",
					JOptionPane.ERROR_MESSAGE, null);
		else{
			for(int i = 0; i < rows; i++){
				grid.get(i).remove(grid.get(i).size()-1);
			}
		columns--;
		}
	}

//replaces block at give row and column	
	public void changeBlock(int x, int y, Block b){
		grid.get(y).set(x, b);
	}
	
//method that creates and arraylist of arraylists of integers as the game will need
//block buffers that prevent you from falling forever by stopping and killing you with spikes
//if you ever somehow leave the confines of the map. the buffer also prevents you form seeing 
//these spikes
	public ArrayList<ArrayList<Integer>> getIntGrid(){
		
		int ybuffer = 10;
		int xbuffer = 14;
		final int airID = 100;
		final int spikeID = 56;
		final int invisID = 55;
		
		ArrayList<ArrayList<Integer>> intMap = new ArrayList<ArrayList<Integer>>();
	
		ArrayList<Integer> buffer = new ArrayList<Integer>();
		ArrayList<Integer> spikeLayer = new ArrayList<Integer>();
		ArrayList<Integer> invisLayer = new ArrayList<Integer>();

	//adds block, then spike then air to buffer layer
		buffer.add(invisID);
		buffer.add(spikeID);
		for(int i = 0; i < columns+xbuffer*2; i++){
			buffer.add(airID);
		}
		buffer.add(spikeID);
		buffer.add(invisID);
	
	//spike layer and invisible block layer	
		spikeLayer.add(invisID);
		for(int i = 0; i<columns+xbuffer*2+2; i++){
			spikeLayer.add(spikeID);
			invisLayer.add(invisID);
		}
		spikeLayer.add(invisID);
		invisLayer.add(invisID);
		invisLayer.add(invisID);
		
//code for surrounding the map with a buffer
	//layer	at the top of map
		intMap.add(invisLayer);
		intMap.add(spikeLayer);
		for(int i = 0; i < ybuffer; i++){
			intMap.add(buffer);
		}	
		
	//buffer to the side of the map	
		for(int i = 0; i < rows; i++){
			intMap.add(new ArrayList<Integer>());
			intMap.get(intMap.size()-1).add(invisID);
			System.out.println(intMap.size()-1);
			intMap.get(intMap.size()-1).add(spikeID);
			for(int p = 0; p< xbuffer; p++)
				intMap.get(intMap.size()-1).add(airID);
			
			for(int j =0; j<columns; j++)
				intMap.get(intMap.size()-1).add(grid.get(i).get(j).getID());
			
			for(int p = 0; p< xbuffer; p++)
				intMap.get(intMap.size()-1).add(airID);

			intMap.get(intMap.size()-1).add(spikeID);
			intMap.get(intMap.size()-1).add(invisID);
		}

	//buffer layer for the bottom of map	
		for(int i = 0; i < ybuffer; i++){
			intMap.add(buffer);
		}
		intMap.add(spikeLayer);
		intMap.add(invisLayer);

		return intMap;
}

//draws each block from grid	
	public void draw(Graphics g){
		
		for(int i = 0; i  < rows; i++){
			for(int j = 0; j < columns; j++){
				grid.get(i).get(j).drawBlock(g, j, i, size);	
			}
		}
	
		
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				g.drawRect(j*size,i*size,size,size);	
			}
		}
	}

//getter setter methods	
	public int getRows(){
		return rows;
	}
	
	public int getColumns(){
		return columns;
	}
	
	public ArrayList<ArrayList<Block>> getGrid(){
		return grid;
	}
}
