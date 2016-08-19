package com.manywho.services.gmail.services;

import com.manywho.sdk.entities.security.AuthenticatedWhoResult;
import com.manywho.sdk.enums.AuthenticationStatus;

/**
 * Created by laura on 08/08/2016.
 */

public class AuthenticationService {

    public AuthenticatedWhoResult buildAuthenticatedWhoResult(String providerName, String email, String name, String clientId, String id, String accessToken) {
        AuthenticatedWhoResult authenticatedWhoResult = new AuthenticatedWhoResult();
        authenticatedWhoResult.setDirectoryId(providerName);
        authenticatedWhoResult.setDirectoryName(providerName);
        authenticatedWhoResult.setEmail(email);
        authenticatedWhoResult.setFirstName(name);
        authenticatedWhoResult.setIdentityProvider(providerName);
        authenticatedWhoResult.setLastName(name);
        authenticatedWhoResult.setStatus(AuthenticationStatus.Authenticated);
        authenticatedWhoResult.setTenantName(clientId);
        authenticatedWhoResult.setToken(accessToken);
        authenticatedWhoResult.setUserId(id);
        authenticatedWhoResult.setUsername(email);

        return authenticatedWhoResult;
    }
}