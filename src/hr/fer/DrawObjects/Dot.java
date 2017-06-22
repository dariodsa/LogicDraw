package hr.fer.DrawObjects;

import java.awt.Graphics;
import java.util.Random;

public class Dot implements SShape{
	private int x;
	private int y;
	
	private boolean canYouMoveIt=true;
	
	private static Random rand=new Random();
	
	public Dot(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	public Dot(Dot D)
	{
		this.x=D.getX();
		this.y=D.getY();
	}
	public int getX()
	{
		return this.x;
	}
	public int getY()
	{
		return this.y;
	}
	public boolean canYouMoveIt()
	{
		return this.canYouMoveIt;
	}
	public void setCanYouMoveIt(boolean can)
	{
		this.canYouMoveIt=can;
	}
	public void setX(int x)
	{
		if(this.canYouMoveIt)this.x=x;
	}
	public void setY(int y)
	{
		this.y=y;
	}
	@Override
	public boolean equals(Object O)
	{
		Dot D=(Dot)O;
		if(D.x==this.x && D.y==this.y)
			return true;
		return false;
	}
	public void setXandY(int x,int y)
	{
		setX(x);
		setY(y);
	}
	public boolean compareValueTo(Dot x)
	{
		return getX()==x.getX() && getY()==x.getY();
	}
	@Override
	public String toString()
	{
		return "("+this.getX()+","+this.getY()+")";
	}
	/**
	 * Function returns random dot between Dot D1 and Dot D2.
	 * --Function throws exception 
	 * @param Dot D1
	 * @param Dot D2
	 * @return Dot
	 */
	
	public static Dot getRandomDot(Dot D1,Dot D2)
	{
		Dot ans=new Dot( getRandom(D1.getX(),D2.getX()),
						 getRandom(D1.getY(),D2.getY()));
		return ans;
	}
	public static int getRandom(int X,int Y)
	{
		if(X>Y){int z=X;X=Y;Y=z;}
		if(X==Y)return X;
		if(Y-X<0)
			return rand.nextInt(50)+X;   /// ????
		
		return (rand.nextInt(Y-X))+X;
	}
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		return;
	}
	@Override
	public void moveIt(Dot newPosition) {
		setXandY(x, y);
		
	}
	@Override
	public Dot getLocation() {
		return this;
	}
	
}
