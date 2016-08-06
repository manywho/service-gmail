package com.lordkote.gmail.services;

import com.google.api.services.oauth2.model.Userinfoplus;
import com.manywho.sdk.api.security.AuthenticatedWhoResult;

import java.util.UUID;

/**
 * Created by Jose on 02/08/2016.
 */
public class AuthenticationService {
    public AuthenticatedWhoResult authenticateUser(Userinfoplus userinfo, String token) {
        AuthenticatedWhoResult result = new AuthenticatedWhoResult();
        result.setDirectoryId(userinfo.getId());
        result.setDirectoryName(userinfo.getId());
        result.setEmail(userinfo.getEmail());
        result.setFirstName("example");
        result.setIdentityProvider("GMAIL");
        result.setLastName("example");
        result.setStatus(AuthenticatedWhoResult.AuthenticationStatus.Authenticated);
        result.setTenantName(userinfo.getEmail());
        result.setToken(token);
        result.setUserId(userinfo.getId());
        result.setUsername(userinfo.getEmail());

        return result;
    }
}