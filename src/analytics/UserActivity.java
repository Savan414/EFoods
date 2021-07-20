package analytics;

public class UserActivity
{
	String sessionId;
	long startTime;
	final int SIZE = 100;
	long[] addItemTime;
	int counter;
	int limitCounter;
	long checkoutTime;
	
	//Construct a UserActivity object, initializing sessionId and startTime
	UserActivity(String sessionId, long startTime)
	{
		this.sessionId = sessionId;
		this.startTime = startTime;
		addItemTime = new long[SIZE];
		counter = 0;
		limitCounter = SIZE;
	}
	
	void logAddItem (long time)
	{
		addItemTime[counter++] = time;
		if (counter > limitCounter -1)
		{
			limitCounter = limitCounter + SIZE;
			long[] newArray = new long[limitCounter];
			System.arraycopy(addItemTime, 0, newArray, 0, counter);
			addItemTime = newArray;
		}
	}
}