package com.manywho.services.gmail.services;

import com.manywho.sdk.entities.run.elements.config.Authorization;
import com.manywho.sdk.entities.run.elements.type.Object;
import com.manywho.sdk.entities.run.elements.type.ObjectCollection;
import com.manywho.sdk.entities.run.elements.type.Property;
import com.manywho.sdk.entities.run.elements.type.PropertyCollection;
import com.manywho.sdk.entities.security.AuthenticatedWho;

/**
 * Created by Jose on 02/08/2016.
 */
public class AuthorizationService {

    public String getUserAuthorizationStatus(Authorization authorization, AuthenticatedWho user) {
        switch (authorization.getGlobalAuthenticationType()) {
            case Public:
                return "200";
            case AllUsers:
                if (!user.getUserId().equalsIgnoreCase("PUBLIC_USER")) {
                    return "200";
                } else {
                    return "401";
                }
            case Specified:
                if (!user.getUserId().equalsIgnoreCase("PUBLIC_USER")) {
                    return "401";
                }
            default:
                return "401";
        }
    }

    public ObjectCollection loadGroups(String enterpriseId) {
        return new ObjectCollection();
    }

    public Object loadGroupAttributes() {
        PropertyCollection properties = new PropertyCollection();
        properties.add(new Property("Label", "Users"));
        properties.add(new Property("Value", "users"));

        Object object = new Object();
        object.setDeveloperName("AuthenticationAttribute");
        object.setExternalId("users");
        object.setProperties(properties);

        return object;
    }
}