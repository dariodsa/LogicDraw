package hr.fer.DrawObjects;

import hr.fer.Geometric.Geometric;

import java.awt.Graphics;


/**
 * Represents the wire in our draw. It is consist of parent symbol and child symbol. You would also need to tell on which input pin
 * do you want to connect your wire. You can also set its visibility. 
 * @author Dario
 */
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
	 * Sets the input pin of the wire. On which input pin of the child symbol will wire connect.
	 * @param inputPin from 1 to N
	 * @see Symbol#getInput(int)
	 */
	public void setInputPin(int inputPin)
	{
		this.inputPin=inputPin;
	}
	/**
	 * Returns the inputPin attribute. 
	 * @return inputPin
	 * @see Symbol#getInput(int)
	 */
	public int getInputPin()
	{
		return this.inputPin;
	}
	/**
	 * Sets the object's attribute visible to the given value.
	 * @param visible true if it is visible, false otherwise
	 * @return void
	 * @see Wire#draw(Graphics)
	 */
	public void setVisible(boolean visible)
	{
		this.visible=visible;
	}
	/**
	 * Returns if is the wire visible, important for drawing.
	 * @return boolean
	 * @see Wire#draw(Graphics)
	 */
	public boolean isVisible()
	{
		return this.visible;
	}
	/**
	 * Sets the parent symbol. From this symbol wire exits. 
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
	 * Sets the child symbol. In this symbol wire enters. 
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
	 * @return length Euclidean distance 
	 */
	public double getLength()
	{
		return Geometric.distance(getStart(), getEnd());
	}
	@Override
	public void draw(Graphics g) {
		if(visible)
			g.drawLine(getStart().getX(), getStart().getY(),
				   getEnd().getX(), getEnd().getY());
		
	}
}
