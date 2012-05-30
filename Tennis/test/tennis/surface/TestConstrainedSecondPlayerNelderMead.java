package tennis.surface;

import org.junit.Test;

import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalley;
import tennis.simulator.GameState;
import tennis.simulator.MatchState;
import tennis.simulator.SetState;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class TestConstrainedSecondPlayerNelderMead
{
	final double mwp = OMalley.matchInProgress(0.63, 0.61, new CurrentMatchScore(1, 0), new CurrentSetScore(1, 2), true, 3);
	private final double riskA = 0.2;
	private final double riskB = 0.2;
	MatchState initialState = new MatchState(1, 0, new SetState(1, 2), new GameState(true), 3);

	@Test
	public void testRetirementRiskFitTwoPlayer()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {40.0, 60.0};
		final double [] step = {10.0, 10.0};
		nm.addConstraint(0, -1, 0);
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


			final SimulatorWR simulator = new SimulatorWR(alphaA, alphaB, decay, true);
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
