package org.wso2.examples.ErrorPages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by visitha on 1/25/16.
 */
public class ErrorHandler extends HttpServlet {
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html>" + "<head><title>403 Error</title></head>");
        out.print("<h1>403 Error - Resource Forbidden</h1><br><h3>"+request.getUserPrincipal().getName()+" is not an authorized user for this page ");


    }
}