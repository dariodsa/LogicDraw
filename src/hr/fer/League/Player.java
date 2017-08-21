package hr.fer.League;
import hr.fer.GeneticAlgorithm.*;

import java.util.*;

public class Player extends Draw implements Comparable<Player>{

	private int score=0;
	private int homeScore=0;
	private int awayScore=0;
	
	public Player(int height,int width)
	{
		super(height,width);
	}
	
	public Player(String postfix, int height, int width, List<Integer> bitmask,
			List<Integer> bitmask2) {
		super(postfix, height, width, bitmask, bitmask2);
		// TODO Auto-generated constructor stub
	}
	
	private void addScore(int val)
	{
		this.score += val;
	}
	
	public void addHomeScore(int val)
	{
		this.homeScore += val;
		addScore(val);
	}
	
	public void addAwayScore(int val)
	{
		this.awayScore += val;
		addScore(val);
	}
	
	public int getScore()
	{
		return this.score;
	}
	public int getHomeScore()
	{
		return this.homeScore;
	}
	public int getAwayScore()
	{
		return this.awayScore;
	}

	@Override
	public int compareTo(Player o) {
		
		//Score 
		int compare1=Integer.compare(score, o.getScore());
		if(compare1!=0)return compare1;
		
		//Away score
		int compare2=Integer.compare(awayScore, o.getAwayScore());
		if(compare2!=0)return compare2;
		
		return Integer.compare(homeScore,o.getHomeScore());
	}

	

}
