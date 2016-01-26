package org.wso2.examples.servlets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.sling.commons.json.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.wso2.examples.filter.utils.SecurityInfo;
import org.wso2.examples.filter.utils.SecurutyConstraintReader;
import sun.misc.BASE64Decoder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class JWTSecurityFilter implements Filter {
    public void init(FilterConfig arg0) throws ServletException {
    }

    private static final Log log   = LogFactory.getLog(JWTSecurityFilter.class);
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        PrintWriter out = resp.getWriter();
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        BASE64Decoder decoder = new BASE64Decoder();
        SecurutyConstraintReader securityContraintReader = new SecurutyConstraintReader();
        SecurityInfo securityInformations = securityContraintReader.getSecurityInformations();

        String jwtHeader = request.getHeader("x-jwt-assertion");


        if(jwtHeader == null) //This is to handle null requests if request is not null it will continue,
        {
            log.warn("Miss match in user name or user roles");
            chain.doFilter(req, resp);
            return;
        }

        String[] jwta = request.getHeader("x-jwt-assertion").split("\\.");
        byte[] decodedHeader = decoder.decodeBuffer(jwta[0]);
        byte[] decodedPayload = decoder.decodeBuffer(jwta[1]);
        byte[] decodedSignature = decoder.decodeBuffer(jwta[2]);
        String headerString = new String(decodedHeader);
        String payloadString = new String(decodedPayload);
        String signatureString = new String(decodedSignature);
        JSONObject payLoad = null;

        try {
            payLoad = (JSONObject) new JSONParser().parse(payloadString);
        } catch (ParseException e) {
            log.error("Error while creating JASON object from payloadString", e);
        }

        String userName = (String) payLoad.get("sub");
        String roles = (String) payLoad.get("http://wso2.org/claims/role");
        List<String> rolesList = new ArrayList<String>(Arrays.asList(roles.split(",")));

        UserRoleRequestWrapper userRoleRequestWrapper = new UserRoleRequestWrapper(userName, rolesList, request);

        if(userRoleRequestWrapper.isUserInRole(securityInformations.getRoleName())) {
            chain.doFilter(userRoleRequestWrapper, resp);
        }
        else
        {
            response.sendError(403);
        }


    }

    public void destroy() {
    }
}