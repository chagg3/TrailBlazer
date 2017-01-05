package trailblazer;

public class Turret 
{
	private int direction;//1=left, 2=right, 3=up, 4=down
	private int i, j;
	
	public Turret(int i, int j, int direction)
	{
		this.i = i;
		this.j = j;
		this.direction = direction;
	}
	public Projectile fire()
	{
		Projectile n;
		//int blitMod;
		
		if (direction == 1)
			n = new Projectile(j*48, i*48, direction);
		else if (direction == 2)
			n = new Projectile((j+1)*48, i*48, direction);
		else if (direction == 3)
			n = new Projectile(j*48, i*48, direction);
		else
			n = new Projectile(j*48, (i+1)*48, direction);
			
		
		return n;
	}
}
