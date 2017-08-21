package hr.fer.GeneticAlgorithm;
import hr.fer.League.*;
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
		evaluate();
		rand.setSeed(System.currentTimeMillis());
	}
	/**
	 * Return the best evaluation function in the given population.
	 * @return double
	 */
	public double getBestPerform()
	{
		double ans=115651561651.56;
		
		return draws.get(0).getNumWiresCrossing();
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
		//evaluate();
		List<Draw>kids=new ArrayList<>();
		
		
		int kol=size/5;
		
		for(int i=0;i<kol;++i)  // The best one in the generation
			kids.add(draws.get(i).duplicate());
		
		for(int i=0;i<size/7;++i)
		{
			Draw D=selectOneDraw().duplicate();
			muttation(D);
			Draw D2=new Player(D.postfix,D.getHeight(),D.getWidth(),D.getbitmask1(),D.getbitmask2());
			//System.out.println("D2 "+D2.forceDirected());
			kids.add(D2);
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
		evaluate();
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
		return ans;
		//double brAns=ans.forceDirected();//.getEvaluationFunction();
		/*
		for(Draw D : draws)
		{
			double temp=D.forceDirected();//.getEvaluationFunction();
			if(D.compareTo(ans)<0)
			{
				ans=D;
				//brAns=temp;
			}
		}
		return ans;*/
	}
	/**
	 * Mutate the single draw.
	 * @param Draw D
	 * @return void
	 */
	public void muttation(Draw D)
	{
		Random rand=new Random();
		rand.setSeed(System.currentTimeMillis());
		int pos=rand.nextInt(35);
		
		//if(pos<28)
			GeneticOperations.moveItLeftOrRight(D);
		//if(rand.nextInt(35)<28)
			GeneticOperations.moveItUpperOrDown(D);
		
		
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
		/*draws.sort((new Comparator<Draw>() {

			@Override
			public int compare(Draw d, Draw o) {
				if(d.edgeCrossing==o.edgeCrossing)
				{
					//totalDistance
					//minimumNodeDistanceSum
					double x1=d.totalDistance/o.totalDistance;
					double x2=d.minimumNodeDistanceSum/o.minimumNodeDistanceSum;
					//return Double.compare((x1+x2)/2.0, 1.0);
					return Double.compare(d.forceDirected(), o.forceDirected());
					//return Double.compare(o.getYLength(), d.getYLength());
				}
				return Integer.compare(d.edgeCrossing, o.edgeCrossing);
			}
		}));*/
		
		LeagueTable liga=new LeagueTable(draws);
		liga.playTwoRounds();
		//draws.clear();
		List<Player> leagueOrder=liga.getLeagueOrder();
		int pos=0;
		draws.clear();
		
		for(Player P: leagueOrder)
		{
			draws.add(P);
		}
		
		//System.out.println(draws.get(0).getNumWiresCrossing() +" "+leagueOrder.get(0).getScore()+ " "+leagueOrder.get(1).getScore() );
	}
	
	/**
	 * Adds new Player to the population.
	 * @param Draw 
	 * @return void
	 */
	public void addNewDraw(Draw d)
	{
		draws.add(d);
	}
	/**
	 * Generate new Player based on the parents.
	 * @param Draw parent1
	 * @param Draw parent2
	 * @return Draw 
	 */
	public Draw generateNewDraw(Draw parent1,Draw parent2)
	{
		List<Integer>bitmaskParent1=parent1.getbitmask1();
		List<Integer>bitmaskParent2=parent2.getbitmask1();
		
		int br=rand.nextInt(bitmaskParent1.size());
		
		int br2=br+rand.nextInt((bitmaskParent1.size())-br);
		
		List<Integer>bitmaskChild=new ArrayList<>();
		for(int i=0;i<br;++i)
			bitmaskChild.add(bitmaskParent1.get(i));
		for(int i=br;i<br2;++i)
			bitmaskChild.add(bitmaskParent2.get(i));
		for(int i=br2;i<bitmaskParent1.size();++i)
			bitmaskChild.add(bitmaskParent1.get(i));
		if(br%2==0)
			return new Player(parent1.postfix,parent1.getHeight(),parent1.getWidth(),bitmaskChild,parent1.getbitmask2());
		return new Player(parent1.postfix,parent1.getHeight(),parent1.getWidth(),bitmaskChild,parent2.getbitmask2());
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
