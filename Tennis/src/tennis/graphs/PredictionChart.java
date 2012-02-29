package tennis.graphs;

import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.simulator.SimulationOutcomes;
import tennis.simulator.Simulator;

public class PredictionChart extends XYLineChart
{
	private final List<Double> predictions;
	private final List<Double> predictionsWithInjury;

	public PredictionChart(final String title, final List<Double> predictions, final List<Double> predictionsWithInjury) throws IOException
	{
	    super(title, "Points", "Match-winning Probability");
		this.predictions = predictions;
		this.predictionsWithInjury = predictionsWithInjury;
	}

	@Override
	protected void buildChart() throws IOException
	{
		final JFreeChart chart = createXYLineChart(createDataset());
		((XYPlot) chart.getPlot()).getRangeAxis().setRange(0, 1);
		final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(800, 400));
	    setContentPane(chartPanel);
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries unbiasedMwp = new XYSeries("Unbiased MWP");
		final XYSeries injuryBiasedMwp = new XYSeries("Injury-biased MWP");
		final XYSeries mwpDifference = new XYSeries("MWP Difference");
	    for(int i = 0; i < predictions.size(); i++)
	    {
			unbiasedMwp.add(i, predictions.get(i));
			injuryBiasedMwp.add(i, predictionsWithInjury.get(i));
			mwpDifference.add(i, predictions.get(i) - predictionsWithInjury.get(i));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(unbiasedMwp);
	    dataset.addSeries(injuryBiasedMwp);
	    dataset.addSeries(mwpDifference);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
		final SimulationOutcomes outcomes = new Simulator().simulate(0.62, 0.40, 1);
		final PredictionChart oneBallChart = outcomes.targetOneBallPredictionChart();
		oneBallChart.buildChart();
		oneBallChart.pack();
	    RefineryUtilities.centerFrameOnScreen(oneBallChart);
	    oneBallChart.setVisible(true);
	    outcomes.print("Target", "Opponent", 3);

	    final PredictionChart oneSetChart = outcomes.targetOneSetPredictionChart();
		oneSetChart.buildChart();
		oneSetChart.pack();
	    RefineryUtilities.centerFrameOnScreen(oneSetChart);
	    oneSetChart.setVisible(true);

	    final PredictionChart twoSetsChart = outcomes.targetTwoSetsPredictionChart();
		twoSetsChart.buildChart();
		twoSetsChart.pack();
	    RefineryUtilities.centerFrameOnScreen(twoSetsChart);
	    twoSetsChart.setVisible(true);
	}
}
