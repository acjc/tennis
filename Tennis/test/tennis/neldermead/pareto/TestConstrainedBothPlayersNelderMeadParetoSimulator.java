package tennis.neldermead.pareto;

import org.junit.Test;

import tennis.neldermead.TestNelderMead;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRPareto;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class TestConstrainedBothPlayersNelderMeadParetoSimulator extends TestNelderMead
{
	private final double riskA = 0.2;
	private final double riskB = 0.2;

	@Test
	public void testRetirementRiskFitTwoPlayer()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {40.0, 40.0, 50.0, 50.0, 60.0, 60.0};
		final double [] step = {5.0, 5.0,5.0, 5.0, 5.0, 5.0};
		final RetirementRiskBothPlayersFunction f = new RetirementRiskBothPlayersFunction();
		nm.nelderMead(f, simplex, step, 0.005);

		System.out.println("\nFinal Answer");
		f.function(nm.getParamValues());
		System.out.println("Minimum = " + nm.getMinimum());
	}

	private class RetirementRiskBothPlayersFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double alphaA = param[0];
			final double alphaB = param[1];
			final double decay = 0.85;
			System.out.println("AlphaA = " + alphaA + ", alphaB = " + alphaB + ", Decay = " + decay);


			final SimulatorWR simulator = new SimulatorWRPareto(alphaA, alphaB, decay, true);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, initialState, true, 20000);
			final double targetMwpWR = outcomes.proportionTargetWon();
			final double opponentMwpWR = outcomes.proportionOpponentWon();
			final double rateA = outcomes.proportionTargetRetirements();
			final double rateB = outcomes.proportionOpponentRetirements();

			outcomes.minPrint("A", "B");
			return Math.abs(rateA - riskA) + Math.abs(rateB - riskB) + Math.abs(mwp - (targetMwpWR + riskA)) + Math.abs((1 - mwp) - (opponentMwpWR + riskB));
		}
	}
}
