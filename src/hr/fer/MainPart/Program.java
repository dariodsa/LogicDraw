package hr.fer.MainPart;
import java.util.Random;

import hr.fer.DrawObjects.*;

import javax.swing.*;

import com.sun.javafx.geom.Vec2d;

public class Program {

	public static int GRID=300;
	public static void main(String[] args) 
	{
		try {
			SwingUtilities.invokeAndWait(
					()->
					{
						new MainFrame(500, 1300);
					}
			);
		} catch (Exception e) {System.err.println(e.getMessage());}
		
	}

}
