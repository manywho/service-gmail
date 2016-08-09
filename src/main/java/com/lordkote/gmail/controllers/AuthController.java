package com.lordkote.gmail.controllers;

import com.lordkote.gmail.config.GmailAppConfig;
import com.lordkote.gmail.managers.AuthManager;
import com.manywho.sdk.entities.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.entities.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.entities.security.AuthenticatedWhoResult;
import com.manywho.sdk.entities.security.AuthenticationCredentials;
import com.manywho.sdk.services.annotations.AuthorizationRequired;
import com.manywho.sdk.services.controllers.AbstractOauth2Controller;
import com.manywho.sdk.services.oauth.AbstractOauth2Provider;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
@Consumes("application/json")
@Produces("application/json")
public class AuthController extends AbstractOauth2Controller {
    @
    private AuthManager authManager;

    @Inject
    public AuthController(GmailAppConfig oauth2Provider) {
        super(oauth2Provider);
    }

    @Override
    public AuthenticatedWhoResult authentication(AuthenticationCredentials authenticationCredentials) throws Exception {
        return authManager.authenticateUser(oauth2Provider, authenticationCredentials);
    }

    @Path("/authorization")
    @POST
    @AuthorizationRequired
    public ObjectDataResponse authorization(ObjectDataRequest objectDataRequest) throws Exception {
        return authManager.authorizeUser(getOauthService(), oauth2Provider, getAuthenticatedWho(), objectDataRequest);
    }

    @Override
    public ObjectDataResponse groups(ObjectDataRequest objectDataRequest) throws Exception {
        return authManager.loadGroups(objectDataRequest);
    }

    @Override
    public ObjectDataResponse groupAttributes(ObjectDataRequest objectDataRequest) throws Exception {
        return authManager.loadGroupAttributes();
    }

    @Override
    public ObjectDataResponse users(ObjectDataRequest objectDataRequest) throws Exception {
        return authManager.loadUsers(objectDataRequest);
    }

    @Override
    public ObjectDataResponse userAttributes(ObjectDataRequest objectDataRequest) throws Exception {
        return super.userAttributes(objectDataRequest);
    }
}