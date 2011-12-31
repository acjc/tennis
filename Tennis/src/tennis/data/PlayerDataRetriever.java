package tennis.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import edu.depauw.csc.dcheeseman.wgetjava.WGETJavaResults;

public class PlayerDataRetriever
{
	public int getPlayerId(final String playerHtml) throws IOException
	{
		final Pattern pattern = Pattern.compile("player_id=(\\d*)");
		final Matcher matcher = pattern.matcher(playerHtml);
		matcher.find();
		return Integer.parseInt(matcher.group(1));
	}

	public String readFileToString(final File file) throws IOException
	{
		return FileUtils.readFileToString(file);
	}

	public int getNumberOfMatches(final String html)
	{
		final String text = html.replaceAll("\\<.*?>","");
		final Pattern pattern = Pattern.compile("\\((\\d*)-(\\d*)\\)\\s*Match W/L");
		final Matcher matcher = pattern.matcher(text);
		matcher.find();
		return Integer.parseInt(matcher.group(1)) + Integer.parseInt(matcher.group(2));
	}

	public String findStat(final String html, final String stat)
	{
		final String text = html.replaceAll("\\<.*?>","");
		final Pattern pattern = Pattern.compile("([\\d\\.%]+).*\\s*" + stat);
		final Matcher matcher = pattern.matcher(text);
		matcher.find();
		return matcher.group(1);
	}

	public void printCookies() throws IOException
	{
		final URL myUrl = new URL("http://www.tennisinsight.com/");
		final URLConnection connection = myUrl.openConnection();
		connection.connect();

		String headerName = null;
		for (int i=1; (headerName = connection.getHeaderFieldKey(i))!=null; i++)
		{
		 	if (headerName.equals("Set-Cookie"))
		 	{
		 		String cookie = connection.getHeaderField(i);
		 		cookie = cookie.substring(0, cookie.indexOf(";"));
		        System.out.println("Name: " + cookie.substring(0, cookie.indexOf("=")) + ", " +
		        				   "Value: " + cookie.substring(cookie.indexOf("=") + 1, cookie.length()));
		 	}
		}
	}

	public WGETJavaResults downloadFile(final URL theUrl, final File file) throws IOException {
		URLConnection connection;
		connection = theUrl.openConnection();
		final String cookies = "cookTube4=Better+Off; cookname=debapriyapal; cookpass=enigmatic";
		connection.setRequestProperty("Cookie", cookies);
		connection.connect();

		System.out.println("Downloading: " + theUrl.toString());

		final String type = connection.getContentType();
		if (type != null)
		{
			final byte[] buffer = new byte[4 * 1024];
			int read;

			final FileOutputStream os = new FileOutputStream(file);
			final InputStream in = connection.getInputStream();

			while ((read = in.read(buffer)) > 0) {
				os.write(buffer, 0, read);
			}

			os.close();
			in.close();

			return WGETJavaResults.COMPLETE;
		}
		else
		{
			return WGETJavaResults.FAILED_UKNOWNTYPE;
		}
	}
}
