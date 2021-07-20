package ctrl;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Engine;

@WebServlet({ "/Browse"})
public class Browse extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public Browse() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try
		{
			Engine engine = Engine.getInstance();
			request.setAttribute("catalogs", engine.getCatalogs());
			if(request.getParameter("orderBy")!= null)
			{
				String cat = request.getParameter("cat");
				if (cat != null && !cat.isEmpty() && !cat.equals("NONE"))
				{
					if(!request.getParameter("orderBy").equals("NONE"))
					{
						request.setAttribute("items",engine.getItems(cat, request.getParameter("orderBy")));						
					}
					else
					{
						request.setAttribute("items",engine.getItems(cat));
					}
					request.setAttribute("cat",request.getParameter("cat"));
				}
				else if (request.getParameter("number")!= null)
				{
					request.setAttribute("items",engine.searchItem(request.getParameter("number")));
					request.setAttribute("number",request.getParameter("number"));
				}
				request.getParameter("orderBy");
			}
			else if(request.getParameter("cat")!=null)
			{
				request.setAttribute("items",engine.getItems(request.getParameter("cat")));
				request.setAttribute("cat",request.getParameter("cat"));
				//To make sure that items does not disappear after users press continue shopping
				request.getSession().setAttribute("previousCat", request.getParameter("cat"));
			}
			else if(request.getParameter("select_item").equals("search"))
			{
				request.setAttribute("items",engine.searchItem(request.getParameter("number")));
				request.setAttribute("number",request.getParameter("number"));
			}
			//To make sure that items does not disappear after users press continue shopping
			else if (request.getSession().getAttribute("previousCat") != null) {
				request.setAttribute("items",engine.getItems(request.getSession().getAttribute("previousCat").toString()));
			}

		}
		catch (Exception e)
		{
			request.setAttribute("error", e.getMessage());
		}
		
		if (request.getSession().getAttribute("user") != null) {
			String user = (String) request.getSession().getAttribute("user");
			request.setAttribute("user", user);
		}
		
		this.getServletContext().getRequestDispatcher("/Browse.jspx").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

}
























