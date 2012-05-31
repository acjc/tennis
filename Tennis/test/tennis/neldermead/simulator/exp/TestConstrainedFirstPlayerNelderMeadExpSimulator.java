package tennis.neldermead.simulator.exp;

import org.junit.Test;

import tennis.neldermead.simulator.pareto.TestNelderMead;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRExp;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class TestConstrainedFirstPlayerNelderMeadExpSimulator extends TestNelderMead
{
	private final double risk = 0.2;

	@Test
	public void testRetirementRiskFitOnePlayer()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {1000.0, 0.75, 5000.0, 0.85, 10000.0, 1.0};
		final double [] step = {1000.0, 1.0, 1000.0, 1.0, 1000.0, 1.0};
		nm.addConstraint(0, -1, 0);
		nm.addConstraint(1, -1, 0.75);
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
			final double lambda = param[0];
			final double decay = param[1];
			System.out.println("Lambda = " + lambda + ", Decay = " + decay);

			final SimulatorWR simulator = new SimulatorWRExp(lambda, -1, decay, true);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, initialState, true, 50000);
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
