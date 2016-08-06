package com.lordkote.gmail.controllers;

import com.lordkote.gmail.managers.IdentityManager;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.api.security.AuthenticatedWhoResult;
import com.manywho.sdk.api.security.AuthenticationCredentials;
import com.manywho.sdk.services.controllers.AbstractIdentityController;
import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * Created by Jose on 02/08/2016.
 */
@Path("/")
public class IdentityController extends AbstractIdentityController {
    private final IdentityManager identityManager;

    @Inject
    public IdentityController(IdentityManager identityManager) {
        this.identityManager = identityManager;
    }

    @Override
    public AuthenticatedWhoResult authentication(AuthenticationCredentials authenticationCredentials) throws Exception {
        return identityManager.authentication(authenticationCredentials);
    }

    @Override
    public ObjectDataResponse authorization(ObjectDataRequest objectDataRequest) throws Exception {
        return identityManager.authorization(objectDataRequest);

    }

    @Override
    public ObjectDataResponse groups(ObjectDataRequest objectDataRequest) throws Exception {
        return identityManager.groups(objectDataRequest);
    }

    @Override
    public ObjectDataResponse groupAttributes(ObjectDataRequest objectDataRequest) throws Exception {
        return identityManager.groupAttributes(objectDataRequest);
    }

    @Override
    public ObjectDataResponse users(ObjectDataRequest objectDataRequest) throws Exception {
        return identityManager.users(objectDataRequest);
    }

    @Override
    public ObjectDataResponse userAttributes(ObjectDataRequest objectDataRequest) throws Exception {
        return identityManager.usersAttributes(objectDataRequest);
    }
}