/*
 * Copyright 2007 Sun Microsystems, Inc.
 * All rights reserved.  You may not modify, use,
 * reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://developer.sun.com/berkeley_license.html
 */


package org.wso2.examples.servlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.annotation.security.DeclareRoles;

import org.wso2.examples.filter.utils.*;


/**
 * This is a simple example of an HTTP Servlet that can only be accessed
 * by an authenticated user.  It responds to the GET
 * method of the HTTP protocol.
 */
@DeclareRoles("admin")
public class GreetingServlet extends HttpServlet {
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        // then write the data of the response
        out.println("<html>" + "<head><title>Hello</title></head>");

        // then write the data of the response
        out.println(
                "<body  bgcolor=\"#ffffff\">"
                        + "<img src=\"duke.waving.gif\" alt=\"Duke waving\">"
                        + "<h2>Hello, my name is Duke. What's yoursswdsdsdsdswd?</h2>"
                        + "<form method=\"get\">"
                        + "<input type=\"text\" name=\"username\" size=\"25\">"
                        + "<p></p>" + "<input type=\"submit\" value=\"Submit\">"
                        + "<input type=\"reset\" value=\"Reset\">" + "</form>");

//
//      FilterInformationReader reader =  new FilterInformationReader();
//        JWTSecurityFilterInfo info = reader.read();
////
//
////        if(info == null)
////        {
////            out.print("info is null");
////        }
//
//       String role = info.getConstraint();


        SecurutyConstraintReader x= new SecurutyConstraintReader();
        SecurityInfo q = x.getSecurityInformations();
        String role = q.getRoleName();
        String res = q.getWebResourceName();
        String pattern = q.getUrlPattern();
//        List<Typer> li = q.getType();
        //String type = q.getType().getType();


        String username = request.getParameter("username");
        Enumeration<String> names = request.getHeaderNames();
        while(names.hasMoreElements()) {
            out.println(names.nextElement());

        }

        out.print("<h2>");
        out.print("User name :  " + request.getUserPrincipal().getName() + "<br>");
        out.print("If there is a role admin print true else false : ----"+role+"-----"+res+"------"+pattern+"-----");
        if(request.isUserInRole("admin"))
        {
            out.print("true");
        }
        else
        {
            out.print("false");
        }
        out.print("</h2>");

        if ((username != null) && (username.length() > 0)) {
            RequestDispatcher dispatcher = getServletContext()
                    .getRequestDispatcher(
                            "/response");

            if (dispatcher != null) {
                dispatcher.include(request, response);
            }
        }

        out.println("</body></html>");
        out.close();
    }

    public String getServletInfo() {
        return "The Hello servlet says hello.";
    }
}
