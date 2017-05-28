package hr.fer.DrawObjects;

import hr.fer.Geometric.Geometric;

import java.awt.Graphics;


public class Wire implements SShape
{
	private Dot start;
	private Dot end;
	private Symbol parent;
	private Symbol child;
	
	private boolean visible;
	
	public Wire(Dot start,Dot end)
	{
		this.start=start;
		this.end=end;
		this.visible=true;
	}
	public Wire(Dot start,Dot end,boolean visible)
	{
		this(start,end);
		this.visible=visible;
	}
	
	/*
	 * Sets the object's attribute visible to the given value.
	 * @param boolean
	 * @return void
	 */
	public void setVisible(boolean visible)
	{
		this.visible=visible;
	}
	/*
	 * Returns if is the wire visible, important for drawing.
	 * @return boolean
	 */
	public boolean isVisible()
	{
		return this.visible;
	}
	/*
	 * Sets the parent symbol. See also getParent.
	 * @param Symbol
	 * @return void
	 */
	public void setParent(Symbol parent)
	{
		this.parent=parent;
	}
	/*
	 * Returns the symbol from which is the wire connected.(child). See also start dot. 
	 * @return Symbol
	 */
	public Symbol getParent()
	{
		return this.parent;
	}
	/*
	 * Sets the child symbol. See also getChild.
	 * @param Symbol
	 * @return void
	 */
	public void setChild(Symbol child)
	{
		this.child=child;
	}
	/*
	 * Returns the symbol on which is the wire connected.(child). See also end dot. 
	 * @return Symbol
	 */
	public Symbol getChild()
	{
		return this.child;
	}
	/*
	 * Sets the start dot.
	 * @param Dot start
	 * @return void
	 */
	public void setStart(Dot start)
	{
		this.start=start;
	}
	/*
	 * Sets the end dot.
	 * @param Dot end
	 * @return void
	 */
	public void setEnd(Dot end)
	{
		this.end=end;
	}
	/*
	 * Returns the start dot.
	 * @return Dot
	 */
	public Dot getStart()
	{
		return this.start;
	}
	/*
	 * Returns the end dot.
	 * @return Dot
	 */
	public Dot getEnd()
	{
		return this.end;
	}
	/*
	 * Returns the length of the wire.
	 * @return double
	 */
	public double getLength()
	{
		return Geometric.distance(getStart(), getEnd());
	}
	@Override
	public void draw(Graphics g) {
		if(visible)
			g.drawLine(start.getX(), start.getY(),
				   end.getX(), end.getY());
		
	}
	@Override
	public void moveIt(Dot newPosition) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Dot getLocation() {
		// TODO Auto-generated method stub
		return null;
	}
}
