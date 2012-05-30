package tennis.surface;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.direct.NelderMead;
import org.junit.Test;

public class TestNelderMead
{
	@Test
	public void testSampleNelderMead() throws OptimizationException, FunctionEvaluationException, IllegalArgumentException
	{
		final NelderMead nm = new NelderMead();
		final double [] simplex = {0.0, 0.0, 1.2, 0.0, 0.0, 0.8};
		final RealPointValuePair minimum = nm.optimize(new SampleFunction(), GoalType.MINIMIZE, simplex);
		System.out.println(minimum.getValue());
	}

	private class RetirementRiskFunction implements MultivariateRealFunction
	{
		@Override
		public double value(final double[] point) throws FunctionEvaluationException, IllegalArgumentException
		{
			// point = {alpha, decay}
			return 0;
		}
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
