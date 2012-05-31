package tennis.neldermead.simulator;

import org.junit.Test;

import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRPareto;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class TestConstrainedSecondPlayerNelderMeadSimulator extends TestNelderMead
{
	private final double riskA = 0.2;
	private final double riskB = 0.2;

	@Test
	public void testRetirementRiskFitTwoPlayer()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {40.0, 60.0};
		final double [] step = {10.0, 10.0};
		final RetirementRiskSecondPlayerFunction f = new RetirementRiskSecondPlayerFunction();
		nm.nelderMead(f, simplex, step, 0.005);

		System.out.println("\nFinal Answer");
		f.function(nm.getParamValues());
	}

	private class RetirementRiskSecondPlayerFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double alphaA = 50.0;
			final double alphaB = param[0];
			final double decay = 0.85;
			System.out.println("AlphaA = " + alphaA + ", alphaB = " + alphaB + ", Decay = " + decay);


			final SimulatorWR simulator = new SimulatorWRPareto(alphaA, alphaB, decay, true);
			final SimulationOutcomes outcomes = simulator.simulate(0.63, 0.61, initialState, true, 20000);
			final double targetMwpWR = outcomes.proportionTargetWon();
			final double opponentMwpWR = outcomes.proportionOpponentWon();
			final double rateA = outcomes.proportionTargetRetirements();
			final double rateB = outcomes.proportionOpponentRetirements();

			outcomes.minPrint("A", "B");
			return Math.abs(rateA - riskA) + Math.abs(rateB - riskB) + Math.abs(mwp - (targetMwpWR + riskA)) + Math.abs((1 - mwp) - (opponentMwpWR + riskB));
		}
	}
}
