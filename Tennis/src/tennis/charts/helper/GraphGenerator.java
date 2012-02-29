package tennis.charts.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

import tennis.charts.lpm.CrossMatchLpmChart;
import tennis.charts.lpm.DefaultLpmChart;
import au.com.bytecode.opencsv.CSVReader;

public class GraphGenerator
{
	public static void main(final String[] args) throws IOException
	{
		System.out.println("Starting Graph Generator...");
		final MatchOddsFilter filter = new MatchOddsFilter();

		String matchType = "retirements\\three";
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

			createCharts(playerOne, playerTwo, true);
		}

		matchType = "retirements\\five";
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

			createCharts(playerOne, playerTwo, false);
		}

		matchType = "treatment\\three";
		System.out.println(matchType);
		final File [] treatmentThreeSets = new File("doc\\" + matchType).listFiles();
		for (final File match : treatmentThreeSets)
		{
			System.out.println(match.getName());
			final File [] matchOdds = new File("doc\\" + matchType + File.separator + match.getName()).listFiles(filter);

			final String playerOneName = matchOdds[0].getName().split("\\.")[0];
			final String playerTwoName = matchOdds[1].getName().split("\\.")[0];

			final PlayerOdds playerOne = new PlayerOdds(playerOneName, matchType, match.getName());
			final PlayerOdds playerTwo = new PlayerOdds(playerTwoName, matchType, match.getName());

			createCharts(playerOne, playerTwo, true);
		}

		matchType = "treatment\\five";
		System.out.println(matchType);
		final File [] treatmentFiveSets = new File("doc\\" + matchType).listFiles();
		for (final File match : treatmentFiveSets)
		{
			System.out.println(match.getName());
			final File [] matchOdds = new File("doc\\" + matchType + File.separator + match.getName()).listFiles(filter);

			final String playerOneName = matchOdds[0].getName().split("\\.")[0];
			final String playerTwoName = matchOdds[1].getName().split("\\.")[0];

			final PlayerOdds playerOne = new PlayerOdds(playerOneName, matchType, match.getName());
			final PlayerOdds playerTwo = new PlayerOdds(playerTwoName, matchType, match.getName());

			createCharts(playerOne, playerTwo, false);
		}

		System.out.println("Stopping Graph Generator");
	}

	private static void createCharts(final PlayerOdds playerOne, final PlayerOdds playerTwo, final boolean threeSets) throws FileNotFoundException, IOException
	{
		final CSVReader oddsReader1 = playerOne.getMatchOdds();
		final CSVReader oddsReader2 = playerTwo.getMatchOdds();
		if (Double.parseDouble(oddsReader1.readNext()[6]) < Double.parseDouble(oddsReader2.readNext()[6]))
		{
			if (threeSets)
			{
				new DefaultLpmChart(playerOne, playerTwo);
				new CrossMatchLpmChart(playerOne, playerTwo);
			}
			else
			{
				new DefaultLpmChart(playerOne, playerTwo);
				new CrossMatchLpmChart(playerOne, playerTwo);
			}
		}
		else
		{
			if (threeSets)
			{
				new DefaultLpmChart(playerTwo, playerOne);
				new CrossMatchLpmChart(playerTwo, playerOne);
			}
			else
			{
				new DefaultLpmChart(playerTwo, playerOne);
				new CrossMatchLpmChart(playerTwo, playerOne);
			}
		}
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
