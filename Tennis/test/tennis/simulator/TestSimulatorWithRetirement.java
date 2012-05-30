package tennis.simulator;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.direct.NelderMead;
import org.junit.Ignore;
import org.junit.Test;

public class TestSimulatorWithRetirement
{
	@Ignore
	@Test
	public void testFindAlpha() throws IOException
	{
		final double baseAlpha = 154.13;
		for(double i = 0; i <= 0.01; i += 0.001)
		{
			System.out.println(baseAlpha + i);
			final SimulatorWithRetirement simulator = new SimulatorWithRetirement(baseAlpha + i, 0.01, 0.85);
			simulator.simulate(0.6, 0.6, 10000).print("A", "B", 3);
			System.out.println();
		}
	}

	@Test
	public void testSimulatorWithRetirement() throws IOException
	{
		final double baseAlpha = 154.133;
		final SimulatorWithRetirement simulator = new SimulatorWithRetirement(baseAlpha, 0.01, 0.85);
		simulator.simulate(0.6, 0.6, 250000).print("A", "B", 3);
	}

//	@Ignore
	@Test
	public void testNelderMead() throws OptimizationException, FunctionEvaluationException, IllegalArgumentException
	{
		final NelderMead nm = new NelderMead();
		final double [] simplex = {0.0, 0.0, 1.2, 0.0, 0.0, 0.8};
		final RealPointValuePair minimum = nm.optimize(new SampleFunction(), GoalType.MINIMIZE, simplex);
		System.out.println(minimum.getValue());
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}

	private class SampleFunction implements MultivariateRealFunction
	{
		@Override
		public double value(final double[] point) throws FunctionEvaluationException, IllegalArgumentException
		{
			final double x = point[0];
			final double y = point[1];
			return Math.pow(x, 2) - (4 * x) + Math.pow(y, 2) - y - (x * y);
		}
	}
}
