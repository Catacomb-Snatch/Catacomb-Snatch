package net.catacombsnatch.game.core.statistic;

import java.util.ArrayList;
import java.util.List;

public class Statistics {
	protected static List<Statistic> statistics;
	static {
		statistics = new ArrayList<Statistic>();
	}
	
	public Statistic getStatistic(Statistic statistic) {
		for(Statistic s : statistics) {
			if(s.equals(statistic)) return s;
		}
		
		return null;
	}
	
}
