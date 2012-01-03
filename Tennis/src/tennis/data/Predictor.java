package tennis.data;

import java.io.IOException;

public class Predictor
{
	public static void main(final String[] args) throws IOException
	{
		final Player p1 = new Player("Roger Federer");
		final Player p2 = new Player("Jo-Wilfried Tsonga");
		p1.adjustStatistics(p2);
		p2.adjustStatistics(p1);
	}
}
