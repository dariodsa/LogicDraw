package hr.fer.League;
import hr.fer.GeneticAlgorithm.*;

import java.util.*;

public class LeagueTable {
	
	List<Game>scheduledGames =new ArrayList<>();
	
	List<Player>players = new ArrayList<>();
	
	public LeagueTable(List<Draw>lista)
	{
		for(Draw D: lista)
		{
			players.add((Player)D);
		}
		
		scheduledGames=LeagueTable.construct(players);
	}
	
	public void playTwoRounds()
	{
		for(Game g: scheduledGames)
			g.playGame();
		
		
		players.sort(new Comparator<Player>() {

			@Override
			public int compare(Player o1, Player o2) {
				return o1.compareTo(o2);
			}
		});
	}
	public List<Player> getLeagueOrder()
	{
		return this.players;
	}
	private static List<Game> construct(List<Player>players)
	{
		List<Game>games=new ArrayList<>();
		
		for(int i=0;i<players.size();++i)
		{
			for(int j=0;j<players.size();++j)
			{
				if(i==j)continue;
				games.add(new Game(players.get(i),players.get(j)));
			}
		}
		
		return games;
	}
}
