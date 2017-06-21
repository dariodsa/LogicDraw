package hr.fer.DrawObjects;

import hr.fer.Geometric.Geometric;

import java.awt.Graphics;


public class Wire implements SShape
{
	private Symbol parent;
	private Symbol child;
	
	private int inputPin;
	private boolean visible;
	
	public Wire(Symbol parent,Symbol child,int intputPin)
	{
		this.parent=parent;
		this.child=child;
		this.inputPin=intputPin;
		this.visible=true;
	}
	public Wire(Symbol parent,Symbol child,int inputPin,boolean visible)
	{
		this(parent,child,inputPin);
		this.visible=visible;
	}
	
	/**
	 * Sets the input pin of the wire.
	 * @param inputPin
	 */
	public void setInputPin(int inputPin)
	{
		this.inputPin=inputPin;
	}
	public int getInputPin()
	{
		return this.inputPin;
	}
	/**
	 * Sets the object's attribute visible to the given value.
	 * @param boolean
	 * @return void
	 */
	public void setVisible(boolean visible)
	{
		this.visible=visible;
	}
	/**
	 * Returns if is the wire visible, important for drawing.
	 * @return boolean
	 */
	public boolean isVisible()
	{
		return this.visible;
	}
	/**
	 * Sets the parent symbol. See also getParent.
	 * @param Symbol
	 * @return void
	 */
	public void setParent(Symbol parent)
	{
		this.parent=parent;
	}
	/**
	 * Returns the symbol from which is the wire connected.(child). See also start dot. 
	 * @return Symbol
	 */
	public Symbol getParent()
	{
		return this.parent;
	}
	/**
	 * Sets the child symbol. See also getChild.
	 * @param Symbol
	 * @return void
	 */
	public void setChild(Symbol child)
	{
		this.child=child;
	}
	/**
	 * Returns the symbol on which is the wire connected.(child). See also end dot. 
	 * @return Symbol
	 */
	public Symbol getChild()
	{
		return this.child;
	}
	/**
	 * Returns the start dot.
	 * @return Dot
	 */
	public Dot getStart()
	{
		return this.parent.getOutputDot();
	}
	/**
	 * Returns the end dot.
	 * @return Dot
	 */
	public Dot getEnd()
	{
		return this.child.getInput(inputPin);
	}
	/**
	 * Returns the length of the wire.
	 * @return double
	 */
	public double getLength()
	{
		return Geometric.distance(getStart(), getEnd());
	}
	@Override
	public void draw(Graphics g) {
		//if(visible)
			g.drawLine(getStart().getX(), getStart().getY(),
				   getEnd().getX(), getEnd().getY());
		
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
