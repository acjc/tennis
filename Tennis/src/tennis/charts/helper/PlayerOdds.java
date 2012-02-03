package tennis.charts.helper;

import java.io.File;

public class PlayerOdds
{
	private final String title;
	private final String firstName;
	private final String surname;
	private final String matchType;

	public PlayerOdds(final String name, final String matchType, final String title)
	{
		this.title = title;
		this.matchType = matchType;
		final String [] names = name.split(" ");
		this.firstName = names[0];
		this.surname = names[1];
	}

	public String getTitle()
	{
		return title + " (" + surname + " Odds)";
	}

	private String getFolderName()
	{
		return "doc\\" + matchType + File.separator + title;
	}

	private String getName()
	{
		return firstName + " " + surname;
	}

	public File getMatchOdds()
	{
		return new File(getFolderName() + File.separator + getName() + ".csv");
	}

	public File getTwoNil()
	{
		return new File(getFolderName() + File.separator + surname + " " + "2 - 0.csv");
	}

	public File getTwoOne()
	{
		return new File(getFolderName() + File.separator + surname + " " + "2 - 1.csv");
	}

	public File getThreeNil()
	{
		return new File(getFolderName() + File.separator + surname + " " + "3 - 0.csv");
	}

	public File getThreeOne()
	{
		return new File(getFolderName() + File.separator + surname + " " + "3 - 1.csv");
	}

	public File getThreeTwo()
	{
		return new File(getFolderName() + File.separator + surname + " " + "3 - 2.csv");
	}
}
