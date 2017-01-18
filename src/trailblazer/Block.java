package trailblazer;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/*
 * Charles
 * ICS SUMMATIVE TRAIL BLAZER
 * Serializable block class that determines interactions with other blocks
 */

public class Block implements Serializable{

	private int id;
	private static BufferedImage img = null;
	
//default constructor that pulls image	
	public Block(){
		id = 100;
		if (img==null){
			try{
				img = ImageIO.read(new File("bin/BlockTextures.png"));
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
//getter setters
	public Block(int id){
		this.id = id;
	}
	
	public int getID(){
		return id;
	}
	
//get buffered image based on block id from the image and draws block.	
	public void drawBlock(Graphics g, int column, int row, int size){

		g.drawImage(img, column*size, row*size, (column+1)*size, (row+1)*size, (id%12)*48, (id/12)*48, (id%12+1)*48, (id/12+1)*48, null);

	}
}
