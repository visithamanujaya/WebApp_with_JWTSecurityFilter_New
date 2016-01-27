package org.wso2.examples.filter.utils;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class UserAuthenticatorTest extends TestCase {

    public void testAuthenticateUser() throws Exception {

        RoleName roleName1 = new RoleName();
        RoleName roleName2 = new RoleName();
        List<RoleName> roleNames = new ArrayList<RoleName>();

        roleName1.setRoleName("admin");
        roleName2.setRoleName("visitha");

        roleNames.add(roleName1);
        roleNames.add(roleName2);

        AuthParam authParam = new AuthParam();
        List<AuthParam> authParamList = new ArrayList<AuthParam>();
        authParam.setRoleName(roleNames);
        authParam.setUrlPattern("/greeting");
        authParamList.add(authParam);

        List<String> authRoles = new ArrayList<String>();
        authRoles.add("adminn");
        authRoles.add("visitha");
        authRoles.add("Minura");
        authRoles.add("Ravindu");
        authRoles.add("Chandupa");
        authRoles.add("WIjesinghe");
        authRoles.add("Manel");



        String uri = "first/greeting";
        UserAuthenticator userAuthenticator = new UserAuthenticator(authParamList, authRoles, uri);
        userAuthenticator.authenticateUser();
        assertEquals(true,userAuthenticator.authenticateUser() );
    }
}