package hr.fer.GeneticAlgorithm;
import java.util.*;

/*
 * The idea is to not have a transit function of goodness, so we need to simulate a tournament, to get a winner.
 * accelerated Swiss
*/
public class LeagueTable 
{
	private List<Draw>draws=new ArrayList<>();
	/*
	 * Constructs the new object with the given draws.
	 * @param List<Draw>
	 */
	public LeagueTable(List<Draw> draws)
	{
		this.draws=new ArrayList<>(draws);
	}
	/*
	 * @return List<Draw>
	 */
	public List<Draw> getLeagueOrder()
	{
		return null;
	}
	
}
