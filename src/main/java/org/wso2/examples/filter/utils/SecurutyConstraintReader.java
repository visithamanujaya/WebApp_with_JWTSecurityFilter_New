package org.wso2.examples.filter.utils;

/**
 * Created by visitha on 1/22/16.
 */

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class SecurutyConstraintReader {

    public SecurityInfo getSecurityInformations() {

        ClassLoader classLoader = getClass().getClassLoader();
        String filePath = "securityinfo.xml";
        File file = new File(classLoader.getResource(filePath).getFile());

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SecurityInfo.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SecurityInfo que = (SecurityInfo) jaxbUnmarshaller.unmarshal(file);
            return que;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}