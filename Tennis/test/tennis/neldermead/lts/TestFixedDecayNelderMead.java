package tennis.neldermead.lts;

import org.junit.Test;

import tennis.neldermead.simulator.TestNelderMead;
import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalleyWRWin;
import tennis.omalley.RetirementRisk;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class TestFixedDecayNelderMead extends TestNelderMead
{
	private final double risk = 0.2;

	@Test
	public void testRetirementRiskFitFixedDecay()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {1.0, 50.0};
		final double [] step = {10.0, 10.0};
		nm.addConstraint(0, -1, 0);
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

			final OMalleyWRWin oMalley = new OMalleyWRWin(alpha, 1000.0, 0.85, true);
			final double targetMwpWR = oMalley.matchInProgress(pa, pb, new RetirementRisk(), new CurrentMatchScore(1, 0), new CurrentSetScore(1, 2), true, 3);

			System.out.println("mwp = " + mwp + ", mwpWR = " + targetMwpWR + ", MWP Difference = " + (mwp - targetMwpWR));
			return Math.abs(mwp - (targetMwpWR + risk));
		}
	}
}
