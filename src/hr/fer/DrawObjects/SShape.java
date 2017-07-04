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
	/**
	 * Moves it to the new position. 
	 * @param newPosition that dot is the new center dot of the shape.
	 */
	public void moveIt(Dot newPosition);
	/**
	 * Returns the center of the shape.
	 * @return center
	 */
	public Dot getLocation();
	
}
