package tennis.neldermead;

import tennis.simulator.GameState;
import tennis.simulator.MatchState;
import tennis.simulator.RetirementRisk;
import tennis.simulator.SetState;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRHyperExp;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class NelderMeadPointLevelRetirementRiskSearch
{
	private final double chance = 0.000115;
	private final double lambda = 10.0;
	private final double decay = 0.95;
	private final SimulatorWR simulator;
	private final double pa;
	private final double pb;
	private final double gapA;
	private final double gapB;
	private final double mwp;
	private final MatchState initialState;

	public NelderMeadPointLevelRetirementRiskSearch(final double pa, final double pb, final double mwp, final double gapA, final double gapB,
										  			final int targetSets, final int opponentSets, final int targetGames, final int opponentGames, final int targetPoints, final int opponentPoints,
										  			final boolean servingNext, final int numSetsToWin)
	{
		this.pa = pa;
		this.pb = pb;
		this.gapA = gapA;
		this.gapB = gapB;
    	this.mwp = mwp;
    	this.initialState = new MatchState(targetSets, opponentSets, new SetState(targetGames, opponentGames), new GameState(targetPoints, opponentPoints, servingNext), numSetsToWin);
    	this.simulator = new SimulatorWRHyperExp(chance, lambda, decay, true);
	}

	public double search()
	{
		final Minimisation nm = new Minimisation();
//		final double [] simplex = {0.001, 0.01};
//		final double [] step = {0.001, 0.001};
		final double [] simplex = {0.001, 0.01};
		final double [] step = {0.005, 0.005};
		nm.addConstraint(0, -1, 0);
		final RetirementRiskFunction f = new RetirementRiskFunction();
		nm.nelderMead(f, simplex, step, 0.001, 50);

		System.out.println("Final Answer");
		final double[] params = nm.getParamValues();
		f.function(params);
//		System.out.println("ra = " + params[0]);
		System.out.println();

		return params[0];
	}

	public static void main(final String[] args)
	{
		System.out.println(new NelderMeadPointLevelRetirementRiskSearch(0.645, 0.645, 0.5, 0.1, 0.0, 0, 0, 3, 4, 0, 0, true, 3).search());
	}

	private class RetirementRiskFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double ra = param[0];

			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, new RetirementRisk(ra, 0), initialState, true, 10000);
			final double targetMwpNormalWin = outcomes.proportionTargetWon();
			final double opponentMwpNormalWin = outcomes.proportionOpponentWon();
			final double targetMwpAfterFirstSet = (targetMwpNormalWin + outcomes.proportionOpponentRetirementsAfterFirstSet()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirementsAfterFirstSet() + outcomes.proportionOpponentRetirementsAfterFirstSet());
			final double targetMwpAfterOneBall = (targetMwpNormalWin + outcomes.proportionOpponentRetirements()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirements() + outcomes.proportionOpponentRetirements());

			System.out.println("ra = " + ra);
			System.out.println("MWP After First Set = " + targetMwpAfterFirstSet);
			System.out.println("MWP After One Ball = " + targetMwpAfterOneBall);
			System.out.println("Target Ret. After First Set = " + outcomes.proportionTargetRetirementsAfterFirstSet());
			System.out.println("MWP = " + mwp + ", GapA = " + gapA + ", GapB = " + gapB);
			outcomes.minPrint("A", "B");
			System.out.println();

//			return Math.abs(mwp - (targetMwpAfterFirstSet + gapA));
			return Math.abs(mwp - (targetMwpAfterFirstSet + gapA)) + Math.abs((2 * gapA) - (mwp - targetMwpAfterFirstSet));
		}
	}
}
