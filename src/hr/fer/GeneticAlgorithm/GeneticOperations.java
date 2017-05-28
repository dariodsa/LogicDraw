package hr.fer.GeneticAlgorithm;
import hr.fer.DrawObjects.*;
import hr.fer.Geometric.Geometric;

import java.util.*;

import com.sun.javafx.geom.Vec2d;

public class GeneticOperations 
{
	private static Random rand=new Random();
	
	/*
	 * Function returns true if the square of a size squareSize around Dot position is empty.
	 * @Draw D
	 * @param Dot position
	 * @param int squareSize
	 * @return boolean
	 */
	public static boolean isTheSquareClear(Draw D,Dot position,int squareSize)
	{
		List<Symbol>symbols=D.getSymbols();
		for(Symbol S: symbols)
		{
			Dot pos=S.getCenterDot();
			if(  Math.abs(pos.getX()-position.getX())<=squareSize/2 &&
				 Math.abs(pos.getY()-position.getY())<=squareSize/2
					)
				return false;
		}
		return true;
	}
	/*
	 * Function returns the list of the symbols that are in the specific square.
	 * @Draw D
	 * @param Dot position
	 * @param int squareSize
	 * @return List<Symbol>
	 */
	public static List<Symbol> symbolsInTheSquare(Draw D,Dot position,int squareSize)
	{
		List<Symbol>symbols=D.getSymbols();
		List<Symbol>resSymbols=new ArrayList<>();
		for(Symbol S: symbols)
		{
			Dot pos=S.getCenterDot();
			if(  Math.abs(pos.getX()-position.getX())<=squareSize/2 &&
				 Math.abs(pos.getY()-position.getY())<=squareSize/2
					)
				resSymbols.add(S);
		}
		return resSymbols;
	}
	/*
	 * Choose a random edge and move it to a random new position.
	 * @param Draw D
	 * @return void
	 */
	public static void EdgeMutation1(Draw D)
	{
		List<Wire>wires=D.getWires();
		int br=rand.nextInt(wires.size());
		for(int i=br,len=wires.size();i<len;i=(i+1)%len)
		{
			if(wires.get(i).isVisible())
			{
				Dot start=wires.get(i).getStart();
				Dot end=wires.get(i).getEnd();
				
				Vec2d randVector=Geometric.returnVector();
				wires.get(i).getStart()
							.setXandY(
									wires.get(i).getStart().getX()+(int)randVector.x, 
									wires.get(i).getStart().getY()+(int)randVector.y);
				
				Vec2d randVector2=Geometric.returnVector();
				wires.get(i).getEnd()
							.setXandY(
									wires.get(i).getEnd().getX()+(int)randVector2.x, 
									wires.get(i).getEnd().getY()+(int)randVector2.y);
				return;
			}
		}
	}
	/*
	 *  Like EdgeMutation-1, but the length and angle of the edge is kept unchanged.
	 * @param Draw D
	 * @return void
	 */
	public static void EdgeMutation2(Draw D)
	{
		List<Wire>wires=D.getWires();
		int br=rand.nextInt(wires.size());
		for(int i=br,len=wires.size();i<len;i=(i+1)%len)
		{
			if(wires.get(i).isVisible())
			{
				Dot start=wires.get(i).getStart();
				Dot end=wires.get(i).getEnd();
				
				Vec2d randVector=Geometric.returnVector();
				wires.get(i).getStart()
							.setXandY(
									wires.get(i).getStart().getX()+(int)randVector.x, 
									wires.get(i).getStart().getY()+(int)randVector.y);
				
				
				wires.get(i).getEnd()
							.setXandY(
									wires.get(i).getEnd().getX()+(int)randVector.x, 
									wires.get(i).getEnd().getY()+(int)randVector.y);
				
				return;
			}
		}
	}
	/*
	 *  Like EdgeMutation-1, but the length and angle of the edge is kept unchanged.
	 *  In this function we gave specific edge that we want to be moved and vector also.
	 * @param Draw D
	 * @param intger pos
	 * @param Vector<Integer> MovingVector
	 * @return void
	 */
	public static void EdgeMutation2(Draw D,int pos,Vector<Integer> MovingVector)
	{
		
	}
	/*
	 * Change inputpins on the specific symbol in the pos position in the list of the symbols.
	 * @param Draw D
	 * @param int pos
	 * @return void
	 */
	public static void ChangeInputPinsOfSymbol(Draw D,int pos)
	{
		
	}
	/*
	 * Change inputpins on the some random OR / AND symbol.
	 * @param Draw D
	 * @return void
	 */
	public static void ChangeInputPins(Draw D)
	{
		
	}
	/*
	 * Choose a random node and move it to a random empty square.
	 * @param Draw D
	 * @param int squareSize
	 * @return void
	 */
	public static void SingleMutate(Draw D, int squareSize)
	{
		
	}
	/*
	 * Like EdgeMutation-2(length and angle of the edge is kept unchanged), but two edges incident with a same node are moved.
	 * @param Draw D
	 * @return void
	 */
	public static void TwoEdgeMutation(Draw D)
	{
		Random rand=new Random();
		Vector<Integer>MovingVector=new Vector<>();
		MovingVector.addElement(rand.nextInt(100));
		MovingVector.addElement(rand.nextInt(100));
		
		List<Wire>wires=D.getWires();
		int br=0;
		/*while(br<1500)
		{
			int pos=rand.nextInt(wires.size());
			int pos2=rand.nextInt(wires.size());
			if(pos==pos2)continue;
			if(wires.get(pos).getChild()==null || wires.get(pos2).getChild()==null || wires.get(pos).getParent()==null || wires.get(pos2).getParent()==null)
				continue;
			
			if(wires.get(pos).getParent().id==wires.get(pos2).getChild().id ||
				wires.get(pos2).getParent().id==wires.get(pos).getChild().id)
			{
				 EdgeMutation2(D, pos, MovingVector);
			 	 EdgeMutation2(D, pos2, MovingVector);
				 return;
			}
			++br;
		}*/
	}
	/*
	 * Choose randomly two squares from the drawing area such that at least one of them contains a node. 
	 * If both contain a node, exchange the nodes. 
	 * If only one of them contains a node, then move the node from the present location to the empty square. 
	 * @param Draw D
	 * @param int squareSize
	 * @return void
	 */
	public static void SmallMutate(Draw D,int squareSize)
	{
		
	}
}
