package analytics;

import java.util.Map;

public class AnalyticEngine
{
	//list of attribute name
	public static final String ORDER = "order";
	public static final String CLIENTS = "clients";
	
	private static AnalyticEngine instance = null;
	
	private AnalyticEngine()
	{		
	}
	
	public static AnalyticEngine getInstance()
	{
		if (instance == null)
			instance = new AnalyticEngine();
		return instance;
	}
	
	public double computeAveAddItemInterval(Map<String, UserActivity> users)
	{
		long totalTime = 0;
		int numberOfIntervals = 0;
		for(UserActivity user :users.values())
		{
			long last = user.startTime;
			int user_c = user.counter;
			if (user_c > 0)
			{
				long[] timeLog = user.addItemTime;
				for(int i = 0; i < user_c; i++)
				{
					totalTime = totalTime + timeLog[i] - last;
					last = timeLog[i];
					numberOfIntervals ++;
				}				
			}
		}
		if (numberOfIntervals < 1 ) //in case there is no one has entered yet.
			return -1;
		double ave = (double)totalTime/1000/numberOfIntervals;
		return ave;
	}
	
	public double computeCheckOutInterval(Map<String, UserActivity> users)
	{
		long totalTime = 0;
		int numberOfUsers = 0;
		for(UserActivity user: users.values())
		{
			long first = user.startTime;
			long checkOut = user.checkoutTime;
			if(checkOut > 0 )
			{
				totalTime = totalTime + checkOut - first;
				numberOfUsers ++;				
			}
		}
		if (numberOfUsers < 1 ) //in case there is no one has entered yet.
			return -1;
		return (double)totalTime / numberOfUsers;
	}
	
	
	
	
}