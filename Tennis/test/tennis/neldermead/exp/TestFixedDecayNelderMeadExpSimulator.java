package tennis.neldermead.exp;

import org.junit.Test;

import tennis.neldermead.TestNelderMead;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRExp;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class TestFixedDecayNelderMeadExpSimulator extends TestNelderMead
{
	private final double risk = 0.2;

	@Test
	public void testRetirementRiskFitFixedDecay()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {5000.0, 15000.0};
		final double [] step = {1000.0, 1000.0};
		nm.addConstraint(0, -1, 0);
		final RetirementRiskFixedDecayFunction f = new RetirementRiskFixedDecayFunction();
		nm.nelderMead(f, simplex, step, 0.005);

		System.out.println("\nFinal Answer");
		f.function(nm.getParamValues());
		System.out.println("Minimum = " + nm.getMinimum());
		System.out.println();
	}

	private class RetirementRiskFixedDecayFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double lambda = param[0];
			final double decay = 0.85;
			System.out.println("Lambda = " + lambda + ", Decay = " + decay);

			final SimulatorWR simulator = new SimulatorWRExp(lambda, -1, decay, true);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, initialState, true, 50000);
			final double targetMwpWR = outcomes.proportionTargetWon();
			final double opponentMwpWR = outcomes.proportionOpponentWon();
			final double rate = outcomes.proportionTargetRetirements();

			System.out.println("MWP = " + mwp + ", Risk = " + risk);
			outcomes.minPrint("A", "B");
			final double targetNoRetMwp = targetMwpWR / (targetMwpWR + opponentMwpWR);
			System.out.println("Target No Ret. MWP = " + targetNoRetMwp);
			final double opponentNoRetMwp = opponentMwpWR / (targetMwpWR + opponentMwpWR);
			System.out.println("Opponent No Ret. MWP = " + opponentNoRetMwp);
			return Math.abs(rate - risk) + Math.abs(mwp - targetNoRetMwp) + Math.abs((1 - mwp) - opponentNoRetMwp);
		}
	}
}
