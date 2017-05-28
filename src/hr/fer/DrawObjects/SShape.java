package hr.fer.DrawObjects;

import java.awt.Graphics;
import java.io.IOException;

public interface SShape 
{
	public void draw(Graphics g) throws IOException;
	public void moveIt(Dot newPosition);
	public Dot getLocation();
	
}
