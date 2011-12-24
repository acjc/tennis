package tennis.odds;

import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class ReadCsv
{
	public static void main(final String[] args) throws IOException
	{
		final CSVReader reader = new CSVReader(new FileReader("doc\\sharapova.csv"));
	    printLine(reader.readNext());
	    printLine(reader.readNext());
	    String [] nextLine;
	    while ((nextLine = reader.readNext()) != null) {
	        printLpm(nextLine);
	    }
	}

	private static void printLpm(final String[] nextLine)
	{
		System.out.print("Time: " + nextLine[1] + ", Player: " + nextLine[4] + ", LPM: " + nextLine[10] + '\n');
	}

	private static void printLine(final String[] nextLine)
	{
		for (final String string : nextLine)
		{
			System.out.print(string + ", ");
		}
	}
}
