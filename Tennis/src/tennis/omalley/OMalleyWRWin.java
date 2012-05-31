package tennis.omalley;

import static java.lang.Math.pow;
import tennis.distributions.pareto.BoundedParetoDistribution;
import tennis.simulator.RetirementRisk;

public final class OMalleyWRWin
{
	private final double alphaA;
	private final double alphaB;
	private final double decay;
	private final boolean withRetirement;

	public OMalleyWRWin(final double alphaA, final double alphaB, final double decay, final boolean withRetirement)
	{
		this.alphaA = alphaA;
		this.alphaB = alphaB;
		this.decay = decay;
		this.withRetirement = withRetirement;
	}

	public OMalleyWRWin(final double alpha, final double decay, final boolean withRetirement)
	{
		this(alpha, alpha, decay, withRetirement);
	}

	public double tiebreak(final double pa, final double pb)
	{
		final double p = pa;
		final double q = 1 - pb;

		double result = 0;
		for (final double[] a : Matrices.tbMatrix)
		{
			result += a[0] * pow(p, a[1]) * pow(1 - p, a[2]) * pow(q, a[3]) * pow(1 - q, a[4]) * pow(d(p, q), a[5]);
		}
		return result;
	}

	private double d(final double p, final double q)
	{
		return p * q * pow(1 - (p * (1 - q) + (1 - p) * q), -1);
	}

	public double gameInProgress(final double pa)
	{
		return gameInProgress(pa, pa, new RetirementRisk(), new CurrentGameScore(), true);
	}

	public double gameInProgress(final double pa, final double pb, final RetirementRisk risk, final boolean serving)
	{
		return gameInProgress(pa, pb, risk, new CurrentGameScore(), serving);
	}

	public double gameInProgress(final double pa, final double pb, final RetirementRisk risk, final CurrentGameScore gameScore, final boolean serving)
	{
		final int a = gameScore.getTargetPoints();
		final int b = gameScore.getOpponentPoints();

		if (withRetirement)
		{
			if (alphaA >= 500)
			{
				risk.ra = 0;
			}
			else
			{
				final BoundedParetoDistribution riskA = new BoundedParetoDistribution(alphaA, decay);
				risk.ra *= decay;
				risk.ra += riskA.sample();
			}
			if (alphaB >= 500)
			{
				risk.rb = 0;
			}
			else
			{
				final BoundedParetoDistribution riskB = new BoundedParetoDistribution(alphaB, decay);
				risk.rb *= decay;
				risk.rb += riskB.sample();
			}
		}

		// p = probability target player wins this point, q = probability target player loses this point
		double p, q;
		if(serving)
		{
			p = pa / (1 + risk.ra + risk.rb);
			q = (1 - pa) / (1 + risk.ra + risk.rb);
		}
		else
		{
			p = (1 - pb) / (1 + risk.ra + risk.rb);
			q = pb / (1 + risk.ra + risk.rb);
		}

		if (a > b && a >= 4 && Math.abs(a - b) >= 2)
		{
			return 1.0;
		}
		if (b > a && b >= 4 && Math.abs(a - b) >= 2)
		{
			return 0.0;
		}
		if (a == b && a >= 3) // Deuce (sum of an infinite geometric series)
		{
			return Math.pow(p, 2) / (1 - 2 * p * (1 - p));
		}
		else
		{
			return (1 - risk.ra) * (1 - risk.rb) * (p * gameInProgress(pa, pb, risk, gameScore.incTargetPoints(), serving)
													+ q * gameInProgress(pa, pb, risk, gameScore.incOpponentPoints(), serving));
		}
	}

	public double setInProgress(final double pa, final double pb, final RetirementRisk risk, final boolean servingNext)
	{
		return setInProgress(pa, pb, risk, new CurrentSetScore(), new CurrentGameScore(), servingNext);
	}

	public double setInProgress(final double pa, final double pb, final boolean servingNext)
	{
		return setInProgress(pa, pb, new RetirementRisk(), new CurrentSetScore(), new CurrentGameScore(), servingNext);
	}

	public double setInProgress(final double pa, final double pb, final CurrentSetScore setScore, final boolean servingNext)
	{
		return setInProgress(pa, pb, new RetirementRisk(), setScore, new CurrentGameScore(), servingNext);
	}

	public double setInProgress(final double pa, final double pb, final RetirementRisk risk, final CurrentSetScore setScore, final boolean servingNext)
	{
		return setInProgress(pa, pb, risk, setScore, new CurrentGameScore(), servingNext);
	}

	public double setInProgress(final double pa, final double pb, final RetirementRisk risk,
								final CurrentSetScore setScore, final CurrentGameScore gameScore,
								final boolean servingNext)
	{
		final int targetGames = setScore.getTargetGames();
		final int opponentGames = setScore.getOpponentGames();

		if (targetGames == 7 || (targetGames == 6 && opponentGames < 5))
		{
			return 1.0;
		}
		if ((targetGames < 5 && opponentGames == 6) || opponentGames == 7)
		{
			return 0.0;
		}
		if (targetGames == 6 && opponentGames == 6)
		{
			return tiebreak(pa, pb);
		}
		final double g = gameInProgress(pa, pb, risk, gameScore, servingNext);
		return g * setInProgress(pa, pb, risk, setScore.incTargetGames(), new CurrentGameScore(), !servingNext) + (1 - g) * setInProgress(pa, pb, risk, setScore.incOpponentGames(), new CurrentGameScore(), !servingNext);
	}

	public double matchInProgress(final double pa, final double pb, final RetirementRisk risk,
								     	 final CurrentMatchScore matchScore, final CurrentSetScore setScore, final CurrentGameScore gameScore,
								     	 final boolean servingNext, final int numSetsForWin)
	{
		if (matchScore.getTargetSets() == numSetsForWin)
		{
			return 1.0;
		}
		if (matchScore.getOpponentSets() == numSetsForWin)
		{
			return 0.0;
		}
		else // Doesn't matter who serves first the next set because you don't know who served at the end of the previous set
		{
			final double s = setInProgress(pa, pb, risk, setScore, gameScore, servingNext);
			return s * matchInProgress(pa, pb, risk, matchScore.incTargetSets(), numSetsForWin) + (1 - s) * matchInProgress(pa, pb, risk, matchScore.incOpponentSets(), numSetsForWin);
		}
	}

	public double matchInProgress(final double pa, final double pb, final RetirementRisk risk, final CurrentMatchScore matchScore, final int numSetsForWin)
	{
		return matchInProgress(pa, pb, risk, matchScore, new CurrentSetScore(), (Math.random() < 0.5) ? true : false, numSetsForWin);
	}

	public double matchInProgress(final double pa, final double pb, final RetirementRisk risk, final CurrentMatchScore matchScore, final CurrentSetScore setScore, final boolean servingNext, final int numSetsForWin)
	{
		return matchInProgress(pa, pb, risk, matchScore, setScore, new CurrentGameScore(), servingNext, numSetsForWin);
	}

	public double matchInProgress(final double pa, final double pb, final int numSetsForWin)
	{
		return matchInProgress(pa, pb, new RetirementRisk(), new CurrentMatchScore(), numSetsForWin);
	}
}
