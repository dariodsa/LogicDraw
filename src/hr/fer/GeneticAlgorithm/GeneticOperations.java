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
		
		int br=rand.nextInt(Parser.getNumOfNodes(D.postfix));
		int i=0;
		for(Symbol S: D.getSymbols())
		{
			if(S.isSymbolOrAndType() || S.getType()==Symbols.NOT)
			{
				if(i==br)
				{
					if(rand.nextInt(12)%2==0)
					{
						if(S.child.getDepth()-S.getDepth()>1)
							S.setDepth(S.getDepth()+1, true);
					}
					else 
					{
						//lijevo
						boolean k=true;
						for(Symbol parent : S.getParents())
						{
							if(S.getDepth()-1==parent.getDepth())k=false;
						}
						if(k)S.setDepth(S.getDepth()-1, true);
					}
					break;
				}
				++i;
			}
		}
	}
	
	
}
