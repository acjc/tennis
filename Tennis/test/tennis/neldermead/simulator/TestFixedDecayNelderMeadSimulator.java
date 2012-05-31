package tennis.neldermead.simulator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.junit.Test;

import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRPareto;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class TestFixedDecayNelderMeadSimulator extends TestNelderMead
{
	private final double risk = 0.2;

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

	@Test
	public void testRetirementRiskFitFixedDecay()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {40.0, 60.0};
		final double [] step = {10.0, 10.0};
		final RetirementRiskFixedDecayFunction f = new RetirementRiskFixedDecayFunction();
		nm.nelderMead(f, simplex, step, 0.001);

		System.out.println("\nFinal Answer");
		f.function(nm.getParamValues());
		System.out.println("Minimum = " + nm.getMinimum());
	}

	private class RetirementRiskFixedDecayFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double alpha = param[0];
			final double decay = 0.85;
			System.out.println("Alpha = " + alpha + ", Decay = " + decay);

			final SimulatorWR simulator = new SimulatorWRPareto(alpha, 1000.0, decay, true);
			final SimulationOutcomes outcomes = simulator.simulate(0.63, 0.61, initialState, true, 20000);
			final double targetMwpWR = outcomes.proportionTargetWon();
			final double opponentMwpWR = outcomes.proportionOpponentWon();
			final double rate = outcomes.proportionTargetRetirements();

			System.out.println("MWP = " + mwp);
			outcomes.minPrint("A", "B");
			System.out.println("MWP Difference = " + (mwp - targetMwpWR));
			return Math.abs(rate - risk) + Math.abs(mwp - (targetMwpWR + risk)) + Math.abs((1 - mwp) - opponentMwpWR);
		}
	}
}
