package hr.fer.League;
import java.sql.Date;
import java.util.*;

import hr.fer.GeneticAlgorithm.*;

public class Game {
	
	private Player homePlayer;
	private Player awayPlayer;
	
	private Integer homePlayerScore=0;
	private Integer awayPlayerScore=0;
	
	private final static int WireCrossScore = 10; 
	private final static int MinimumNodeDistanceScore = 2; 
	private final static int EdgeLengthDeviationScore = 2;
	private final static int YLengthScore = 1;
	
	public Game(Player homePlayer, Player awayPlayer)
	{
		this.homePlayer=homePlayer;
		this.awayPlayer=awayPlayer;
	}
	public void playGame()
	{
		homePlayerScore=0;
		awayPlayerScore=0;
		
		fight((double)homePlayer.getNumWiresCrossing(),
			  (double)awayPlayer.getNumWiresCrossing(),
			  (x,y)->(Double.compare(x, y)),
			  WireCrossScore
			 );
		
		fight((double)homePlayer.getMinimumNodeDistance(),
			  (double)awayPlayer.getMinimumNodeDistance(),
			  (x,y)->(Double.compare(y, x)),
			  MinimumNodeDistanceScore
				 );
		fight((double)homePlayer.getEdgeLengthDeviation(),
			  (double)awayPlayer.getEdgeLengthDeviation(),
			  (x,y)->(Double.compare(x, y)),
			  EdgeLengthDeviationScore
			  );
		fight((double)homePlayer.getYLength(),
			  (double)awayPlayer.getYLength(),
			  (x,y)->(Double.compare(x, y)),
			  YLengthScore
			  );
		
		homePlayer.addHomeScore(homePlayerScore);
		awayPlayer.addAwayScore(awayPlayerScore);
		
		
	}
	private void fight(Double value1,Double value2,Comparator<Double> C, int score)
	{
		if(C.compare(value1, value2)==-1)
		{
			homePlayerScore += score;
			//awayPlayerScore -= score;
		}
		else if(C.compare(value1, value2)==1)
		{
			awayPlayerScore += score;
		}
		else
		{
			Random rand=new Random(System.currentTimeMillis());
			double x = rand.nextGaussian();
			//60% for victory -> home
			//40% for victory -> away
			// 1/sqrt(2*pi) * e ^ ( -0.5*x^2)
			if( x < 0.25137)homePlayerScore += score;
			awayPlayerScore += score;
		}
	}
	
	
}
