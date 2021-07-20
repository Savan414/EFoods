package ctrl;

import java.io.IOException;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Engine;
import model.OrderBean;

/**
 * Servlet implementation class Checkout
 */
@WebServlet("/Checkout")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Checkout() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getSession().setAttribute("back", request.getRequestURL().toString());
		OrderBean order = (OrderBean) request.getSession().getAttribute("order");
		//User is guranteed to have a value because a filter takes cares of that
		String user = (String) request.getSession().getAttribute("user");
		request.setAttribute("user", user);
		
		//checkout only if the user is logged in and the cart is not empty and getParameter 
		if (!order.isEmpty() && request.getParameter("force") != null && user != null) {
	
			
			order.setSubmitted(new Date());
			order.setAccount(user);

			try {
				Engine engine = Engine.getInstance();
				int count = engine.increment();
				order.setOrderNumber(count);
				request.setAttribute("order", order);
				
				engine.jaxbObjectToXML(order, "PO_" + count, engine.getXmlFolderPath()); 
				order.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
			
	    try {
				  Engine engine = Engine.getInstance();
				  request.setAttribute("fileNames", engine.getXMLLinks(user, engine.getXmlFolderPath()));
				  request.setAttribute("processedfileNames", engine.getXMLLinks(user, engine.getXmlPOProcessedFolderPath()));
				 
		} catch (Exception e) {
			   e.printStackTrace();
		}			
		
		
		this.getServletContext().getRequestDispatcher("/Checkout.jspx").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}