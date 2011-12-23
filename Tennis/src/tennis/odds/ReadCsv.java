package tennis.odds;

import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class ReadCsv {

	public static void main(final String[] args) throws IOException
	{
		final CSVReader reader = new CSVReader(new FileReader("doc\\match.csv"));
	    String [] nextLine;
	    while ((nextLine = reader.readNext()) != null) {
	        for (final String string : nextLine)
			{
	        	System.out.print(string + ", ");
			}
	        System.out.println();
	    }
	}
}
