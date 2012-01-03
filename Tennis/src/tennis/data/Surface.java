package tennis.data;

public enum Surface
{
	ALL(0),	HARD(1), INDOOR(2), CARPET(3), CLAY(10), GRASS(20);

	private final int value;

	private Surface(final int value)
	{
		this.value = value;
	}

	public int getValue()
	{
	    return value;
	}
}
