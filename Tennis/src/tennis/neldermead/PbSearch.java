package tennis.neldermead;

import tennis.omalley.OMalley;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class PbSearch
{
	private final double pa = 0.645;
	private final double oddsMwp;

	public PbSearch(final double oddsMwp)
	{
		this.oddsMwp = oddsMwp;
	}

	public double search()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {0.5, 0.8};
		final double [] step = {0.01, 0.01};
		nm.addConstraint(0, -1, 0.4);
		nm.addConstraint(0, 1, 0.8);
		final PointWinningProbabilityFunction f = new PointWinningProbabilityFunction();
		nm.nelderMead(f, simplex, step, 0.005);

		System.out.println("Final Answer");
		final double[] params = nm.getParamValues();
		f.function(params);
		System.out.println("pb = " + params[0]);
		System.out.println();
		return params[0];
	}

	private class PointWinningProbabilityFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double pb = param[0];
//			System.out.println("pb = " + pb);

			final double mwp = OMalley.matchInProgress(pa, pb, 3);
//			System.out.println("MWP = " + mwp);

//			System.out.println();
			return Math.abs(oddsMwp - mwp);
		}
	}
}
