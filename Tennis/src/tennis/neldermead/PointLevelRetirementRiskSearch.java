package tennis.neldermead;

import tennis.omalley.CurrentGameScore;
import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalley;
import tennis.simulator.GameState;
import tennis.simulator.MatchState;
import tennis.simulator.RetirementRisk;
import tennis.simulator.SetState;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRHyperExp;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class PointLevelRetirementRiskSearch
{
	private final double chance = 0.000115;
	private final double lambda = 10.0;
	private final double decay = 0.95;
	private final SimulatorWR simulator = new SimulatorWRHyperExp(chance, lambda, decay, true);
	private final double pa;
	private final double pb;
	private final double riskA;
	private final double riskB;
	private final double mwp;
	private final MatchState initialState;

	public PointLevelRetirementRiskSearch(final double pa, final double pb, final double riskA, final double riskB,
										  final int targetSets, final int opponentSets, final int targetGames, final int opponentGames, final int targetPoints, final int opponentPoints,
										  final boolean servingNext, final int numSetsToWin)
	{
		this.pa = pa;
		this.pb = pb;
		this.riskA = riskA;
		this.riskB = riskB;
    	this.mwp = OMalley.matchInProgress(pa, pb, new CurrentMatchScore(targetSets, opponentSets), new CurrentSetScore(targetGames, opponentGames), new CurrentGameScore(targetPoints, opponentPoints), servingNext, numSetsToWin);
    	this.initialState = new MatchState(targetSets, opponentSets, new SetState(targetGames, opponentGames), new GameState(targetPoints, opponentPoints, true), numSetsToWin);
	}

	public double search()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {0.001, 0.01, 0.1};
		final double [] step = {0.01, 0.01, 0.01};
		nm.addConstraint(0, -1, 0);
		final RetirementRiskFunction f = new RetirementRiskFunction();
		nm.nelderMead(f, simplex, step, 0.001, 50);

		System.out.println("Final Answer");
		final double[] params = nm.getParamValues();
		f.function(params);
		System.out.println("ra = " + params[0]);
		System.out.println();

		return params[0];
	}

	public static void main(final String[] args)
	{
		System.out.println(new PointLevelRetirementRiskSearch(0.6842333984375, 0.6057666015625001, 0.2606, 0.0, 0, 0, 0, 0, 0, 0, true, 3).search());
	}

	private class RetirementRiskFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double ra = param[0];
//			System.out.println("ra = " + ra);

			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, new RetirementRisk(ra, 0), initialState, true, 10000);
			final double targetMwpWR = outcomes.proportionTargetWon();

//			System.out.println("MWP = " + mwp + ", RiskA = " + riskA + ", RiskB = " + riskB);
//			outcomes.minPrint("A", "B");
//			System.out.println();
			return Math.abs(mwp - (targetMwpWR + riskA));
		}
	}
}
