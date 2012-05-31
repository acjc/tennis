package tennis.neldermead;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.junit.Test;

import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class TestNelderMeadSample
{
	@Test
	public void testSampleNelderMead() throws OptimizationException, FunctionEvaluationException, IllegalArgumentException
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {0.0, 0.0, 1.2, 0.0, 0.0, 0.8};
		nm.nelderMead(new SampleFunction(), simplex);
		assertThat(nm.getMinimum(), equalTo(-6.999999999999934));
	}

	private class SampleFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double x = param[0];
			final double y = param[1];
			return Math.pow(x, 2) - (4 * x) + Math.pow(y, 2) - y - (x * y);
		}
	}
}
