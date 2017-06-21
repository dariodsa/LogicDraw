package hr.fer.GeneticAlgorithm;
import hr.fer.Geometric.*;
import hr.fer.DrawObjects.*;

import javax.swing.*;

import java.awt.geom.Line2D;
import java.util.*;
import java.util.function.Predicate;

public class Draw 
{
	private List<Wire>wires=new ArrayList<>();
	private List<Symbol>symbols=new ArrayList<>();
	private List<SShape>shapes=new ArrayList<>();
	private Map<String,Symbol>ulazi=new java.util.HashMap<>();
	
	private List<Integer>bitmask=new ArrayList<>();
	private List<Integer>bitmask2=new ArrayList<>();
	
	private int height=500;
	private int width=1000;
	
	private List<Symbol>temp_list=new ArrayList<>();
	private static Random rand=new Random();
	public String postfix;
	
	private double minimumNodeDistanceSum;
	private double minimumNodeDistance;
	private double edgeLengthDeviation;
	private double totalDistance;
	private int edgeCrossing;
	
	public Draw(int height,int width)
	{
		this.height=height;
		this.width=width;
	}
	public Draw(String postfix,int height,int width,List<Integer>bitmask,List<Integer>bitmask2)
	{
		this.postfix=postfix;
		this.height=height;
		this.width=width;
		
		this.bitmask=new ArrayList<>(bitmask);
		this.bitmask2=new ArrayList<>(bitmask2);
		
		Stack<Symbol>S=new Stack<>();
		
		int id=0;
		for(int i=0,len=postfix.length();i<len;++i)
		{
			if(postfix.charAt(i)=='-' || postfix.charAt(i)=='+' || postfix.charAt(i)=='*')
			{
				if(postfix.charAt(i)=='-')
				{
					Symbol P=S.peek();
					S.pop();
					
					//Adding a new Symbol to the list, 1 operands
					Symbol P2=new Symbol(Symbols.NOT,id++);
					P2.addParent(P);
					S.push(P2);
					symbols.add(P2);
				}
				else 
				{
					Symbol P=S.peek();
					S.pop();
					Symbol P1=S.peek();
					S.pop();
					Symbol P2;
					
					//Adding a new Symbol to the list, 2 operands
					if(postfix.charAt(i)=='+')
						P2=new Symbol(Symbols.OR,id++);
					else
						P2=new Symbol(Symbols.AND,id++);
					
					P2.addParent(P);
					P2.addParent(P1);
					
					S.push(P2);
					symbols.add(P2);
				}
			}
			else
			{
				Symbol s=new Symbol(Symbols.INPUT,id++);
				s.setName(postfix.charAt(i)+"");
				if(ulazi.containsKey((s.getName()))==false)
				{
					ulazi.put((s.getName()), s);
					symbols.add(s);
					S.push(ulazi.get(s.getName()));
				}
				else
				{
					S.push(ulazi.get(s.getName()));
				}
				
			}
			
		}
		Symbol P=S.peek();
		S.pop();
		Symbol out=new Symbol(Symbols.OUTPUT,id++);
		out.setName("Out");
		out.addParent(P);
		symbols.add(out);
		
		
		TopoSort();
		generate();
		setEvaluationVariables();
	}
	
	public Draw duplicate()
	{
		Draw D=new Draw(height,width);
		
		D.setWires(new ArrayList<Wire>(wires));
		D.setSymbols(new ArrayList<Symbol>(symbols));
		
		D.minimumNodeDistance=this.minimumNodeDistance;
		D.minimumNodeDistanceSum=this.minimumNodeDistanceSum;
		D.edgeCrossing=this.edgeCrossing;
		
		return D;
	}
	/**
	 * TopoSort gives the depth parameter to the symbols, or the relative position in the draw. If the symbol has a depth two, it can't be on the
	 * right side of the symbol with a depth three.
	 */
	public void TopoSort()
	{

		symbols.sort((t1,t2)->Integer.compare(t1.getDepth(),t2.getDepth()));
		for(Symbol simbol: symbols)
		{
			if(simbol.getType()==Symbols.INPUT)
				simbol.setDepth(1,true);
			for(Symbol parent: simbol.getParents())
			{
				//generateWires(parent, simbol);
				simbol.setDepth(parent.getDepth()+1);
			}
		}
		
		symbols.sort((t1,t2)->Integer.compare(t1.getDepth(),t2.getDepth()));
		
	}
	
	public void generate()
	{
		addCentralDots();
		
		for(Symbol S: symbols)
			for(Symbol parent: S.getParents())
				generateWires(parent, S);
		
		for(Symbol S:temp_list)
		{
			symbols.add(S);
		}
		rotatePins();
	}
	/*
	 * Corrects the position of the Symbols.
	 * Symbol S1 must have less or equal X value !!!!
	 * @param Symbol S1
	 * @param Symbol S2
	 * @return void
	 */
	private void checkThePosition(Symbol S1,Symbol S2)
	{
		
	}
	public void setSymbols(List<Symbol>symbols)
	{
		this.symbols=new ArrayList<Symbol>(symbols);
	}	
	public void setWires(List<Wire>wires)
	{
		this.wires=new ArrayList<Wire>(wires);
	}		
	/**
	 * This doesn't work I don't know WHY ?????
	 */
	public void rotatePins()
	{
		for(Symbol S: symbols)
		{
			if(S.isSymbolOrAndType())
			{
				Wire w1=S.getEnteringWire(0);
				Wire w2=S.getEnteringWire(1);
				
				if(w1.getStart().getY()>w2.getStart().getY())
				{
					//System.out.println(w1.getStart().getY()+" "+w2.getStart().getY());
					w1.setInputPin(2);
					w2.setInputPin(1);
				}
			}
		}
		return;
	}
	
	private void addCentralDots()
	{
		int pos=0;
		for(Symbol S: symbols)
		{
			Dot center=new Dot(bitmask2.get(pos)*60+200,
					           bitmask.get(pos)*30+100);
			
			if(S.getType()==Symbols.INPUT)center.setX(100);  
			if(S.getType()==Symbols.OUTPUT)center.setX(1050);
			if(S.isSymbolOutInType())center.setY(Dot.getRandom(100, 400));
			S.setPosition(center);
			
			if(S.isSymbolOutInType()) //The input and output dots can't be moved in the x direction, only in the y direction.
				S.getCenterDot().setCanYouMoveIt(false);
			
			addInVisibleWireAroundTheSymbol(S);
			
			if(!S.isSymbolOutInType())++pos;
		}
		
		return;
	}
	/**
	 * Adds invisible wires around the given symbol so that we can calculate if the given symbol overlap.
	 * @param 	
	 */
	private void addInVisibleWireAroundTheSymbol(Symbol S) 
	{
		
		Wire w1=new Wire(S.gridDots[0],S.gridDots[1],1,false);
		Wire w2=new Wire(S.gridDots[1],S.gridDots[2],1,false);
		Wire w3=new Wire(S.gridDots[2],S.gridDots[3],1,false);
		Wire w4=new Wire(S.gridDots[3],S.gridDots[0],1,false);
		
		wires.add(w1);wires.add(w2);wires.add(w3);wires.add(w4);
		return;
	}
	private void generateWires(Symbol p2, Symbol p)
	{
		checkThePosition(p2, p);
		int pin=p.getNextEmptyPin();
		
		Dot temp=p2.getLocation();
		Symbol temp2=p2;
		for(int i=0,r1=rand.nextInt(1);i<0;++i)
		{
			//System.out.println(temp+"+"+ p.getInput(pin));
			Dot d2=Dot.getRandomDot(temp, p.getInput(pin));
			//System.out.println(d2);
			Symbol S1=new Symbol(Symbols.EDGE,p2.getId());
			S1.addParent(temp2);
			S1.setPosition(d2);
			
			Wire W2=new Wire(temp2,S1,1);
			
			wires.add(W2);
			S1.AddEnteringWire(W2);
			temp_list.add(S1);
			
			temp=d2;
			temp2=S1;
		}
		
		Wire W1=new Wire(temp2,p,p.getNextEmptyPin());
		wires.add(W1);	
	
		p.AddEnteringWire(W1);
	}
	public List<Wire>getWires()
	{
		return this.wires;
	}
	public int getHeight()
	{
		return this.height;
	}
	public int getWidth()
	{
		return this.width;
	}
	public List<Symbol>getSymbols()
	{
		return this.symbols;
	}
	
	public List<SShape> getShapes()
	{
		shapes.clear();
		for(Symbol S: symbols)
			shapes.add(S);
			
		for(Wire W: wires)
			shapes.add(W);
		
		return shapes;
	}
	
	
	/**
	 * Function returns true if the given wires cross, false otherwise. 
	 * @param Wire A
	 * @param Wire B
	 * @return boolean 
	 */
	private boolean DoWiresCross(Wire A,Wire B)
	{
		return 
				Line2D.linesIntersect(A.getStart().getX(),A.getStart().getY(),
									  A.getEnd().getX(),A.getEnd().getY(),
									  B.getStart().getX(),B.getStart().getY(),
									  B.getEnd().getX(),B.getEnd().getY());
	}
	public int getNumWiresCrossing()
	{
		return this.edgeCrossing;
	}
	/**
	 * Function returns the number of the intersection of the Draw.
	 * @see DoWiresCross
	 * @return number of intersection
	 */
	private int getWiresCrossing()
	{
		//return 8;
		int ans=0;
		for(int i=0,len=wires.size();i<len;++i)
		{
			for(int j=i+1;j<len;++j)
			{
				//if(wires.get(i).isVisible()==false && wires.get(j).isVisible()==false)continue;
				if(wires.get(i).getStart().compareValueTo(wires.get(j).getEnd()))continue;
				if(wires.get(i).getStart().compareValueTo(wires.get(j).getStart()))continue;
				if(wires.get(i).getEnd().compareValueTo(wires.get(j).getEnd()))continue;
				if(wires.get(j).getStart().compareValueTo(wires.get(i).getEnd()))continue;
				if(DoWiresCross(wires.get(i),wires.get(j)))
				{
					++ans;
				}
			}
		}
		
		return ans;
	}
	public double getEdgeLengthDeviation()
	{
		double dul=1515615;
		double ans=0;
		
		for(Wire w:wires)
		{
			if(w.isVisible())
				dul=Math.min(dul, w.getLength());
		}
		for(Wire w: wires)
		{
			if(w.isVisible())
				ans+=w.getLength()-dul;
		}
		return ans;
	}
	/**
	 * Minimum Node Distance (Nbr of Nodes * Min. Node Distance2): This term helps
	 * in distributing the nodes. The square of minimum node distance is multiplied by the
	 * number of nodes.
	 */
	void setMinimumNodeDistance()
	{
		
	}
	/**
	 * Minimum Node Distance Sum (Min. Node Dist. Sum): The distance of each node
	 * from its nearest neighbour is measured, and the distances are added up. The bigger
	 * the sum the more evenly the nodes are usually distributed over the drawing area.
	 * @return void
	 */
	private void setMinimumNodeDistanceSum()
	{
		double ans=0;
		for(int i=0,len=symbols.size();i<len;++i)
		{
			double mina=4545645;
			for(int j=0;j<len;++j)
			{
				if(i==j)continue;
				mina=Math.min(mina,Geometric.distance(symbols.get(i).getCenterDot(), symbols.get(j).getCenterDot()));
			}
			ans+=mina;
		}
		this.minimumNodeDistanceSum=ans;
	}
	public double getMinimumNodeDistance()
	{
		return this.minimumNodeDistanceSum;
	}
	public double getTotalDistance()
	{
		double ans=0;
		for(Wire w:wires)
			if(w.isVisible())
				ans+=w.getLength();
		return ans;
	}
	public void setEvaluationVariables()
	{
		this.edgeCrossing=getWiresCrossing();
		this.edgeLengthDeviation=getEdgeLengthDeviation();
		this.totalDistance=getTotalDistance();
		setMinimumNodeDistance();
		setMinimumNodeDistanceSum();
		
	}
	public double getEvaluationFunction()
	{
		//setEvaluationVariables();
		//System.out.println(minimumNodeDistance+" "+minimumNodeDistanceSum+" "+edgeLengthDeviation+" "+edgeCrossing);
		return 5000090 * edgeCrossing
				+totalDistance
				+edgeLengthDeviation
				-15*minimumNodeDistanceSum;
	}
	
}
