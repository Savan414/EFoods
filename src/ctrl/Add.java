package ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Engine;
import model.OrderBean;

@WebServlet("/Add")
public class Add extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public Add() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession();
		session.setAttribute("back", request.getRequestURL().toString());
		if (session.getAttribute("user") != null) {
			String user = (String) session.getAttribute("user");
			request.setAttribute("user", user);
		}
		OrderBean order = ((OrderBean) session.getAttribute("order"));
		try
		{
			Engine engine = Engine.getInstance();		

			if(request.getParameter("select_item")!=null)//add item
			{
				String number = request.getParameter("number");
				String quantity = request.getParameter("qty");
				engine.orderAddItem(order, number, quantity);
			}
			if(request.getParameter("update")!=null)//update carts
			{
				List<String> toDelete = new ArrayList<String>();
				Map<String, String> itemsQty = new HashMap<String, String>();
				List<String> parameters = Collections.list(request.getParameterNames());
				for(String parameter: parameters)
				{
					if (parameter.startsWith("delete"))
						toDelete.add(request.getParameter(parameter));
					else if(parameter.matches("\\d{4}[A-Z|a-z]\\d{3}"))
						itemsQty.put(parameter, request.getParameter(parameter));
				}
				engine.updateOrder(order, itemsQty, toDelete);
			}
		}
		catch (Exception e)
		{
				request.setAttribute("error", e.getMessage());
		}			

		String  checkoutName = "checkout";
		if (session.getAttribute("user") != null)
			checkoutName = "confirm";
		request.setAttribute("checkout", checkoutName);
		
		request.setAttribute("order", order);	
		this.getServletContext().getRequestDispatcher("/Add.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

}