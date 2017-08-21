package hr.fer.Geometric;

public class VVector {
	public double x;
	public double y;
	public VVector(double x,double y)
	{
		this.x=x;
		this.y=y;
	}
	public double distance()
	{
		return Math.sqrt(x*x+y*y);
	}
	public void normalize()
	{
		double dist=distance();
		this.x/=dist;
		this.y/=dist;
	}
	public void multi(double C)
	{
		this.x*=C;
		this.y*=C;
	}
	public static VVector add(VVector V1, VVector V2)
	{
		return new VVector(V1.x+V2.x,V1.y+V2.y);
		
	}
	public static VVector diff(VVector V1, VVector V2)
	{
		return new VVector(V1.x-V2.x,V1.y-V2.y);
	}
	
}
