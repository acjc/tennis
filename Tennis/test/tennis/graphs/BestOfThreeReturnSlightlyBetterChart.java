package tennis.graphs;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.OMalley;

public class BestOfThreeReturnSlightlyBetterChart extends XYLineChart
{
	public BestOfThreeReturnSlightlyBetterChart() throws IOException
	{
	    super("3 set match with pa always 0.02 better than pb", "pa", "bestOfThree(pa, pa - 0.02");
	}

	@Override
	public void buildChart() throws IOException
	{
		final JFreeChart chart = createChart();
		final ChartPanel chartPanel = new ChartPanel(chart);
		((XYPlot) chart.getPlot()).getRangeAxis().setRange(0.5, 1);
	    chartPanel.setPreferredSize(new Dimension(1000, 300));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + title + ".png"), chart, 1000, 300);
	}


	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("BestOfThree");
	    for(double i = 0.02; i < 1.01; i += 0.01)
	    {
			series.add(i, OMalley.bestOfThree(i, i - 0.02));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final BestOfThreeReturnSlightlyBetterChart chart = new BestOfThreeReturnSlightlyBetterChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
