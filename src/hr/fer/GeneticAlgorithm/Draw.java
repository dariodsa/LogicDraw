package hr.fer.GeneticAlgorithm;
import hr.fer.Geometric.*;
import hr.fer.DrawObjects.*;

import javax.swing.*;

import java.awt.geom.Line2D;
import java.util.*;
import java.util.function.Predicate;

public class Draw implements Comparable<Draw> 
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
	
	double minimumNodeDistanceSum;
	double minimumNodeDistance;
	double edgeLengthDeviation;
	double totalDistance;
	int edgeCrossing;
	
	public Draw(int height,int width)
	{
		this.height=height;
		this.width=width;
	}
	public Draw(String postfix,int height,int width,List<Integer>bitmask,List<Integer>bitmask2)
	{
		this(height,width);
		this.postfix=postfix;
		
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
					P.child=P2;
					
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
					P.child=P2;
					P1.child=P2;
					
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
		
		Draw D=new Draw(550, 1100);
		D.postfix=postfix;
		D.setWires(new ArrayList<Wire>(wires));
		D.setSymbols(new ArrayList<Symbol>(symbols));
		D.setBitmask(getbitmask());
		D.setBitmask2(getbitmask2());
		
		D.minimumNodeDistance=this.minimumNodeDistance;
		D.minimumNodeDistanceSum=this.minimumNodeDistanceSum;
		D.edgeCrossing=this.edgeCrossing;
		D.totalDistance=this.totalDistance;
		D.edgeLengthDeviation=this.edgeLengthDeviation;
		
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
			if(simbol.getType()==Symbols.OUTPUT)
				simbol.setDepth(37, true);
		}
		
		symbols.sort((t1,t2)->Integer.compare(t1.getDepth(),t2.getDepth()));
		
	}
	
	public void generate()
	{
		addCentralDots();
		
		for(Symbol S:symbols)
		{
			for(Symbol S1:symbols)
			{
				if(S.isSymbolOutInType() && S1.isSymbolOutInType() && S.getName()!=S1.getName() && Math.abs(S.getLocation().getY()-S1.getLocation().getY())<15)
				{
					S1.setPosition(new Dot(100,S1.getLocation().getY()+40));
				}
			}
		}
		
		for(Symbol S: symbols)
		
			for(Symbol parent: S.getParents())
				generateWires(parent, S);
			
		for(Symbol S:temp_list)
			symbols.add(S);
		
		rotatePins();
	}
	public void setSymbols(List<Symbol>symbols)
	{
		this.symbols=new ArrayList<Symbol>(symbols);
	}	
	public void setWires(List<Wire>wires)
	{
		this.wires=new ArrayList<Wire>(wires);
	}
	public void setBitmask(List<Integer>bitmask)
	{
		this.bitmask=new ArrayList<Integer>(bitmask);
	}
	public void setBitmask2(List<Integer>bitmask2)
	{
		this.bitmask2=new ArrayList<Integer>(bitmask2);
	}
	/**
	 * It rotates the input pins in order to minimize the intersections.
	 */
	public void rotatePins()
	{
		for(Symbol S: symbols)
		{
			if(S.isSymbolOrAndType())
			{
				Wire w1=S.getEnteringWire(0);
				Wire w2=S.getEnteringWire(1);
				if(DoWiresCross(w1, w2))
				{
					w1.setInputPin(3-w1.getInputPin());
					w2.setInputPin(3-w2.getInputPin());
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
			Dot center=new Dot(bitmask2.get(pos)*35+150,
					           bitmask.get(pos)*15+100);
			
			if(S.getType()==Symbols.INPUT)center.setX(100);  
			if(S.getType()==Symbols.OUTPUT)center.setX(1050);
			
			if(S.getType()==Symbols.OUTPUT)center.setY(S.getParent(1).getCenterDot().getY());
			
			S.setPosition(center);
			
			if(S.isSymbolOutInType()) //The input and output dots can't be moved in the x direction, only in the y direction.
				S.getCenterDot().setCanYouMoveIt(false);
			
			addInVisibleWireAroundTheSymbol(S);
			
			if(S.isSymbolOrAndType() || S.getType()==Symbols.NOT)++pos;
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
		Wire w3=new Wire(S.gridDots[3],S.gridDots[2],1,false);
		Wire w4=new Wire(S.gridDots[3],S.gridDots[0],1,false);
		
		wires.add(w1);wires.add(w2);wires.add(w3);wires.add(w4);
		return;
	}
	private void generateWires(Symbol p2, Symbol p)
	{
		
		int pin=p.getNextEmptyPin();
		
		Dot temp=p2.getLocation();
		Symbol temp2=p2;
		for(int i=0,r1=rand.nextInt(2);i<r1;++i)
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
	
		if(p2.isSymbolOutInType())p2.setPosition(new Dot(100,W1.getEnd().getY()));
		
		p.AddEnteringWire(W1);
	}
	public List<Wire>getWires()
	{
		return this.wires;
	}
	public List<Integer> getbitmask()
	{
		return this.bitmask;
	}
	public List<Integer> getbitmask2()
	{
		return this.bitmask2;
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
		wires.sort(new Comparator<Wire>() {

			@Override
			public int compare(Wire o1, Wire o2) {
				
				return Integer.compare(o1.getStart().getX(), o2.getStart().getX());
			}
		});
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
				
				if(Math.max(wires.get(i).getEnd().getX(),wires.get(i).getStart().getX())<
						Math.min(wires.get(j).getStart().getX(),wires.get(j).getEnd().getX()))
					break;
				
				if(DoWiresCross(wires.get(i),wires.get(j)))
				{
					++ans;
				}
			}
		}
		
		int k=0;
		for(int i=0;i<postfix.length();++i)
		{
			if(postfix.charAt(i)>='a' && postfix.charAt(i)<='z')++k;
		}
		
		return ans-k-1;
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
			for(int j=i+1;j<len;++j)
			{
				//if(i==j)continue;
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
	@Override
	public int compareTo(Draw o) {
		if(edgeCrossing==o.edgeCrossing)
		{
			//totalDistance
			//minimumNodeDistanceSum
			double x1=totalDistance/o.totalDistance;
			double x2=minimumNodeDistanceSum/o.minimumNodeDistanceSum;
			return Double.compare((x1+x2)/2.0, 1.0);
		}
		return Integer.compare(edgeCrossing, o.edgeCrossing);
		
	}
	
}
