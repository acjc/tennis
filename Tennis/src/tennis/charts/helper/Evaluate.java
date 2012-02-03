package tennis.charts.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import tennis.charts.CrossMatchFiveSetLpmChart;
import tennis.charts.CrossMatchThreeSetLpmChart;
import tennis.charts.DefaultFiveSetLpmChart;
import tennis.charts.DefaultThreeSetLpmChart;
import au.com.bytecode.opencsv.CSVReader;

public class Evaluate
{
	public static void main(final String[] args) throws IOException
	{
		String matchType = "retirements\\three";
		final File [] retirementsThreeSets = new File("doc\\" + matchType).listFiles();
		for (final File match : retirementsThreeSets)
		{
			final File [] matchOdds = new File("doc\\" + matchType + File.separator + match.getName()).listFiles(new MatchOddsFilter());

			System.out.println(matchOdds[0].getName());
			System.out.println(matchOdds[1].getName());

			final String playerOneName = matchOdds[0].getName().split("\\.")[0];
			final String playerTwoName = matchOdds[1].getName().split("\\.")[0];

			final PlayerOdds playerOne = new PlayerOdds(playerOneName, matchType, match.getName());
			final PlayerOdds playerTwo = new PlayerOdds(playerTwoName, matchType, match.getName());

			createCharts(playerOne, playerTwo, true);
		}

		matchType = "retirements\\five";
		final File [] retirementsFiveSets = new File("doc\\" + matchType).listFiles();
		for (final File match : retirementsFiveSets)
		{
			final File [] matchOdds = new File("doc\\" + matchType + File.separator + match.getName()).listFiles(new MatchOddsFilter());

			final String playerOneName = matchOdds[0].getName().split("\\.")[0];
			final String playerTwoName = matchOdds[1].getName().split("\\.")[0];

			final PlayerOdds playerOne = new PlayerOdds(playerOneName, matchType, match.getName());
			final PlayerOdds playerTwo = new PlayerOdds(playerTwoName, matchType, match.getName());

			createCharts(playerOne, playerTwo, false);
		}

		matchType = "treatment\\three";
		final File [] treatmentThreeSets = new File("doc\\" + matchType).listFiles();
		for (final File match : treatmentThreeSets)
		{
			final File [] matchOdds = new File("doc\\" + matchType + File.separator + match.getName()).listFiles(new MatchOddsFilter());

			final String playerOneName = matchOdds[0].getName().split("\\.")[0];
			final String playerTwoName = matchOdds[1].getName().split("\\.")[0];

			final PlayerOdds playerOne = new PlayerOdds(playerOneName, matchType, match.getName());
			final PlayerOdds playerTwo = new PlayerOdds(playerTwoName, matchType, match.getName());

			createCharts(playerOne, playerTwo, true);
		}

		matchType = "treatment\\five";
		final File [] treatmentFiveSets = new File("doc\\" + matchType).listFiles();
		for (final File match : treatmentFiveSets)
		{
			final File [] matchOdds = new File("doc\\" + matchType + File.separator + match.getName()).listFiles(new MatchOddsFilter());

			final String playerOneName = matchOdds[0].getName().split("\\.")[0];
			final String playerTwoName = matchOdds[1].getName().split("\\.")[0];

			final PlayerOdds playerOne = new PlayerOdds(playerOneName, matchType, match.getName());
			final PlayerOdds playerTwo = new PlayerOdds(playerTwoName, matchType, match.getName());

			createCharts(playerOne, playerTwo, false);
		}
	}

	private static void createCharts(final PlayerOdds playerOne, final PlayerOdds playerTwo, final boolean threeSets) throws FileNotFoundException, IOException
	{
		final CSVReader oddsReader1 = new CSVReader(new FileReader(playerOne.getMatchOdds()));
		final CSVReader oddsReader2 = new CSVReader(new FileReader(playerTwo.getMatchOdds()));
		if (Double.parseDouble(oddsReader1.readNext()[6]) < Double.parseDouble(oddsReader2.readNext()[6]))
		{
			if (threeSets)
			{
				new DefaultThreeSetLpmChart(playerOne);
				new CrossMatchThreeSetLpmChart(playerTwo);
			}
			else
			{
				new DefaultFiveSetLpmChart(playerOne);
				new CrossMatchFiveSetLpmChart(playerTwo);
			}
		}
		else
		{
			if (threeSets)
			{
				new DefaultThreeSetLpmChart(playerTwo);
				new CrossMatchThreeSetLpmChart(playerOne);
			}
			else
			{
				new DefaultFiveSetLpmChart(playerTwo);
				new CrossMatchFiveSetLpmChart(playerOne);
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
