package ctrl;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import analytics.AnalyticEngine;
import analytics.UserActivity;

/**
 * Servlet implementation class Analytics
 */
@WebServlet("/Analytics")
public class Analytics extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public Analytics() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		AnalyticEngine engine = AnalyticEngine.getInstance();
		
		@SuppressWarnings("unchecked")
		double aveAddItem = engine.computeAveAddItemInterval((Map<String, UserActivity>)
				request.getServletContext().getAttribute(AnalyticEngine.CLIENTS));
		System.out.println("aveAddItem=" + aveAddItem);
		if(aveAddItem > 0)
		{
			request.setAttribute("aveAddItem", aveAddItem);	
		}
		else
		{
			request.setAttribute("rpt_AddItem", "No Users Yet");
		}
		
		
		@SuppressWarnings("unchecked")
		double aveCheckout = engine.computeCheckOutInterval(((Map<String, UserActivity>)
				request.getServletContext().getAttribute(AnalyticEngine.CLIENTS))); 
		System.out.println("aveCheckout=" + aveCheckout);
		if(aveCheckout > 0)
		{
			request.setAttribute("aveCheckout", aveCheckout);	
		}
		else
		{
			request.setAttribute("rpt_Checkout", "No Users Checked out Yet");
		}
		
		
		this.getServletContext().getRequestDispatcher("/Analytics.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

}