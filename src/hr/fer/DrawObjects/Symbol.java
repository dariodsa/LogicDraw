package hr.fer.DrawObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

/**
 * 
 * @see SShape
 * @author Dario
 *
 */
public class Symbol implements SShape{
	
	private Symbols type;
	private List<Symbol>parents=new ArrayList<>();
	
	public Symbol child; 
	
	private List<Wire>enteringWires=new ArrayList<>();
	
	private int height;
	private int width;
	private int depth=0;
	
	private String name="";
	
	private Dot center;
	private Dot output;
	public List<Dot>inputDots=new ArrayList<>();
	
	public Symbol[]gridDots=new Symbol[5];
	
	public Symbol()
	{
		parents=new ArrayList<>();
		enteringWires=new ArrayList<>();
		inputDots=new ArrayList<>();
		this.center=new Dot(0,0);
		this.output=new Dot(0,0);
		setSize(40, 40);
	}
	public Symbol(Symbols type)
	{
		this();
		this.type=type;
		
		if(type==Symbols.INPUT)
			depth=1;
		if(type==Symbols.INPUT || type==Symbols.OUTPUT)
			setSize(10, 10);
		if(type==Symbols.EDGE || type==Symbols.GRID)
			setSize(1,1);
		
		if(type!=Symbols.GRID)
			for(int i=0;i<4;++i)gridDots[i]=new Symbol(Symbols.GRID);
	}
	public Symbol(Symbols type,Dot position)
	{
		this(type);
		setPosition(position);
	}
	
	/**
	 * Sets the width and the height of the element.
	 * @param int height
	 * @param int width
	 * @return void
	 */
	public void setSize(int height,int width)
	{
		this.height=height;
		this.width=width;
	}
	/**
	 * Returns the next empty input pin, so that new wire can connect to it. 
	 * @return int Returns the number indificator of the empty input pin. The returned value will be in the in the range from 1 to N.
	 */
	public int getNextEmptyPin()
	{
		return enteringWires.size()+1;
	}
	/**
	 * Returns the type of the symbol. 
	 * @return the defined types in the enum Symbols.
	 * @see Symbols
	 */
	public Symbols getType()
	{
		return this.type;
	}
	/**
	 * Returns the height of the symbol.
	 * @return int
	 */
	public int getHeight()
	{
		return this.height;
	}
	/**
	 * Returns the width of the symbol.
	 * @return int
	 */
	public int getWidth()
	{
		return this.width;
	}
	/**
	 * Returns the list of the parents. Parents are symbols that are connected to this symbol.
	 * @return List<Symbol>
	 */
	public List<Symbol> getParents()
	{
		return this.parents;
	}
	/**
	 * Sets the parents. See also for more information, getParents.
	 * @param List<Symbol>
	 * @return void
	 */
	public void setParents(List<Symbol> parentsTemp)
	{
		parents.clear();
		for(Symbol S: parentsTemp)
			addParent(S);
	}
	/**
	 * Returns the parent with at a specific location which is given as the parametar of the function.
	 * First parent is at location 1.
	 * @param int pos
	 * @return Symbol
	 */
	public Symbol getParent(int pos)
	{
		if(this.parents.size()<pos || pos<1)
			throw new RuntimeException("This parent does not exsist.");
		return this.parents.get(pos-1);
	}
	/**
	 * Adds new parent to list of the existing. 
	 * @param Symbol
	 * @return void
	 */
	public void addParent(Symbol parent)
	{
		
		parents.add(parent);
	}
	public void AddEnteringWire(Wire W1)
	{
		enteringWires.add(W1);
	}
	public List<Wire> getEnteringWires()
	{
		return new ArrayList<>(enteringWires);
	}
	/**
	 * Gets the entering wire in the symbol. 
	 * @param pos from 0 to N
	 * @return wire that enters the pos-th input pin.
	 */
	public Wire getEnteringWire(int pos)
	{
		return enteringWires.get(pos);
	}
	/**
	 * Returns the depth of the given symbol in the draw. This is used in the construct of the draw, topological sort. 
	 * @return int depth 
	 */
	public int getDepth()
	{
		return this.depth;
	}
	/**
	 * Sets the depth of the symbol if the given depth is larger than the previous.
	 * @param depth from one to ...
	 */
	public void setDepth(int depth)
	{
		this.depth=Math.max(this.depth, depth);
	}
	/**
	 * Sets the depth despite the previous value.
	 * @param depth
	 * @param change
	 */
	public void setDepth(int depth,boolean change)
	{
		this.depth=depth;
	}
	/**
	 * Sets the new position of the center dot. By that everything is changed. The output dot will be set to a new one, input dots will be updated, as
	 * well as , the grid dots.
	 * @param center
	 */
	public void setPosition(Dot center)
	{
		this.center.setXandY(center.getX(),center.getY());
		
		this.output.setXandY(this.center.getX()+getWidth()/2,this.center.getY());
		
		
		int numOfParents=parents.size();
		if(numOfParents==0)numOfParents=1;
		int br=1;
		if(inputDots.isEmpty())
		{
			for(int i=1;i<=numOfParents;++i)
			{
				inputDots.add(new Dot(
									this.center.getX()-getWidth()/2,
									(int)(this.center.getY()-getHeight()/2+(double)i*((double)getHeight()/((double)numOfParents+1.0)))
									));
				
			}
		}
		for(Dot tocka: inputDots)
		{
				tocka.setXandY(this.center.getX()-getWidth()/2,
					(int)(this.center.getY()-getHeight()/2+(double)br*((double)getHeight()/((double)numOfParents+1.0))));
			++br;
		}
		
		if(type!=Symbols.GRID && type!=Symbols.EDGE)
		{
			int offSet=2;
			if(isSymbolOutInType())
				offSet=-5;
		
		
		gridDots[0].setPosition(new Dot(getCenterDot().getX()-getWidth()/2+offSet, getCenterDot().getY()-getHeight()/2+offSet));
		gridDots[1].setPosition(new Dot(getCenterDot().getX()+getWidth()/2-offSet, getCenterDot().getY()-getHeight()/2+offSet));
		gridDots[2].setPosition(new Dot(getCenterDot().getX()+getWidth()/2-offSet, getCenterDot().getY()+getHeight()/2-offSet));
		gridDots[3].setPosition(new Dot(getCenterDot().getX()-getWidth()/2+offSet, getCenterDot().getY()+getHeight()/2-offSet));
		}
		
		
	}
	
	/**
	 * Gets the name of the symbol. It will be display just near the symbol in the main draw. Right now this
	 * is used only on input and output pins. 
	 * @return name string 
	 */
	public String getName()
	{
		return this.name;
	}
	/**
	 * Sets the name of the symbol. 
	 */
	public void setName(String name)
	{
		this.name=name;
	}
	/**
	 * Returns true if the symbol's type is OR or AND, false otherwise.
	 * @return boolean
	 * @see Symbols
	 */
	public boolean isSymbolOrAndType()
	{
		return getType()==Symbols.OR || getType()==Symbols.AND;
	}
	/**
	 * Returns true if the symbol's type is INPUT or OUTPUT, false otherwise.
	 * @return boolean
	 * @see Symbols
	 */
	public boolean isSymbolOutInType()
	{
		return getType()==Symbols.INPUT || getType()==Symbols.OUTPUT;
	}
	/**
	 * Returns the center dot of the symbol.
	 * @return
	 */
	public Dot getCenterDot()
	{
		return this.center;
	}
	/**
	 * Returns the output dot of the symbol. The wire that exiting from the symbol, firstly is connect on the output dot.
	 * Output dot is ( center.x + width/2, center.y).
	 * @return
	 */
	public Dot getOutputDot()
	{
		return output;
	}
	/**
	 * @param Variable num from 1 to n.
	 */
	public Dot getInput(int num)
	{
		return inputDots.get(num-1);
	}
	
	@Override
	public void draw(Graphics g) throws IOException {
		
		BufferedImage img=null;
		if(getType()==Symbols.OR)
			img = ImageIO.read(new File("or.png"));
		else if(getType()==Symbols.AND)
			img = ImageIO.read(new File("and.png"));
		else if(getType()==Symbols.NOT)
			img = ImageIO.read(new File("not.png"));
		else if(getType()!=Symbols.EDGE)
			img = ImageIO.read(new File("inout.png"));
		if(img!=null)
		{
			g.drawImage(img, 
					getCenterDot().getX()-getWidth()/2,//-img.getWidth()/2,
					getCenterDot().getY()-getHeight()/2,//-img.getHeight()/2,
					getWidth(),getHeight(),
					null);
		}
		
	}
	
	public void moveIt(Dot newPosition) {
		setPosition(new Dot(newPosition));
	}
	
	public Dot getLocation() {
		return center;
	}
}
