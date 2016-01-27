package org.wso2.examples.filter.utils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by visitha on 1/27/16.
 */
public class UserAuthenticator {

    private List<AuthParam> authParamList;
    private List<String> rolesList;
    private String uri;

    public UserAuthenticator(List<AuthParam> authParamList, List<String> rolesLis, String uri) {
        this.authParamList = authParamList;
        this.rolesList = rolesLis;
        this.uri = uri;
    }

    public boolean authenticateUser() {
        Iterator<AuthParam> iterator = authParamList.iterator();

        while (iterator.hasNext()) {
            AuthParam authParams = iterator.next();
            String urlPattern = authParams.getUrlPattern();
            List<RoleName> roleName = authParams.getRoleName();
            Iterator<RoleName> roleNameIterator =roleName.iterator();

            if (uri.contains(urlPattern)) {
                while(roleNameIterator.hasNext())
                {
                    String authenticatedRole = roleNameIterator.next().getRoleName();
                    if(rolesList.contains(authenticatedRole)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
