package hr.fer.GeneticAlgorithm;
import java.util.*;

import hr.fer.DrawObjects.*;
import hr.fer.MainPart.*;

public class Population 
{
	List<Draw> draws=new ArrayList<>();
	public static int crossoverRate=4;
	public static int mutationRate=42;
	
	private static Random rand=new Random();
	
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
		double ans=115651561651.56;
		for(Draw D: draws)
			ans=Math.min(ans,D.getEvaluationFunction());
		
		return ans;
	}
	/**
	 * Sets the new generation, but first we kill the previous. 
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
		int size=getPopulationSize();
		evaluate();
		List<Draw>kids=new ArrayList<>();
		
		
		int kol=size/5;
		
		for(int i=0;i<kol;++i)  // The best one in the generation
			kids.add(draws.get(i).duplicate());
		
		for(int i=0;i<size/3;++i)
		{
			Draw D=selectOneDraw().duplicate();
			muttation(D);
			kids.add(D);
		}
		for(int i=kids.size();i<size;++i)
		{
			Draw D1=selectOneDraw().duplicate();
			Draw D2=selectOneDraw().duplicate();
			Draw kid=generateNewDraw(D1,D2);
			//System.out.println(kid.getEvaluationFunction()+" <-- "+D1.getEvaluationFunction()+" "+D2.getEvaluationFunction());
			kids.add(kid);
		}
		
		setNewGeneration(kids);
	}
	/**
	 * Kills the entire generation.
	 * @return void
	 */
	public void killTheGeneration()
	{
		this.draws.clear();
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
		
		//if(pos<28)
			GeneticOperations.moveItLeftOrRight(D);
		//if(rand.nextInt(35)<28)
			GeneticOperations.moveItUpperOrDown(D);
		D=new Draw(D.postfix,D.getHeight(),D.getWidth(),D.getbitmask(),D.getbitmask2());
		
	}
	/**
	 * Returns the population size.
	 * @return size population size
	 */
	public int getPopulationSize()
	{
		return this.draws.size();
	}
	/**
	 * Sorts the draws in the asc order using the evaluation function.
	 * @return void
	 */
	public void evaluate()
	{
		draws.sort((new Comparator<Draw>() {

			@Override
			public int compare(Draw d, Draw o) {
				if(d.edgeCrossing==o.edgeCrossing)
				{
					//totalDistance
					//minimumNodeDistanceSum
					double x1=d.totalDistance/o.totalDistance;
					double x2=d.minimumNodeDistanceSum/o.minimumNodeDistanceSum;
					return Double.compare((x1+x2)/2.0, 1.0);
				}
				return Integer.compare(d.edgeCrossing, o.edgeCrossing);
			}
		}));
		
	}
	
	/**
	 * Adds new draw to the population.
	 * @param Draw 
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
		List<Integer>bitmaskParent1=parent1.getbitmask();
		List<Integer>bitmaskParent2=parent2.getbitmask();
		
		int br=rand.nextInt(bitmaskParent1.size());
		
		List<Integer>bitmaskChild=new ArrayList<>();
		for(int i=0;i<br;++i)
			bitmaskChild.add(bitmaskParent1.get(i));
		for(int i=br;i<bitmaskParent2.size();++i)
			bitmaskChild.add(bitmaskParent2.get(i));
		
		if(br%2==0)
			return new Draw(parent1.postfix,parent1.getHeight(),parent1.getWidth(),bitmaskChild,parent1.getbitmask2());
		return new Draw(parent1.postfix,parent1.getHeight(),parent1.getWidth(),bitmaskChild,parent2.getbitmask2());
	}
	/**
	 * Returns some draw from the whole population. 
	 * This is actually, a function to choose a parent.
	 * @return Draw
	 */
	public Draw selectOneDraw()
	{
		//3*e^(-3x)
		
		double broj=(double)rand.nextInt(100);
		double x=Math.log((100.0-broj)/100)/(-3.0);
		
		double omjer=x/(1.5352);
		int br=(int)((double)draws.size()*omjer);
		
		return draws.get(br);
	}
	
}
