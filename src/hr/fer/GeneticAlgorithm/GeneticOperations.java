package hr.fer.GeneticAlgorithm;
import hr.fer.Parsing.*;
import hr.fer.DrawObjects.*;
import hr.fer.Geometric.Geometric;

import java.util.*;

import com.sun.javafx.geom.Vec2d;

public class GeneticOperations 
{
	private static Random rand=new Random();
	
	public static void moveItLeftOrRight(Draw D)
	{
		rand.setSeed(System.currentTimeMillis());
		int br=rand.nextInt(Parser.getNumOfNodes(D.postfix)-1);
		int i=0;
		int cnt=0;
		for(Symbol S: D.getSymbols())
		{
			++cnt;
			if(S.isSymbolOrAndType() || S.getType()==Symbols.NOT)
			{
				if(i==br)
				{
					if(rand.nextInt(12)%2==0)
					{
						//System.out.println("Desno "+S.child.getDepth()+" --> "+S.getDepth());
						if(S.child!=null)
						if(S.child.getDepth()-S.getDepth()>1)
						{
							S.setDepth(S.getDepth()+1, true);
							//if(S.getDepth()==24)System.out.println(D.forceDirected()+ "  :-)");
							//System.out.println("Idemo desno "+S.getDepth()+" --> "+D.forceDirected());
						}
					}
					else 
					{
						//lijevo
						//System.out.println("lijevo "+S.child.getDepth()+" --> "+S.getDepth());
						boolean k=true;
						for(Symbol parent : S.getParents())
						{
							if(S.getDepth()-1==parent.getDepth())k=false;
						}
						if(k && S.getParents().size()>0)
						{
							S.setDepth(S.getDepth()-1, true);
						}
					}
					D.bitmask2.set(i, S.getDepth());
					break;
				}
				++i;
			}
		}
	}
	public static void moveItUpperOrDown(Draw D)
	{
		rand.setSeed(System.currentTimeMillis());
		int br=rand.nextInt(Parser.getNumOfNodes(D.postfix)-1);
		rand.setSeed(System.currentTimeMillis());
		
		int i=0;
		for(Symbol S: D.getSymbols())
		{
			if(S.isSymbolOrAndType() || S.getType()==Symbols.NOT)
			{
				if(br==i)
				{
					int broj=(int)rand.nextInt(7);
					if(broj%2==0)
					{
						broj-=3;
						D.bitmask1.set(i, D.bitmask1.get(i)+broj);
						if(D.bitmask1.get(i)>25	)D.bitmask1.set(i, 25);
						if(D.bitmask1.get(i)<0)D.bitmask1.set(i,0);
					}
					else 
					{
						broj=rand.nextInt(25);
						D.bitmask1.set(i,broj);
					}
					
					break;
				}
				
				++i;	
			}
		}
	}
	
}
