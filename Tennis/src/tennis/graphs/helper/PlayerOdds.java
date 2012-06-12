package tennis.graphs.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class PlayerOdds
{
	public static final int TIME_INDEX = 0;
	public static final int DATE_INDEX = 1;
	public static final int BACK_INDEX = 2;
	public static final int LAY_INDEX = 4;
	public static final int LPM_INDEX = 6;

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
		return title;
	}

	private String getFolderName()
	{
		return "doc\\" + matchType + File.separator + title;
	}

	public String getName()
	{
		return firstName + " " + surname;
	}

	public String getSurname()
	{
		return surname;
	}

	public CSVReader getMatchOdds() throws FileNotFoundException
	{
		return new CSVReader(new FileReader(new File(getFolderName() + File.separator + getName() + ".csv")));
	}

	private File getTwoNil()
	{
		return new File(getFolderName() + File.separator + surname + " " + "2 - 0.csv");
	}

	private File getTwoOne()
	{
		return new File(getFolderName() + File.separator + surname + " " + "2 - 1.csv");
	}

	private File getThreeNil()
	{
		return new File(getFolderName() + File.separator + surname + " " + "3 - 0.csv");
	}

	private File getThreeOne()
	{
		return new File(getFolderName() + File.separator + surname + " " + "3 - 1.csv");
	}

	private File getThreeTwo()
	{
		return new File(getFolderName() + File.separator + surname + " " + "3 - 2.csv");
	}

	public List<CSVReader> getSetOdds() throws FileNotFoundException
	{
		final List<CSVReader> setOdds = new ArrayList<CSVReader>();
		if (getThreeOne().isFile())
		{
			setOdds.add(new CSVReader(new FileReader(getThreeNil())));
			setOdds.add(new CSVReader(new FileReader(getThreeOne())));
			setOdds.add(new CSVReader(new FileReader(getThreeTwo())));
		}
		else
		{
			setOdds.add(new CSVReader(new FileReader(getTwoNil())));
			setOdds.add(new CSVReader(new FileReader(getTwoOne())));
		}
		return setOdds;
	}
}
