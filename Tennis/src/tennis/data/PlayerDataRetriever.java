package tennis.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public WGETJavaResults downloadFile(final URL theUrl) throws IOException {
		URLConnection con;
		con = theUrl.openConnection();
		con.connect();

		final String type = con.getContentType();
		if (type != null)
		{
			final byte[] buffer = new byte[4 * 1024];
			int read;

			final FileOutputStream os = new FileOutputStream("doc\\player.html");
			final InputStream in = con.getInputStream();

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
