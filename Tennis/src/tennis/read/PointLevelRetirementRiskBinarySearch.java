package tennis.read;

import tennis.simulator.GameState;
import tennis.simulator.MatchState;
import tennis.simulator.RetirementRisk;
import tennis.simulator.SetState;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRHyperExp;

public class PointLevelRetirementRiskBinarySearch
{
	private final double pa;
	private final double pb;
	private final double mwp;
	private final double gapA;
	private final double goalMwp;
	private final MatchState initialState;
	private final SimulatorWR simulator;

	public PointLevelRetirementRiskBinarySearch(final double pa, final double pb, final double mwp, final double gapA, final double gapB,
					   							final int targetSets, final int opponentSets, final int targetGames, final int opponentGames, final int targetPoints, final int opponentPoints,
					   							final boolean servingNext, final int numSetsToWin)
	{
		this.pa = pa;
		this.pb = pb;
		this.mwp = mwp;
		this.gapA = gapA;
		this.goalMwp = mwp - gapA;
		this.initialState = new MatchState(targetSets, opponentSets, new SetState(targetGames, opponentGames), new GameState(targetPoints, opponentPoints, servingNext), numSetsToWin);
    	this.simulator = new SimulatorWRHyperExp(0.000115, 10.0, 0.95, true);
	}

	public static void main(final String[] args)
	{
//		System.out.println(new PointLevelRetirementRiskBinarySearch(0.6658984375, 0.6241015625, 0.6777231777231777, 0.0065822381258622276, 0.0, 0, 0, 1, 1, 4, 5, false, 2).search());
		System.out.println(new PointLevelRetirementRiskBinarySearch(0.645, 0.645, 0.5, 0.05, 0.0, 0, 0, 1, 1, 4, 5, false, 3).search());
	}

	public double search()
	{
		double upperR = 0.25;
		double r = 0.0;
		double lowerR = 0.0;
		double targetMwpAfterFirstSet = -1;
		int iterations = 0;
		while(Math.abs(targetMwpAfterFirstSet - goalMwp) != 0 && iterations < 25)
		{
			r = (upperR + lowerR) / 2.0;

			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, new RetirementRisk(r, 0), initialState, true, 25000);
			final double targetMwpNormalWin = outcomes.proportionTargetWon();
			final double opponentMwpNormalWin = outcomes.proportionOpponentWon();
			final double targetMwpAfterOneBall = (targetMwpNormalWin + outcomes.proportionOpponentRetirements()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirements() + outcomes.proportionOpponentRetirements());
			targetMwpAfterFirstSet = (targetMwpNormalWin + outcomes.proportionOpponentRetirementsAfterFirstSet()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirementsAfterFirstSet() + outcomes.proportionOpponentRetirementsAfterFirstSet());

			if(targetMwpAfterFirstSet < goalMwp || (mwp - targetMwpAfterOneBall) > 2 * gapA)
			{
				upperR = r;
			}
			else
			{
				lowerR = r;
			}
			iterations++;
//			System.out.println("U = " + upperR + ", L = " + lowerR + ", r = " + r + ", Goal MWP = " + goalMwp);
//			System.out.println("MWP After First Set = " + targetMwpAfterFirstSet);
//			System.out.println("MWP After One Ball = " + targetMwpAfterOneBall);
//			outcomes.minPrint("A", "B");
//			System.out.println();
		}

		return r;
	}
}
