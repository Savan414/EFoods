package analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import jakarta.servlet.http.HttpServletRequest;

import model.ItemBean;
import model.OrderBean;

/**
 * Application Lifecycle Listener implementation class Monitor
 *
 */
@WebListener
public class Monitor implements 
	HttpSessionListener, ServletRequestListener 
{
    public Monitor() 
    {
    }

	public void sessionCreated(HttpSessionEvent se)  
	{
		attachOrder (se);
		addUser(se);
	}
	//attach Order Object for the new session(new user)
	private void attachOrder(HttpSessionEvent se)
	{
		//attach order (shopping cart) object to Session context
		HttpSession session = se.getSession();
		List<ItemBean> items = new ArrayList<ItemBean>();
		OrderBean order = new OrderBean(items);
		session.setAttribute(AnalyticEngine.ORDER, order);
	}
	//Add the new session/user to the list of 'userActivity' stored in Servlet contextt 
	private void addUser(HttpSessionEvent se)
	{
		//Add client to list of "clients" in ServletContext to monitor all users activity
		ServletContext servlet =se.getSession().getServletContext();	
		if (servlet.getAttribute(AnalyticEngine.CLIENTS) == null)
		{
			servlet.setAttribute(AnalyticEngine.CLIENTS, new HashMap<String, UserActivity>());
		}
		
		@SuppressWarnings("unchecked")
		Map<String, UserActivity> clients = (Map<String, UserActivity>)servlet.getAttribute("clients");
		clients.put(se.getSession().getId(), 
				new UserActivity(se.getSession().getId(), System.currentTimeMillis()));
	}
	
	
	public void requestInitialized(ServletRequestEvent sre)  
	{ 
        //check if it is addItem Request
		HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
		if(request.getParameter("select_item")!= null)
			logAddItem(sre);
		if(request.getParameter("checkout")!= null && request.getParameter("checkout").equals("confirm"))
			logCheckOut(sre);
	}
	
	

	//to log the adding of item by a user;
	private void logAddItem(ServletRequestEvent sre)
	{
		ServletContext servlet = sre.getServletContext();
		String id = ((HttpServletRequest) sre.getServletRequest()).getSession().getId();
		@SuppressWarnings("unchecked")
		Map<String, UserActivity> users = 
				(Map<String, UserActivity>)servlet.getAttribute(AnalyticEngine.CLIENTS);
		UserActivity user = users.get(id);
		user.logAddItem(System.currentTimeMillis());
	}
	
	private void logCheckOut(ServletRequestEvent sre)
	{
		//TODO: has not implemented the invoking of checkout event. 
		ServletContext server = sre.getServletContext();
		@SuppressWarnings("unchecked")
		Map<String, UserActivity> users = 
				(Map<String, UserActivity>) server.getAttribute(AnalyticEngine.CLIENTS);
		
		UserActivity user =	users.get(((HttpServletRequest)sre.getServletRequest()).getSession().getId());
		user.checkoutTime = System.currentTimeMillis();
	}
}

