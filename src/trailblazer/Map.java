package trailblazer;
/*
 * Charles
 * ICS SUMMATIVE TRAIL BLAZER
 * Dynamic map of level
 */

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class Map implements Serializable{

	private ArrayList<ArrayList<Block>> grid;
	private int rows, columns, size;
	
	public Map(){
		rows=16;
		columns=22;
		size = 30;
		grid = new ArrayList<ArrayList<Block>>();
		
	//creates arraylist with blocks	
		
		for(int i =0; i <rows; i++){
			ArrayList<Block> blocks = new ArrayList<Block>();
			for(int j = 0; j < columns; j++)
				blocks.add(new Block());
			grid.add(blocks);
		}
		System.out.println(grid);
	}

	public Map(ArrayList<ArrayList<Character>> charArr){
		System.out.println(charArr);
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
		System.out.println("1");
	}
	
//add one new array list of blocks to 2d grid in order to add row
	public void addRow(){
		
		ArrayList<Block> blocks = new ArrayList<Block>();
		for(int i = 0; i < columns; i++)
			blocks.add(new Block());
		grid.add(blocks);
		rows++;
		System.out.println(grid);
	}
	
//add a new block for in array list in grid
	public void addColumn(){
		for(int i = 0; i < rows; i++){
			grid.get(i).add(new Block());
		}
		columns++;
		System.out.println(grid);
	}
	
//remove the last row
	public void removeRow(){
		if(rows==16)
			System.out.println("Minimum vertical map size has been reached.");
		else{
			grid.remove(rows-1);
			rows--;
		}
	}
	
//removes last element in each block array list(by removing only one element form one arraylist???)	
	public void removeColumn(){
		if(columns==22)
			System.out.println("Minimum horizontal map size has been reached.");
		else{
			for(int i = 0; i < rows; i++){
			//apparently when one element is added to the end of one arraylist, an element is added to all other arraylists in the arraylist
				grid.get(i).remove(grid.get(i).size()-1);
			}
		columns--;
		}
		System.out.println(grid);
	}

//replaces block	
	public void changeBlock(int x, int y, Block b){
		grid.get(y).set(x, b);
	}
	
//add buffers here	
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

		buffer.add(invisID);
		buffer.add(spikeID);
		for(int i = 0; i < columns+xbuffer*2; i++){
			buffer.add(airID);
		}
		buffer.add(spikeID);
		buffer.add(invisID);
		
		spikeLayer.add(invisID);
		for(int i = 0; i<columns+xbuffer*2+2; i++){
			spikeLayer.add(spikeID);
			invisLayer.add(invisID);
		}
		spikeLayer.add(invisID);
		
		invisLayer.add(invisID);
		invisLayer.add(invisID);
		
		intMap.add(invisLayer);
		intMap.add(spikeLayer);
		for(int i = 0; i < ybuffer; i++){
			intMap.add(buffer);
		}	
		
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

	//bottom buffer layer	
		for(int i = 0; i < ybuffer; i++){
			intMap.add(buffer);
		}
		intMap.add(spikeLayer);
		intMap.add(invisLayer);

		return intMap;
}
	
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
