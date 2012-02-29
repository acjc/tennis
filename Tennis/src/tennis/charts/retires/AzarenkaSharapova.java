package tennis.charts.retires;

import java.io.FileReader;
import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.lpm.CrossMatchThreeSetLpmChart;
import tennis.graphs.lpm.DefaultLpmChart;
import tennis.graphs.lpm.LpmChart;
import au.com.bytecode.opencsv.CSVReader;

public class AzarenkaSharapova
{
	public static void main(final String[] args) throws IOException
	{
		final String title = "Azarenka vs Sharapova - Rome Masters 2011 Quarter Final";
		final PlayerOdds playerOne = new PlayerOdds("Victoria", "Azarenka", "azarenka_sharapova", title);
		final PlayerOdds playerTwo = new PlayerOdds("Maria", "Sharapova", "azarenka_sharapova", title);

		final LpmChart favouriteChart;
		final LpmChart underdogChart;

		final CSVReader oddsReader1 = new CSVReader(new FileReader(playerOne.getMatchOdds()));
		final CSVReader oddsReader2 = new CSVReader(new FileReader(playerTwo.getMatchOdds()));
		if (Double.parseDouble(oddsReader1.readNext()[6]) < Double.parseDouble(oddsReader2.readNext()[6]))
		{
			favouriteChart = new DefaultLpmChart(playerOne);
			underdogChart = new CrossMatchThreeSetLpmChart(playerTwo);
		}
		else
		{
			favouriteChart = new DefaultLpmChart(playerTwo);
			underdogChart = new CrossMatchThreeSetLpmChart(playerOne);
		}

		favouriteChart.pack();
		RefineryUtilities.centerFrameOnScreen(favouriteChart);
		favouriteChart.setVisible(true);

//		underdogChart.pack();
//		RefineryUtilities.centerFrameOnScreen(underdogChart);
//		underdogChart.setVisible(true);
	}
}
