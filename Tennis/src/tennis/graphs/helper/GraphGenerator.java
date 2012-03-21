package tennis.graphs.helper;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import tennis.graphs.lpm.DefaultOddsChart;
import tennis.graphs.lpm.LpmOddsChart;

public class GraphGenerator
{
	public static void main(final String[] args) throws IOException
	{
		System.out.println("Starting Graph Generator...");
		final MatchOddsFilter filter = new MatchOddsFilter();

		String matchType = "examples\\retirements";
		System.out.println(matchType);
		final File [] retirementsThreeSets = new File("doc\\" + matchType).listFiles();
		for (final File match : retirementsThreeSets)
		{
			System.out.println(match.getName());
			final File [] matchOdds = new File("doc\\" + matchType + File.separator + match.getName()).listFiles(filter);

			final String playerOneName = matchOdds[0].getName().split("\\.")[0];
			final String playerTwoName = matchOdds[1].getName().split("\\.")[0];

			final PlayerOdds playerOne = new PlayerOdds(playerOneName, matchType, match.getName());
			final PlayerOdds playerTwo = new PlayerOdds(playerTwoName, matchType, match.getName());

			new DefaultOddsChart(playerOne, playerTwo);
			new DefaultOddsChart(playerTwo, playerOne);
			new LpmOddsChart(playerOne, playerTwo);
			new LpmOddsChart(playerTwo, playerOne);
		}

		matchType = "examples\\treatment";
		System.out.println(matchType);
		final File [] retirementsFiveSets = new File("doc\\" + matchType).listFiles();
		for (final File match : retirementsFiveSets)
		{
			System.out.println(match.getName());
			final File [] matchOdds = new File("doc\\" + matchType + File.separator + match.getName()).listFiles(filter);

			final String playerOneName = matchOdds[0].getName().split("\\.")[0];
			final String playerTwoName = matchOdds[1].getName().split("\\.")[0];

			final PlayerOdds playerOne = new PlayerOdds(playerOneName, matchType, match.getName());
			final PlayerOdds playerTwo = new PlayerOdds(playerTwoName, matchType, match.getName());

			new DefaultOddsChart(playerOne, playerTwo);
			new DefaultOddsChart(playerTwo, playerOne);
			new LpmOddsChart(playerOne, playerTwo);
			new LpmOddsChart(playerTwo, playerOne);
		}

		System.out.println("Stopping Graph Generator");
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
