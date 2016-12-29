package trailblazer;

import java.awt.image.BufferedImage;

public class CheckPoint extends Block
{
	public CheckPoint(BufferedImage texture) 
	{
		super(texture);
	}
	public void setTexture(BufferedImage texture)
	{
		this.texture = texture;
	}
}
