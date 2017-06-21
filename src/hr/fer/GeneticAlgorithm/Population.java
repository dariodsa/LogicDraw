package hr.fer.GeneticAlgorithm;
import java.util.*;

import hr.fer.DrawObjects.*;
import hr.fer.MainPart.*;

public class Population 
{
	List<Draw> draws=new ArrayList<>();
	public static int crossoverRate=4;
	public static int mutationRate=42;
	public Population(List<Draw> draws)
	{
		setNewGeneration(draws);
		
	}
	/**
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
	/**
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
	/**
	 * This function generates new generation based on the selection of the parents.
	 * Currently all constants are hard-coded, but this will change soon.
	 * @return void
	 */
	public void generateNewGeneration()
	{
		int size=draws.size();
		evaluate();
		List<Draw>kids=new ArrayList<>();
		for(int i=size-1;i>=size/2;--i)
		{
			Draw D=draws.get(i);
			kids.add(D.duplicate());
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
			//addNewDraw(new Draw(postfix,550,1100));
		}
	}
	private void killTheGeneration()
	{
		draws.clear();
		return;
	}
	/**
	 * Get the best draw from the population using the evaluation function.
	 * @param void
	 * @return Draw - best draw  
	 */
	public Draw getBestDrawFromPopulation()
	{
		if(draws.size()==0)
			return null;
		Draw ans=draws.get(0);
		double brAns=ans.getEvaluationFunction();
		for(Draw D : draws)
		{
			double temp=D.getEvaluationFunction();
			if(temp<brAns)
			{
				ans=D;
				brAns=temp;
			}
		}
		return ans;
	}
	/**
	 * Mutate the single draw.
	 * @param Draw D
	 * @return void
	 */
	public void muttation(Draw D)
	{
		Random rand=new Random();
		int pos=rand.nextInt(35);
		
		if(pos<=18)GeneticOperations.SingleMutate(D, 18);
		else if(pos>18 && pos<=18+5)GeneticOperations.EdgeMutation2(D);
		//else 
		
		D.setEvaluationVariables();
		
		/*else
		{
			GeneticOperations.ChangeInputPins(D);
		}*/
		
	}
	/**
	 * Sorts the draws in the asc order using the evaluation function.
	 * @return void
	 */
	public void evaluate()
	{
		draws.sort(new Comparator<Draw>() {

			@Override
			public int compare(Draw o1, Draw o2) {
				//return Double.compare(o1.getEvaluationFunction(), o2.getEvaluationFunction());
				if(o1.getNumWiresCrossing()==o2.getNumWiresCrossing())
				{
					return Double.compare(o1.getTotalDistance()+o1.getMinimumNodeDistance(),
										  o2.getTotalDistance()+o2.getMinimumNodeDistance());
				}
				else return Integer.compare(o1.getNumWiresCrossing(), o2.getNumWiresCrossing());
			}
		});
		
	}
	
	/**
	 * Adds new draw to the population.
	 * @param Draw d
	 * @return void
	 */
	public void addNewDraw(Draw d)
	{
		draws.add(d);
	}
	/**
	 * Generate new Draw based on the parents.
	 * @param Draw parent1
	 * @param Draw parent2
	 * @return Draw 
	 */
	public Draw generateNewDraw(Draw parent1,Draw parent2)
	{
		return null;
	}
	/**
	 * Selects some draw from the whole population. 
	 * This is actually, a function to choose a parent.
	 */
	public Draw selectOneDraw()
	{
		return null;
	}
	
}
