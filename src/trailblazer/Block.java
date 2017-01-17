package trailblazer;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/*
 * Charles
 * ICS SUMMATIVE TRAIL BLAZER
 * Type of block
 */

public class Block{

	private int id;
	private static BufferedImage img = null;
	
	public Block(){
		id = 100;
		if (img==null){
			try{
				img = ImageIO.read(new File("bin/BlockTextures.png"));
			}catch(IOException e){
				System.out.println("You did something wrong" + e);
			}
		}
	}
	
	public Block(int id){
		this.id = id;
	}
	
	public int getID(){
		return id;
	}
	
	public void drawBlock(Graphics g, int column, int row, int size){

		g.drawImage(img, column*size, row*size, (column+1)*size, (row+1)*size, (id%12)*48, (id/12)*48, (id%12+1)*48, (id/12+1)*48, null);

	}
}
