package tennis.neldermead.pareto;

import org.junit.Test;

import tennis.neldermead.TestNelderMead;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRPareto;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class TestFixedDecayNelderMeadParetoSimulator extends TestNelderMead
{
	private final double risk = 0.2;

	@Test
	public void testRetirementRiskFitFixedDecay()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {5000.0, 15000.0};
		final double [] step = {1000.0, 1000.0};
		final RetirementRiskFixedDecayFunction f = new RetirementRiskFixedDecayFunction();
		nm.nelderMead(f, simplex, step, 0.005);

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

			final SimulatorWR simulator = new SimulatorWRPareto(alpha, -1, decay, true);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, initialState, true, 20000);
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
