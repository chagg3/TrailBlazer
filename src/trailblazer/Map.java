package trailblazer;
/*
 * Charles
 * ICS SUMMATIVE TRAIL BLAZER
 * Dynamic map of level
 */

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;


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
				blocks.add(new Block((int)charArr.get(i).get(j)-1));
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
