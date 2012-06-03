package tennis.neldermead.simulator.exp;

import org.junit.Test;

import tennis.neldermead.simulator.pareto.TestNelderMead;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRExp;
import flanagan.math.Minimisation;
import flanagan.math.MinimisationFunction;

public class TestConstrainedBothPlayersNelderMeadExpSimulator extends TestNelderMead
{
	private final double riskA = 0.2;
	private final double riskB = 0.2;

	@Test
	public void testRetirementRiskFitTwoPlayer()
	{
		final Minimisation nm = new Minimisation();
		final double [] simplex = {1000.0, 1000.0, 5000.0, 5000.0, 10000.0, 10000.0};
		final double [] step = {1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0};
		nm.addConstraint(0, -1, 0);
		nm.addConstraint(1, -1, 0);
		final RetirementRiskBothPlayersFunction f = new RetirementRiskBothPlayersFunction();
		nm.nelderMead(f, simplex, step, 0.005);

		System.out.println("\nFinal Answer");
		f.function(nm.getParamValues());
		System.out.println("Minimum = " + nm.getMinimum());
		System.out.println();
	}

	private class RetirementRiskBothPlayersFunction implements MinimisationFunction
	{
		@Override
		public double function(final double[] param)
		{
			final double lambdaA = param[0];
			final double lambdaB = param[1];
			final double decay = 0.85;
			System.out.println("LambdaA = " + lambdaA + ", LambdaB = " + lambdaB + ", Decay = " + decay);

			final SimulatorWR simulator = new SimulatorWRExp(lambdaA, lambdaB, decay, true);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, initialState, true, 50000);
			final double targetMwpWR = outcomes.proportionTargetWon();
			final double opponentMwpWR = outcomes.proportionOpponentWon();
			final double rateA = outcomes.proportionTargetRetirements();
			final double rateB = outcomes.proportionOpponentRetirements();

			System.out.println("MWP = " + mwp + ", RiskA = " + riskA + ", RiskB = " + riskB);
			outcomes.minPrint("A", "B");
			final double targetNoRetMwp = targetMwpWR / (targetMwpWR + opponentMwpWR);
			System.out.println("Target No Ret. MWP = " + targetNoRetMwp);
			final double opponentNoRetMwp = opponentMwpWR / (targetMwpWR + opponentMwpWR);
			System.out.println("Opponent No Ret. MWP = " + opponentNoRetMwp);
			return Math.abs(rateA - riskA) + Math.abs(rateB - riskB) + Math.abs(mwp - targetNoRetMwp) + Math.abs((1 - mwp) - opponentNoRetMwp);
		}
	}
}
