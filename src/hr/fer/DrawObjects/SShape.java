package hr.fer.DrawObjects;

import java.awt.Graphics;
import java.io.IOException;

public interface SShape 
{
	/**
	 * Draws the shape.
	 * Can throw exception because of the images. 
	 * @param g
	 * @throws IOException
	 */
	public void draw(Graphics g) throws IOException;
	
	
}
