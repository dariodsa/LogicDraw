package hr.fer.GeneticAlgorithm;
import hr.fer.Geometric.*;
import hr.fer.DrawObjects.*;

import javax.swing.*;

import java.awt.geom.Line2D;
import java.util.*;
import java.util.function.Predicate;

public class Draw 
{
	private List<Dot>dots=new ArrayList<>();
	private List<Wire>wires=new ArrayList<>();
	private List<Symbol>symbols=new ArrayList<>();
	private List<SShape>shapes=new ArrayList<>();
	private Map<String,Symbol>ulazi=new java.util.HashMap<>();
	int id=0;
	private double minimumNodeDistanceSum;
	private double minimumNodeDistance;
	private double edgeLengthDeviation;
	private double edgeCrossing;
	public double numOfNodes=0;
	
	private int height=500;
	private int width=1000;
	
	private static Random rand=new Random();
	private List<Symbol>tempList=new ArrayList<>();
	public String postfix;
	
	
	public Draw(String postfix,int height,int width)
	{
		this.postfix=postfix;
		this.height=height;
		this.width=width;
		
		Stack<Symbol>S=new Stack<>();
		for(int i=0,len=postfix.length();i<len;++i)
		{
			if(postfix.charAt(i)=='-' || postfix.charAt(i)=='+' || postfix.charAt(i)=='*')
			{
				if(postfix.charAt(i)=='-')
				{
					Symbol P=S.peek();
					S.pop();
					
					//Adding a new Symbol to the list, 1 operants
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
					
					//Adding a new Symbol to the list, 2 operants
					if(postfix.charAt(i)=='+')
						P2=new Symbol(Symbols.OR,id++);
					else
						P2=new Symbol(Symbols.AND,id++);
					int x=rand.nextInt(15647);
					if(x%2==0)
					{
						P2.addParent(P);
						P2.addParent(P1);
					}
					else
					{
						P2.addParent(P1);
						P2.addParent(P);
					}
					
					S.push(P2);
					symbols.add(P2);
				}
			}
			else
			{
				Symbol s=new Symbol(Symbols.INPUT,id++);
				s.setName(postfix.charAt(i)+"");
				if(ulazi.containsKey((String)s.getName())==false)
				{
					ulazi.put(s.getName(), s);
					symbols.add(s);
					S.push(ulazi.get(s.getName()));
				}
				else
				{
					S.push(ulazi.get(s.getName()));
				}
				
			}
			numOfNodes=symbols.size();
		}
		Symbol P=S.peek();
		S.pop();
		Symbol out=new Symbol(Symbols.OUTPUT,id++);
		out.setName("Out");
		out.addParent(P);
		symbols.add(out);
		generate();
	}
	public void generate()
	{
		addCentralDots();
		for(Symbol simbol: symbols)
		{
			for(Symbol parent: simbol.getParents())
					generateWires(parent, simbol);
		}
		
		//I am adding extra edges in the connection between 
		for(Symbol s1: tempList)
		{
			symbols.add(s1);
		}
		Random r=new Random();
		int ra=r.nextInt(3);
		//for(int i=0;i<ra;++i)
		rotatePinsInSomeSymbols();
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
	private void rotatePinsInSomeSymbols()
	{
		Random r=new Random();
		for(int i=0,len=symbols.size();i<len;i=(i+1))
		{
			//System.out.println(br+" "+ra+" "+i+ " "+len);
			if(symbols.get(i).isSymbolOrAndType())
			{
				int br=getWiresCrossing();
				
				//zamjena 
				Symbol simbol=symbols.get(i);
				Wire W1=simbol.getEnteringWire(0);
				Wire W2=simbol.getEnteringWire(1);
				Dot temp=W1.getEnd();
				W1.setEnd(W2.getEnd());
				W2.setEnd(temp);
				
				if(br<getWiresCrossing())
				{
					Symbol simbola=symbols.get(i);
					Wire W1a=simbola.getEnteringWire(0);
					Wire W2a=simbola.getEnteringWire(1);
					Dot tempa=W1a.getEnd();
					W1a.setEnd(W2a.getEnd());
					W2a.setEnd(tempa);
				}
				//return;
				
			}
		}
		return;
	}
	private void addCentralDots()
	{
		for(Symbol S: symbols)
		{
			Dot center=new Dot(Dot.getRandom(200, 900),
					           Dot.getRandom(175, 575));
			
			if(S.getType()==Symbols.INPUT)center.setX(100);  
			if(S.getType()==Symbols.OUTPUT)center.setX(1050);
			
			dots.add(center);
			S.setPosition(center);
			
			if(S.isSymbolOutInType()) //The input and output dots can't be moved in the x direction, only in the y direction.
				S.getCenterDot().setCanYouMoveIt(false);
			
			if(S.getType()!=Symbols.EDGE)
			{
				addInVisibleWireAroundTheSymbol(S);
			}
		}
		for(Symbol s:tempList)
			symbols.add(s);
		return;
	}
	
	private void addInVisibleWireAroundTheSymbol(Symbol S) 
	{
		int offSet=7;
		
		Dot d1=new Dot(S.getCenterDot().getX()-S.getWidth()/2+offSet, S.getCenterDot().getY()-S.getHeight()/2+offSet);
		Dot d2=new Dot(S.getCenterDot().getX()+S.getWidth()/2-offSet, S.getCenterDot().getY()-S.getHeight()/2+offSet);
		Dot d3=new Dot(S.getCenterDot().getX()+S.getWidth()/2-offSet, S.getCenterDot().getY()+S.getHeight()/2-offSet);
		Dot d4=new Dot(S.getCenterDot().getX()-S.getWidth()/2+offSet, S.getCenterDot().getY()+S.getHeight()/2-offSet);
		
		Wire w1=new Wire(d1,d2,false);
		Wire w2=new Wire(d2,d3,false);
		Wire w3=new Wire(d3,d4,false);
		Wire w4=new Wire(d4,d1,false);
		
		wires.add(w1);wires.add(w2);wires.add(w3);wires.add(w4);
		return;
	}
	private void generateWires(Symbol p2, Symbol p)
	{
		checkThePosition(p2, p);
		
		Dot d1=Dot.getRandomDot(p2.getOutputDot(), p.getInput(p.getNextEmptyPin()));
		
		
		
		dots.add(d1);
		Symbol S=new Symbol(Symbols.EDGE,p2.getId());
		S.addParent(p2);
		S.setPosition(d1);
		
		//Dot d4=new Dot(p2.getOutputDot());
		Wire W=new Wire(p2.getOutputDot(),S.getLocation());
		S.AddEnteringWire(W);
		tempList.add(S);
		++numOfNodes;
		W.setParent(p2);
		W.setChild(S);
		
		wires.add(W);
		Dot temp=d1;
		Symbol temp2=S;
		for(int i=0,r1=rand.nextInt(3);i<0;++i)
		{
			Dot d2=Dot.getRandomDot(temp, p.getInput(p.getNextEmptyPin()));
			
			dots.add(d2);
			
			Symbol S1=new Symbol(Symbols.EDGE,p2.getId());
			S1.addParent(temp2);
			S1.setPosition(d2);
			
			
			Wire W2=new Wire(temp2.getLocation(),S1.getLocation());
			
			W2.setParent(temp2);
			W2.setChild(S1);
			wires.add(W2);
			S1.AddEnteringWire(W2);
			tempList.add(S1);
			++numOfNodes;
			temp=d2;
			temp2=S1;
		}
		Dot d=new Dot(p.getInput(p.getNextEmptyPin()));
		
		Wire W1=new Wire(temp2.getLocation(),p.getInput(p.getNextEmptyPin()));
		
		W1.setParent(temp2);
		W1.setChild(p);
		//W1.setNumOfPinInput(1);
		wires.add(W1);	
	
		p.AddEnteringWire(W1);
		
		
		
		
	}
	public List<Symbol>getSymbols()
	{
		return this.symbols;
	}
	public List<Wire>getWires()
	{
		return this.wires;
	}
	public List<Dot>getDots()
	{
		return this.dots;
	}
	public int getHeight()
	{
		return this.height;
	}
	public int getWidth()
	{
		return this.width;
	}
	public void setDots(List<Dot>L)
	{
		for(int i=0;i<L.size();++i)
		{
			this.dots.get(i).setXandY(L.get(i).getX(),L.get(i).getY());
		}
	}
	public void setSymbols(List<Symbol>L)
	{
		for(int i=0;i<L.size();++i)
		{
			this.symbols.get(i).setPosition(L.get(i).getCenterDot());
		}
	}
	public void setWires(List<Wire>L)
	{
		for(int i=0;i<L.size();++i)
		{
			this.wires.get(i).setStart(L.get(i).getStart());
			this.wires.get(i).setEnd(L.get(i).getEnd());
		}
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
	/*
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
	/*
	 * Function returns the number of the intersection of the Draw.
	 * @see DoWiresCross
	 * @return int
	 */
	public int getWiresCrossing()
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
	/*
	 * Minimum Node Distance (Nbr of Nodes * Min. Node Distance2): This term helps
	 * in distributing the nodes. The square of minimum node distance is multiplied by the
	 * number of nodes.
	 */
	void setMinimumNodeDistance()
	{
		
	}
	/*
	 * Minimum Node Distance Sum (Min. Node Dist. Sum): The distance of each node
	 * from its nearest neighbour is measured, and the distances are added up. The bigger
	 * the sum the more evenly the nodes are usually distributed over the drawing area.
	 * @return void
	 */
	void setMinimumNodeDistanceSum()
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
	public void setEvaluationVariables()
	{
		this.edgeCrossing=getWiresCrossing();
		this.edgeLengthDeviation=getEdgeLengthDeviation();
		setMinimumNodeDistance();
		setMinimumNodeDistanceSum();
		
	}
	public double getEvaluationFunction()
	{
		setEvaluationVariables();
		//System.out.println(minimumNodeDistance+" "+minimumNodeDistanceSum+" "+edgeLengthDeviation+" "+edgeCrossing);
		return 390 * edgeCrossing
				+edgeLengthDeviation
				-minimumNodeDistanceSum;
	}
	
}
