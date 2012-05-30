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

public class TestConstrainedFirstPlayerNelderMead
{
	private final double mwp = OMalley.matchInProgress(0.63, 0.61, new CurrentMatchScore(1, 0), new CurrentSetScore(1, 2), true, 3);
	private final double risk = 0.2;
	MatchState initialState = new MatchState(1, 0, new SetState(1, 2), new GameState(true), 3);

	@Test
	public void testRetirementRiskFitOnePlayer()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {40.0, 0.70, 50.0, 0.85, 60.0, 0.95};
		final double [] step = {10.0, 1.0, 10.0, 1.0, 10.0, 1.0};
		nm.addConstraint(1, -1, 0.70);
		nm.addConstraint(1, 1, 0.95);
		final RetirementRiskFirstPlayerFunction f = new RetirementRiskFirstPlayerFunction();
		nm.nelderMead(f, simplex, step, 0.005);

		System.out.println("\nFinal Answer");
		f.function(nm.getParamValues());
	}

	private class RetirementRiskFirstPlayerFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double alpha = param[0];
			final double decay = param[1];
			System.out.println("Alpha = " + alpha + ", Decay = " + decay);

			final SimulatorWR simulator = new SimulatorWR(alpha, 1000.0, decay, true);
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
