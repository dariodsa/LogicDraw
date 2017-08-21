package hr.fer.GeneticAlgorithm;
import hr.fer.Geometric.*;
import hr.fer.League.Player;
import hr.fer.Parsing.Parser;
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
	
	public List<Integer>bitmask1=new ArrayList<>();
	public List<Integer>bitmask2=new ArrayList<>();
	
	private int height=500;
	private int width=1000;
	
	private List<Symbol>temp_list=new ArrayList<>();
	private static Random rand=new Random(System.currentTimeMillis());
	public String postfix;
	
	double minimumNodeDistanceSum;
	double minimumNodeDistance;
	double edgeLengthDeviation;
	double totalDistance;
	int edgeCrossing;
	
	private double distance=0;
	
	public Draw(int height,int width)
	{
		this.height=height;
		this.width=width;
	}
	public Draw(String postfix,int height,int width,List<Integer>bitmask,List<Integer>bitmask2)
	{
		this(height,width);
		this.postfix=postfix;
		
		this.bitmask1=new ArrayList<>(bitmask);
		this.bitmask2=new ArrayList<>(bitmask2);
		while(checkbitmasks()){}
		
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
					Symbol P2=new Symbol(Symbols.NOT);
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
						P2=new Symbol(Symbols.OR);
					else
						P2=new Symbol(Symbols.AND);
					
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
				Symbol s=new Symbol(Symbols.INPUT);
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
		Symbol out=new Symbol(Symbols.OUTPUT);
		out.setName("Out");
		out.addParent(P);
		P.child=out;
		symbols.add(out);
		
		
		TopoSort();
		generate();
		setEvaluationVariables();
		
	}
	
	public Draw duplicate()
	{
		
		Draw D=new Player(550, 1100);
		D.postfix=postfix;
		D.setWires(new ArrayList<Wire>(wires));
		D.setSymbols(new ArrayList<Symbol>(symbols));
		D.setBitmask1(getbitmask1());
		D.setBitmask2(getbitmask2());
		
		D.minimumNodeDistance=this.minimumNodeDistance;
		D.minimumNodeDistanceSum=this.minimumNodeDistanceSum;
		D.edgeCrossing=this.edgeCrossing;
		D.totalDistance=this.totalDistance;
		D.edgeLengthDeviation=this.edgeLengthDeviation;
		
		return D;
	}
	public boolean checkbitmasks()
	{
		boolean k=false;
		for(int i=0;i<bitmask1.size();++i)
		{
			for(int j=0;j<bitmask1.size();++j)
			{
				if(j!=i && bitmask1.get(i)==bitmask1.get(j) && bitmask2.get(i)==bitmask2.get(j))
				{
					int br=rand.nextInt();
					if(br%2==0)bitmask1.set(i,bitmask1.get(i)+1);
					else bitmask1.set(i,bitmask1.get(i)-1);
					k=true;
				}
			}
		}
		return k;
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
		{
			int i=1;
			S.getParents().sort(new Comparator<Symbol>() {

				@Override
				public int compare(Symbol o1, Symbol o2) {
					return Integer.compare(o1.getLocation().getY(), o2.getLocation().getY());
				}
				
			});
			//System.out.println("Novi simbol "+S.getType());
			for(Symbol parent: S.getParents())
			{
				//System.out.println("Nova lokacija: "+parent.getLocation()+ " " + parent.getOutputDot()+ " " +parent.getType()+" "+parent.getName());
				generateWires(parent,i++, S);
			}
		}
		
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
	public void setBitmask1(List<Integer>bitmask)
	{
		this.bitmask1=new ArrayList<Integer>(bitmask);
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
			Dot center=new Dot(bitmask2.get(pos)*48+150,
					           bitmask1.get(pos)*15+100);
			
			if(S.getType()==Symbols.INPUT)center.setX(100);  
			if(S.getType()==Symbols.OUTPUT)center.setX(1310);
			
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
	private void generateWires(Symbol p2,int id, Symbol p)
	{
		
		int pin=p.getNextEmptyPin();
		/*if(p.isSymbolOrAndType())
		{
			System.out.println("PO "+p.getParent(3-id).getLocation()+" , "+p.getParent(id).getLocation());
			if(p.getNextEmptyPin()==1)if(p.getParent(3-id).getLocation().getY()>p.getParent(id).getLocation().getY())
				pin=1;
			else pin=2;
			else pin=2;
		}*/
		if(p2.isSymbolOutInType())p2.setPosition(new Dot(100,p.getInput(pin).getY()));
		//System.out.println(id+ " " +pin);
		Dot temp=p2.getOutputDot();
		Symbol temp2=p2;
		rand.setSeed(System.currentTimeMillis());
		for(int i=1,r1=rand.nextInt(3);i<1+r1;++i)
		{
			//System.out.println(temp+"+"+ p.getInput(pin));
			//System.out.println(temp+" do "+p.getInput(pin));
			Dot d2=Dot.getRandomDot(temp, p.getInput(pin));
			//System.out.println("Dobiveno je : "+d2);
			//System.out.println(d2);
			Symbol S1=new Symbol(Symbols.EDGE);
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
	public List<Integer> getbitmask1()
	{
		return this.bitmask1;
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
				
				/*if(wires.get(i).getParent().getType()==Symbols.GRID && 
				   wires.get(i).getChild().getType()==Symbols.GRID &&
				   wires.get(j).getParent().getType()==Symbols.GRID &&
				   wires.get(j).getChild().getType()==Symbols.GRID)continue;*/
				
				/*if(Math.max(wires.get(i).getEnd().getX(),wires.get(i).getStart().getX())<
						Math.min(wires.get(j).getStart().getX(),wires.get(j).getEnd().getX()))
					break;*/
				
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
	
	public double getYLength()
	{
		double ans=0;
		for(Wire w:wires)
		{
			if(w.isVisible())
			{
				ans+=Math.abs(w.getStart().getY()-w.getEnd().getY());
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
			for(int j=i+1;j<len;++j)
			{
				//if(i==j)continue;
				mina=Math.min(mina,Geometric.distance(symbols.get(i).getCenterDot(), symbols.get(j).getCenterDot()));
			}
			ans+=mina;
		}
		this.minimumNodeDistanceSum=ans;
	}
	/**
	 * Minimum Node Distance Sum (Min. Node Dist. Sum): The distance of each node
	 * from its nearest neighbour is measured, and the distances are added up. The bigger
	 * the sum the more evenly the nodes are usually distributed over the drawing area.
	 */
	public double getMinimumNodeDistance()
	{
		return this.minimumNodeDistanceSum;
	}
	/**
	 * Returns the sum of the wires euclid distance.
	 * @return sum of the total distance in the draw
	 */
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
		
		
		
		
		//forceDirected();
		
	}
	public void podesavanje()
	{
		/*int ko=10;
		while(ko-->=0)
		{
			for(Symbol S: symbols)
			{
				if(S.getType()==Symbols.EDGE){
					double di=S.disp.distance();
					S.setPosition(new Dot(
									(int)(S.getLocation().getX()+S.disp.x/di*0.5),
									(int)(S.getLocation().getY()+S.disp.y/di*0.5)
							));
				}
				
			}
			forceDirected();
		}*/
		return;
	}
	/**
	 * Graph Drawing by Force-directed Placement
THOMAS M. J. FRUCHTERMAN* AND EDWARD M. REINGOLD
Department of Computer Science, University of Illinois at Urbana-Champaign, 1304 W.
	Springfield Avenue, Urbana, IL 61801-2987, U.S.A.
	 * @return
	 */
	public double forceDirected()
	{
		int numNodes=Parser.getNumOfNodes(this.postfix);
		numNodes=symbols.size();
		distance=0.11033*Math.sqrt((double)((this.width-150)*(this.height-150))/numNodes);
		double ans=0;
		//System.out.println(numNodes);
		for(Symbol S:symbols)S.disp=new VVector(0,0);
		for(int i=0;i<symbols.size();++i)
		{
			if(symbols.get(i).isSymbolOrAndType() || symbols.get(i).getType()==Symbols.NOT || symbols.get(i).isSymbolOutInType() || symbols.get(i).getType()==Symbols.EDGE )
			for(int j=0;j<symbols.size();++j)
			{
				if(i==j)continue;
				if(!(symbols.get(j).isSymbolOrAndType() || symbols.get(j).getType()==Symbols.NOT || symbols.get(j).isSymbolOutInType() || symbols.get(j).getType()==Symbols.EDGE ))continue;
				VVector pos1=new VVector(symbols.get(i).getLocation().getX(),
							symbols.get(i).getLocation().getY());
				VVector pos2=new VVector(symbols.get(j).getLocation().getX(),
						symbols.get(j).getLocation().getY());
			
				VVector diff=VVector.diff(pos1, pos2);
				double kon=1;
				double CON=0.266;
				if(symbols.get(i).getType()==Symbols.EDGE || symbols.get(j).getType()==Symbols.EDGE)kon=0.03252;
				double FR=CON*CON*kon*kon*distance*distance/diff.distance();
				
				diff.normalize();
				diff.multi(FR);
				symbols.get(i).disp=VVector.add(symbols.get(i).disp,diff);
			}
		}
		for(int i=0;i<wires.size();++i)
		{
			if(wires.get(i).isVisible()==false)continue;
			Symbol S1=wires.get(i).getParent();
			Symbol S2=wires.get(i).getChild();
			if(!(S1.isSymbolOrAndType() || S1.getType()==Symbols.NOT || S1.isSymbolOutInType() || S1.getType()==Symbols.EDGE))continue;
			if(!(S2.isSymbolOrAndType() || S2.getType()==Symbols.NOT || S2.isSymbolOutInType() || S2.getType()==Symbols.EDGE))continue;
			VVector diff=VVector.diff(S1.disp, S2.disp);
			double ko=1;
			double FA;
			if(S1.getType()==Symbols.EDGE || S2.getType()==Symbols.EDGE)
			{
				ko=0.00325;
				FA=ko*(1500-diff.distance())*(1500-diff.distance())/distance;
			}
			else 
				FA=ko*diff.distance()*diff.distance()/distance;
			diff.normalize();
			diff.multi(FA);
			S1.disp=VVector.diff(S1.disp,diff);
			S2.disp=VVector.add(S2.disp,diff);
		}
		for(Symbol S:symbols)
		{
			ans+=S.disp.distance();
			
		}
		return ans;
	}
	public double getEvaluationFunction()
	{
		//setEvaluationVariables();
		//System.out.println(minimumNodeDistance+" "+minimumNodeDistanceSum+" "+edgeLengthDeviation+" "+edgeCrossing);
		return 5E140 * edgeCrossing
				+(forceDirected());
	}
	
	public int compareToa(Draw o) {
		if(edgeCrossing==o.edgeCrossing)
		{
			//totalDistance
			//minimumNodeDistanceSum
			double x1=totalDistance/o.totalDistance;
			double x2=minimumNodeDistanceSum/o.minimumNodeDistanceSum;
			//return Double.compare((x1+x2)/2.0, 1.0);
			return Double.compare(this.forceDirected(),o.forceDirected());
		}
		return Integer.compare(edgeCrossing, o.edgeCrossing);
		
	}
	
}
