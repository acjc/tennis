package tennis.neldermead.pareto;

import org.junit.Test;

import tennis.neldermead.TestNelderMead;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRPareto;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class TestConstrainedFirstPlayerNelderMeadParetoSimulator extends TestNelderMead
{
	private final double risk = 0.2;

	@Test
	public void testRetirementRiskFitOnePlayer()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {20.0, 0.15, 50.0, 0.60, 80.0, 0.95};
		final double [] step = {10.0, 1.0, 10.0, 1.0, 10.0, 1.0};
		nm.addConstraint(0, -1, 0);
		nm.addConstraint(1, -1, 0.10);
		nm.addConstraint(1, 1, 1.0);
		final RetirementRiskFirstPlayerFunction f = new RetirementRiskFirstPlayerFunction();
		nm.nelderMead(f, simplex, step, 0.005);

		System.out.println("\nFinal Answer");
		f.function(nm.getParamValues());
		System.out.println("Minimum = " + nm.getMinimum());
	}

	private class RetirementRiskFirstPlayerFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double alpha = param[0];
			final double decay = param[1];
			System.out.println("Alpha = " + alpha + ", Decay = " + decay);

			final SimulatorWR simulator = new SimulatorWRPareto(alpha, 1000.0, decay, true);
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
