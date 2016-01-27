package org.wso2.examples.servlets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.wso2.examples.filter.utils.AuthParam;
import org.wso2.examples.filter.utils.CustomData;
import org.wso2.examples.filter.utils.CustomDataReader;
import org.wso2.examples.filter.utils.UserAuthenticator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class JWTSecurityFilter implements Filter {

    private static final String JWT_TOKEN_SUBJECT = "sub";
    private static final String JWT_TOKEN_USER_ROLES = "http://wso2.org/claims/role";
    private static final Log log = LogFactory.getLog(JWTSecurityFilter.class);
    private CustomData customData;

    public void init(FilterConfig arg0) throws ServletException {
        CustomDataReader customDataReader = new CustomDataReader();
        customData = customDataReader.getCustomData();
    }

    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        PrintWriter out = resp.getWriter();
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        Base64 decoder = new Base64();
        String jwtHeader = request.getHeader("x-jwt-assertion");
        List<AuthParam> rolesAndNames = customData.getSecurityData().getAuthParams();

        //This is to handle null requests if request is not null it will continue,
        if (jwtHeader == null) {
            log.warn("Miss match in user name or user roles");
            chain.doFilter(req, resp);
            return;
        }

        String[] jwtArray = jwtHeader.split("\\.");
        if (jwtArray.length != 3) {
            //format of the JWT header is invalid so we send unautherize
            response.sendError(403);
            return;
        }
        byte[] decodedHeader = decoder.decode(jwtArray[0]);
        byte[] decodedPayload = decoder.decode(jwtArray[1]);
        byte[] decodedSignature = decoder.decode(jwtArray[2]);
        String headerString = new String(decodedHeader);
        String payloadString = new String(decodedPayload);
        String signatureString = new String(decodedSignature);
        JSONObject payLoad = null;

        try {
            payLoad = (JSONObject) new JSONParser().parse(payloadString);
        } catch (ParseException e) {
            log.error("Error while creating JASON object from payloadString", e);
            response.sendError(422, "Invalid JWT Header");
        }

        String userName = (String) payLoad.get(JWT_TOKEN_SUBJECT);
        String roles = (String) payLoad.get(JWT_TOKEN_USER_ROLES);
        List<String> rolesList = new ArrayList<String>(Arrays.asList(roles.split(",")));

        UserRoleRequestWrapper userRoleRequestWrapper = new UserRoleRequestWrapper(userName,
                                                                                   rolesList,
                                                                                   request);

        Iterator<AuthParam> iterator = rolesAndNames.iterator();
        String requestedUri = request.getRequestURI();

        UserAuthenticator userAuthenticator = new UserAuthenticator(rolesAndNames, rolesList,
                                                                    requestedUri);
        if (userAuthenticator.authenticateUser()) {
            chain.doFilter(userRoleRequestWrapper, resp);
        } else {
            response.sendError(403);
            return;
        }

    }

    public void destroy() {
    }
}