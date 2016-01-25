package org.wso2.examples.filter.utils;

/**
 * Created by visitha on 1/22/16.
 */

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SecurityInfo {
    private int id;
    private String roleName;
    private String webResourceName;
    private String urlPattern;

    public SecurityInfo() {}
    public SecurityInfo(int id, String roleName, String webResourceName, String urlPattern) {
        super();
        this.id = id;
        this.roleName = roleName;
        this.webResourceName = webResourceName;
        this.urlPattern = urlPattern;
    }
    @XmlAttribute
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @XmlElement
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    @XmlElement
    public String getWebResourceName() { return webResourceName; }
    public void setWebResourceName (String webResourceName){ this.webResourceName = webResourceName;}
    @XmlElement
    public String getUrlPattern(){ return urlPattern; }
    public void setUrlPattern(String urlPattern){ this.urlPattern = urlPattern; }



}
