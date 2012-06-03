package tennis.neldermead;

import tennis.simulator.MatchState;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRExp;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class NelderMead
{
	private final double decay = 0.75;
	private final double pa;
	private final double pb;
	private final MatchState matchState;
	private final double mwp;
	private final double riskA;
	private final double riskB;

	public NelderMead(final double pa, final double pb, final MatchState matchState, final double mwp, final double riskA, final double riskB)
	{
		this.pa = pa;
		this.pb = pb;
		this.matchState = matchState;
		this.mwp = mwp;
		this.riskA = riskA;
		this.riskB = riskB;
	}

	public Lambdas findLambdas()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {1000.0, 1000.0, 5000.0, 5000.0, 10000.0, 10000.0};
		final double [] step = {1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0};
		nm.addConstraint(0, -1, 0);
		nm.addConstraint(1, -1, 0);
		final RetirementRiskFunction f = new RetirementRiskFunction();
		nm.nelderMead(f, simplex, step, 0.005);

		System.out.println("\nFinal Answer");
		final double[] params = nm.getParamValues();
		System.out.println("LambdaA = " + params[0] + ", LambdaB = " + params[1]);
//		f.function(params);
//		System.out.println("Minimum = " + nm.getMinimum());

		return new Lambdas(params[0], params[1]);
	}

	private class RetirementRiskFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double lambdaA = param[0];
			final double lambdaB = param[1];

			final SimulatorWR simulator = new SimulatorWRExp(lambdaA, lambdaB, decay, true);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, matchState, true, 10000);
			final double targetMwpWR = outcomes.proportionTargetWon();
			final double opponentMwpWR = outcomes.proportionOpponentWon();
			final double rateA = outcomes.proportionTargetRetirements();
			final double rateB = outcomes.proportionOpponentRetirements();

//			System.out.println("MWP = " + mwp + ", RiskA = " + riskA + ", RiskB = " + riskB);
//			outcomes.minPrint("A", "B");
			final double targetNoRetMwp = targetMwpWR / (targetMwpWR + opponentMwpWR);
//			System.out.println("Target No Ret. MWP = " + targetNoRetMwp);
			final double opponentNoRetMwp = opponentMwpWR / (targetMwpWR + opponentMwpWR);
//			System.out.println("Opponent No Ret. MWP = " + opponentNoRetMwp);
			return Math.abs(rateA - riskA) + Math.abs(rateB - riskB) + Math.abs(mwp - targetNoRetMwp) + Math.abs((1 - mwp) - opponentNoRetMwp);
		}
	}
}
