package tennis.neldermead;

import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRHyperExp;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class GlobalParamSearch
{
	private final double pa;
	private final double pb;
	private final double riskA;
	private final double riskB;
	private final double mwp;

	public GlobalParamSearch(final double pa, final double pb, final double riskA, final double riskB, final double mwp)
	{
		this.pa = pa;
		this.pb = pb;
		this.riskA = riskA;
		this.riskB = riskB;
		this.mwp = mwp;
	}

	public static void main(final String[] args)
	{
		final double pa = 0.6;
		final double pb = 0.6;
		double chanceAvg = 0.0;
		double lambdaAvg = 0.0;
		double decayAvg = 0.0;
		for (int i = 0; i < 10; i++)
		{
			final double [] results = new GlobalParamSearch(pa, pb, 0.0195, 0.0195, 0.5).runNelderMead();
			chanceAvg += results[0];
			lambdaAvg += results[1];
			decayAvg += results[2];
		}
		System.out.println("ChanceAvg = " + chanceAvg / 10.0 + ", LambdaAvg = " + lambdaAvg / 10.0 + ", DecayAvg = " + decayAvg / 10.0);
	}

	public double [] runNelderMead()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {0.0001, 5.0, 0.95,
								   0.0002, 12.0, 0.96,
								   0.0007, 18.0, 0.97,
								   0.001, 25.0, 0.99};
		final double [] step = {0.0001, 5.0, 0.05,
								0.0001, 5.0, 0.05,
								0.0001, 5.0, 0.05,
								0.0001, 5.0, 0.05};
		nm.addConstraint(0, -1, 0.000001);
		nm.addConstraint(0, 1, 0.002);
		nm.addConstraint(1, -1, 2.0);
		nm.addConstraint(2, -1, 0.95);
		nm.addConstraint(2, 1, 0.99);
		final RetirementRiskFunction f = new RetirementRiskFunction();
		nm.nelderMead(f, simplex, step, 0.005);

		System.out.println("\nFinal Answer");
		final double[] params = nm.getParamValues();
		f.function(params);
		System.out.println("Minimum = " + nm.getMinimum());
		System.out.println();
		return params;
	}

	private class RetirementRiskFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double chance = param[0];
			final double lambda = param[1];
			final double decay = param[2];
			System.out.println("Chance = " + chance + ", Lambda = " + lambda + ", Decay = " + decay);

			final SimulatorWR simulator = new SimulatorWRHyperExp(chance, lambda, decay, true);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, 3, 10000);
			final double targetMwpWR = outcomes.proportionTargetWon();
			final double opponentMwpWR = outcomes.proportionOpponentWon();
			final double rateA = outcomes.proportionTargetRetirements();
			final double rateB = outcomes.proportionOpponentRetirements();

			outcomes.minPrint("A", "B");
			final double targetNoRiskMwp = targetMwpWR / (targetMwpWR + opponentMwpWR);
			final double opponentNoRiskMwp = opponentMwpWR / (targetMwpWR + opponentMwpWR);
			System.out.println();
			return Math.abs(rateA - riskA) + Math.abs(rateB - riskB) + Math.abs(mwp - targetNoRiskMwp) + Math.abs((1 - mwp) - opponentNoRiskMwp);
		}
	}
}
