package tennis.graphs.helper;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import tennis.graphs.odds.DefaultOddsChart;

public class GraphGenerator
{
	public static void main(final String[] args) throws IOException
	{
		System.out.println("Starting Graph Generator...");
		final MatchOddsFilter filter = new MatchOddsFilter();

		String matchType = "examples\\retirements";
		System.out.println(matchType);
		final File [] retirements = new File("doc\\" + matchType).listFiles();
		for (final File match : retirements)
		{
			drawGraphs(filter, matchType, match);
		}

		matchType = "examples\\treatment";
		System.out.println(matchType);
		final File [] treatment = new File("doc\\" + matchType).listFiles();
		for (final File match : treatment)
		{
			drawGraphs(filter, matchType, match);
		}

		System.out.println("Stopping Graph Generator");
	}

	private static void drawGraphs(final MatchOddsFilter filter, final String matchType, final File match) throws IOException
	{
		System.out.println(match.getName());
		final File [] matchOdds = new File("doc\\" + matchType + File.separator + match.getName()).listFiles(filter);

		final String playerOneName = matchOdds[0].getName().split("\\.")[0];
		final String playerTwoName = matchOdds[1].getName().split("\\.")[0];

		final PlayerOdds playerOne = new PlayerOdds(playerOneName, matchType, match.getName());
		final PlayerOdds playerTwo = new PlayerOdds(playerTwoName, matchType, match.getName());

		new DefaultOddsChart(playerOne, playerTwo).buildChart();
		new DefaultOddsChart(playerTwo, playerOne).buildChart();
//		new LpmOddsChart(playerOne, playerTwo).buildChart();
//		new LpmOddsChart(playerTwo, playerOne).buildChart();
//		new SmoothOddsChart(playerOne, playerTwo).buildChart();
//		new SmoothOddsChart(playerTwo, playerOne).buildChart();
	}

	private static class MatchOddsFilter implements FilenameFilter
	{
		@Override
		public boolean accept(final File file, final String name)
		{
			return !name.contains(" - ");
		}
	}
}
