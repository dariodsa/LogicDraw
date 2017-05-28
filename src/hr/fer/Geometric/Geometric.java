package hr.fer.Geometric;
import java.util.*;

import com.sun.javafx.geom.Vec2d;

import hr.fer.DrawObjects.*;


public class Geometric {
	
	private static Random rand=new Random();
	
	public static double distance(Dot A,Dot B)
	{
		return Math.sqrt((A.getX()-B.getX())*(A.getX()-B.getX())+(A.getY()-B.getY())*(A.getY()-B.getY()));
	}
	public static SShape getClosest(List<SShape> shapes, Dot clicked)
	{
		SShape ans=null;
		
		double dul=15656516;
		for(SShape s:shapes)
		{
			if(s instanceof Symbol)
			{
				if(dul>distance(s.getLocation(),clicked.getLocation()))
				{
					dul=distance(s.getLocation(),clicked.getLocation());
					ans=s;
				}
			}
		}
		
		return ans;
	}
	public static Vec2d returnVector()
	{
		Vec2d vektor=new Vec2d();
		vektor.x=(int)(rand.nextGaussian()*40);
		vektor.y=(int)(rand.nextGaussian()*40);
		return vektor;
	}
}
