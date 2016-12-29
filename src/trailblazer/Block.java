package trailblazer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Block 
{
	protected BufferedImage texture;
	public Block(BufferedImage texture)
	{
		this.texture = texture;
	}
	public void draw(int x, int y, Graphics g)
	{
		g.setColor(Color.GRAY);
		g.fillRect(x, y, 48, 48);
		//g.drawImage(texture, x, y, null);
	}
}
