package tennis.data;

public enum ActivitySurface
{
	ALL(1),	HARD(15), INDOOR(16), CARPET(17), CLAY(18), GRASS(19);

	private final int value;

	private ActivitySurface(final int value)
	{
		this.value = value;
	}

	public int getValue()
	{
	    return value;
	}
}
