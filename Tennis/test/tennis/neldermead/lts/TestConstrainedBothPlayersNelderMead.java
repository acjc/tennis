package tennis.neldermead.lts;

import org.junit.Test;

import tennis.neldermead.simulator.pareto.TestNelderMead;
import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalleyWRWin;
import tennis.simulator.RetirementRisk;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class TestConstrainedBothPlayersNelderMead extends TestNelderMead
{
	private final double riskA = 0.2;
	private final double riskB = 0.2;

	@Test
	public void testRetirementRiskFitTwoPlayer()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {25.0, 25.0, 40.0, 40.0, 60.0, 60.0};
		final double [] step = {10.0, 10.0, 10.0, 10.0, 10.0, 10.0};
		nm.addConstraint(0, -1, 0);
		nm.addConstraint(1, -1, 0);
		final RetirementRiskBothPlayersFunction f = new RetirementRiskBothPlayersFunction();
		nm.nelderMead(f, simplex, step, 0.005);

		System.out.println("\nFinal Answer");
		f.function(nm.getParamValues());
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

			final OMalleyWRWin oMalleyA = new OMalleyWRWin(alphaA, alphaB, 0.85, true);
			final double targetMwpWR = oMalleyA.matchInProgress(pa, pb, new RetirementRisk(), new CurrentMatchScore(1, 0), new CurrentSetScore(1, 2), true, 3);

			final OMalleyWRWin oMalleyB = new OMalleyWRWin(alphaB, alphaA, 0.85, true);
			final double opponentMwpWR = oMalleyB.matchInProgress(pb, pa, new RetirementRisk(), new CurrentMatchScore(1, 0), new CurrentSetScore(1, 2), true, 3);

			System.out.println("mwp = " + mwp + ", targetMwpWR = " + targetMwpWR + ", opponentMwpWR = " + opponentMwpWR);
			return Math.abs(mwp - (targetMwpWR + riskA)) + Math.abs((1 - mwp) - (opponentMwpWR + riskB));
		}
	}
}
