package tennis.read;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.MatchOdds;
import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.helper.SetOdds;
import tennis.graphs.odds.OddsChart;
import au.com.bytecode.opencsv.CSVReader;

public class ChartSimple extends OddsChart
{
	private final int points;

	public ChartSimple(final String title, final int points, final PlayerOdds favourite, final PlayerOdds underdog) throws IOException
	{
		super(title, "Time", "Implied Probability", favourite, underdog);
		this.points = points;
	}

	@Override
	protected XYDataset createDataset() throws IOException
	{
		System.out.println("Processing odds data...");
		final CSVReader favouriteMatchOddsReader = favourite.getMatchOdds();
		final List<CSVReader> favouriteSetOddsReaders = new ArrayList<CSVReader>();
		final List<CSVReader> underdogSetOddsReaders = new ArrayList<CSVReader>();
		favouriteSetOddsReaders.addAll(favourite.getSetOdds());
		underdogSetOddsReaders.addAll(underdog.getSetOdds());

		final List<MatchOdds> favouriteMatchOdds = parseMatchOdds(favouriteMatchOddsReader);
		final List<List<SetOdds>> favouriteSetOdds = parseSetOdds(favouriteSetOddsReaders);
		final List<List<SetOdds>> underdogSetOdds = parseSetOdds(underdogSetOddsReaders);

		final List<Double> retirementRisks = new ArrayList<Double>();
		final List<Double> setOddsProbabilities = new ArrayList<Double>();
		for (int i = 0; i < favouriteMatchOdds.size(); i++)
		{
			final long time = favouriteMatchOdds.get(i).getTime();
			final double matchOddsProbability = favouriteMatchOdds.get(i).getOddsProbability();
			final double setOddsProbability = calculateCorrectedSetOddsProbability(favouriteSetOdds, underdogSetOdds, time);

			setOddsProbabilities.add(setOddsProbability);
			retirementRisks.add(setOddsProbability - matchOddsProbability);
		}

		final List<Double> simpleRetirementRisks = new ArrayList<Double>();
		final List<Double> simpleSetOddsProbabilities = new ArrayList<Double>();
		final int jump = favouriteMatchOdds.size() / points;
		for (int i = 0; i < points; i++)
		{
			simpleSetOddsProbabilities.add(setOddsProbabilities.get(i * jump));
			simpleRetirementRisks.add(retirementRisks.get(i * jump) >= 0 ? retirementRisks.get(i * jump) : 0);
		}

		System.out.println("Processing match...");
		final XYSeries simpleRetirementRiskSeries = new XYSeries("Simple Retirement Risk");
		final XYSeries simpleSetOddsProbabilitySeries = new XYSeries("Simple Set Odds MWP");
		int index = 0;
		for (int i = 0; i < points; i++)
		{
			simpleRetirementRiskSeries.add(index, simpleRetirementRisks.get(index));
			simpleSetOddsProbabilitySeries.add(index, simpleSetOddsProbabilities.get(index));

		    index++;
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(simpleRetirementRiskSeries);
	    dataset.addSeries(simpleSetOddsProbabilitySeries);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
		final ChartSimple chart = new ChartSimple("Murray vs Nieminen - French Open 2012 Second Round (Simple Odds)", 213,
											  	  new PlayerOdds("Andy Murray", "examples\\treatment", "Murray vs Nieminen - French Open 2012 Second Round"),
											  	  new PlayerOdds("Jarkko Nieminen", "examples\\treatment", "Murray vs Nieminen - French Open 2012 Second Round"));
		chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
