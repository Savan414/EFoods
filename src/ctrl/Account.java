package ctrl;

import java.io.File;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Engine;
import model.OrderBean;


@WebServlet("/Account")
public class Account extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.getSession().setAttribute("back", request.getRequestURL().toString());
		String user = (String) request.getSession().getAttribute("user");
		request.setAttribute("user", user);
			
			try {
			  Engine engine = Engine.getInstance();
			  if (request.getParameter("view") != null) {
						//Server the PO view
					String view = request.getParameter("view") + ".xml";
					File file = new File(engine.getXmlFolderPath() + view);
					if (file.exists()) {
							OrderBean order = engine.convertFromXMLFileToObject(file, user);
							request.setAttribute("order", order);
					} else {
						 file = new File(engine.getXmlPOProcessedFolderPath() + view);
						 if (file.exists()) {
							 OrderBean order = engine.convertFromXMLFileToObject(file, user);
							 request.setAttribute("order", order);
						 } else {
							 request.setAttribute("error", "File not found");
						 }
						 
					}
					
				}
				else {
					request.setAttribute("fileNames", engine.getXMLLinks(user, engine.getXmlFolderPath()));
					request.setAttribute("processedfileNames", engine.getXMLLinks(user, engine.getXmlPOProcessedFolderPath()));
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		
			this.getServletContext().getRequestDispatcher("/Account.jspx").forward(request, response);
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}