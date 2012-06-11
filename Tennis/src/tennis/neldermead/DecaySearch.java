package tennis.neldermead;

import static tennis.distributions.ProbabilityDistribution.twister;
import tennis.omalley.CurrentGameScore;
import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalley;
import tennis.simulator.GameState;
import tennis.simulator.MatchState;
import tennis.simulator.SetState;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRExp;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class DecaySearch
{
	private final double pa;
	private final double pb;
	private final MatchState matchState;
	private final double mwp;
	private final double riskA;
	private final double riskB;

	public DecaySearch(final double pa, final double pb, final MatchState matchState, final double mwp, final double riskA, final double riskB)
	{
		this.pa = pa;
		this.pb = pb;
		this.matchState = matchState;
		this.mwp = mwp;
		this.riskA = riskA;
		this.riskB = riskB;
	}

	public static void main(final String[] args)
	{
		double result = 0;
		for(int i = 0; i < 10; i++)
		{
			double pa = twister.nextDouble();
			pa = (pa * 0.12) + 0.58;
			double pb = twister.nextDouble();
			pb = (pb * 0.12) + 0.58;
			final int targetSets = twister.nextInt(3);
			final int opponentSets = twister.nextInt(3);
			final int targetGames = twister.nextInt(6);
			final int opponentGames = twister.nextInt(6);
			final int targetPoints = twister.nextInt(4);
			final int opponentPoints = twister.nextInt(4);
			final int numSetsForWin = 3;
			final boolean servingNext = twister.nextDouble() < 0.5 ? true : false;
			final double mwp = OMalley.matchInProgress(pa, pb, new CurrentMatchScore(targetSets, opponentSets), new CurrentSetScore(targetGames, opponentGames), new CurrentGameScore(targetPoints, opponentPoints), servingNext, numSetsForWin);
			final MatchState matchState = new MatchState(targetSets, opponentSets, new SetState(targetGames, opponentGames), new GameState(targetPoints, opponentPoints, servingNext), numSetsForWin);
			final double riskA = twister.nextDouble() * mwp;
			final double riskB = twister.nextDouble() * (1 - mwp);
			System.out.println("pa = " + pa + ", pb = " + pb);
			System.out.println("(" + targetSets + ", " + opponentSets + "), " + "(" + targetGames + ", " + opponentGames + "), " + "(" + targetPoints + ", " + opponentPoints + ")");
			result += new DecaySearch(pa, pb, matchState, mwp, riskA, riskB).findDecay();
		}
		System.out.println(result / 10.0);
	}

	public double findDecay()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {1000.0, 0.1, 5000.0, 0.5, 10000.0, 0.9};
		final double [] step = {1000.0, 0.25, 1000.0, 0.25, 1000.0, 0.25};
		nm.addConstraint(0, -1, 0.0001);
		nm.addConstraint(1, -1, 0.5);
		nm.addConstraint(1, 1, 1.0);
		final RetirementRiskFunction f = new RetirementRiskFunction();
		nm.nelderMead(f, simplex, step, 0.005);

		System.out.println("\nFinal Answer");
		final double[] params = nm.getParamValues();
		System.out.println("LambdaA = " + params[0] + ", Decay = " + params[1]);
//		f.function(params);
//		System.out.println("Minimum = " + nm.getMinimum());
		System.out.println();

		return params[1];
	}

	private class RetirementRiskFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double lambdaA = param[0];
			final double decay = param[1];
//			System.out.println("LambdaA = " + lambdaA + ", Decay = " + decay);

			final SimulatorWR simulator = new SimulatorWRExp(lambdaA, -1, decay, true);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, matchState, true, 10000);
			final double targetMwpWR = outcomes.proportionTargetWon();
			final double opponentMwpWR = outcomes.proportionOpponentWon();
			final double rateA = outcomes.proportionTargetRetirements();
			final double rateB = outcomes.proportionOpponentRetirements();

//			outcomes.minPrint("A", "B");
			final double targetNoRiskMwp = targetMwpWR / (targetMwpWR + opponentMwpWR);
			final double opponentNoRiskMwp = opponentMwpWR / (targetMwpWR + opponentMwpWR);
//			System.out.println();
			return Math.abs(rateA - riskA) + Math.abs(rateB - riskB) + Math.abs(mwp - targetNoRiskMwp) + Math.abs((1 - mwp) - opponentNoRiskMwp);
		}
	}
}
