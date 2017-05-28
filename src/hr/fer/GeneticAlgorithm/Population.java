package hr.fer.GeneticAlgorithm;
import java.util.*;

import hr.fer.DrawObjects.*;
import hr.fer.MainPart.*;

public class Population 
{
	int generation;
	List<Draw> draws=new ArrayList<>();
	public static int crossoverRate=4;
	public static int mutationRate=42;
	public Population(List<Draw> draws,int numOfGeneration)
	{
		setNewGeneration(draws);
		
	}
	/*
	 * Return the best evaluation function in the given population.
	 * @return double
	 */
	public double getBestPerform()
	{
		double ans=1;
		for(Draw D: draws)
			ans=Math.max(ans,D.getEvaluationFunction());
		
		return ans;
	}
	/*
	 * Sets the new generation, which was received in the call of the function.
	 * @param List<Draw> draws
	 * @return void
	 */
	private void setNewGeneration(List<Draw>draws)
	{
		killTheGeneration();
		for(Draw D: draws)
		{
			addNewDraw(D);
		}
	}
	/*
	 * This function generates new generation based on the selection of the parents.
	 * Currently all constants are hard-coded, but this will be changed.
	 * @return void
	 */
	public void generateNewGeneration()
	{
		int size=draws.size();
		evaluate();
		List<Draw>kids=new ArrayList<>();
		for(int i=size-1;i>=size/2-4;--i)
		{
			Draw D=draws.get(i);
			kids.add(D);
			muttation(D);
			kids.add(D);
		}
		killTheGeneration();
		String postfix="";
		for(Draw D : kids)
		{
			addNewDraw(D);
			postfix=D.postfix;
		}
		for(int i=0;i<4;++i)
		{
			addNewDraw(new Draw(postfix,550,1100));
		}
	}
	private void killTheGeneration()
	{
		draws.clear();
		return;
	}
	/*
	 * Get the best draw from the population.
	 * @param void
	 * @return Draw
	 */
	public Draw getBestDrawFromPopulation()
	{
		if(draws.size()==0)
			return null;
		Draw ans=draws.get(0);
		int brAns=ans.getWiresCrossing();
		for(Draw D : draws)
		{
			int temp=D.getWiresCrossing();
			if(temp<brAns)
			{
				ans=D;
				brAns=temp;
			}
		}
		return ans;
	}
	public void muttation(Draw D)
	{
		Random rand=new Random();
		int pos=rand.nextInt(65);
		
		if(pos<=12)GeneticOperations.TwoEdgeMutation(D);
		if(pos>12 && pos<=12+10)GeneticOperations.EdgeMutation2(D);
		if(pos>12+10 && pos<=12+10+10)GeneticOperations.SingleMutate(D, 100);
		if(pos>12+10+10 && pos<=12+10+10+5)GeneticOperations.EdgeMutation1(D);
		if(pos>12+10+10+5 && pos<=12+10+10+5+5)GeneticOperations.SmallMutate(D, 100);
		else
		{
			GeneticOperations.ChangeInputPins(D);
		}
		
	}
	public void evaluate()
	{
		draws.sort(new Comparator<Draw>() {

			@Override
			public int compare(Draw o1, Draw o2) {
				return Double.compare(o1.getEvaluationFunction(), o2.getEvaluationFunction());
			}
		});
		
	}
	
	/*
	 * Adds new draw to the population.
	 * @param Draw d
	 * @return void
	 */
	public void addNewDraw(Draw d)
	{
		draws.add(d);
	}
	/*
	 * Generate new Draw based on the parents.
	 * @param Draw parent1
	 * @param Draw parent2
	 * @return Draw 
	 */
	public Draw generateNewDraw(Draw parent1,Draw parent2)
	{
		return null;
	}
	/*
	 * Selects some draw from the whole population. 
	 * This is actually, a function to choose a parent.
	 */
	public Draw selectOneDraw()
	{
		return null;
	}
	
}
