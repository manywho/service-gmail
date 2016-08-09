package com.lordkote.gmail.managers;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.lordkote.gmail.config.GmailAppConfig;
import com.lordkote.gmail.services.AuthenticationService;
import com.lordkote.gmail.services.AuthorizationService;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.api.security.AuthenticatedWho;
import com.google.inject.Provider;
import com.manywho.sdk.api.security.AuthenticatedWhoResult;
import com.manywho.sdk.api.security.AuthenticationCredentials;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Created by Jose on 02/08/2016.
 */
public class IdentityManager {
    private final Provider<AuthenticatedWho> authenticatedWhoProvider;
    private final AuthorizationService authorizationService;
    private final AuthenticationService authenticationService;
    private final GmailAppConfig gmailAppConfig;

    @Inject
    public IdentityManager(Provider<AuthenticatedWho> authenticatedWhoProvider,
                           AuthorizationService authorizationService, AuthenticationService authenticationService,
                           GmailAppConfig gmailAppConfig) {

        this.authenticatedWhoProvider = authenticatedWhoProvider;
        this.authorizationService = authorizationService;
        this.authenticationService = authenticationService;
        this.gmailAppConfig = gmailAppConfig;
    }

    public ObjectDataResponse authorization(ObjectDataRequest objectDataRequest) {
        AuthenticatedWho user = authenticatedWhoProvider.get();

        List<MObject> userObject;

        switch (objectDataRequest.getAuthorization().getGlobalAuthenticationType()) {
            case AllUsers:
                userObject = authorizationService.withAllUsers(user);
                break;
            case Public:
                userObject = authorizationService.withPublic(user);
                break;
            case Specified:
                userObject = authorizationService.withSpecified(user, objectDataRequest);
                break;
            default:
                userObject = authorizationService.createUserObject(user, "401");
                break;
        }

        return new ObjectDataResponse(userObject);
    }

    public AuthenticatedWhoResult authentication(AuthenticationCredentials authenticationCredentials) {

        try {
            GoogleTokenResponse response =
                    new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(),
                            gmailAppConfig.getClientId(), gmailAppConfig.getClientSecret(),
                            authenticationCredentials.getCode(), gmailAppConfig.getRedirectUris().get(0))
                            .execute();

            System.out.println("Access token: " + response.getAccessToken());

            GoogleCredential credential = new GoogleCredential.Builder().build().setAccessToken( response.getAccessToken());
            Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                    .setApplicationName(gmailAppConfig.getApplicationName()).build();

            return authenticationService.authenticateUser(oauth2.userinfo().get().execute(), response.getAccessToken());

        } catch (TokenResponseException e) {
            if (e.getDetails() != null) {
                System.err.println("Error: " + e.getDetails().getError());
                if (e.getDetails().getErrorDescription() != null) {
                    System.err.println(e.getDetails().getErrorDescription());
                }
                if (e.getDetails().getErrorUri() != null) {
                    System.err.println(e.getDetails().getErrorUri());
                }
            } else {
                System.err.println(e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return AuthenticatedWhoResult.createDeniedResult();
    }

    public ObjectDataResponse groupAttributes(ObjectDataRequest objectDataRequest) {
        return new ObjectDataResponse(authorizationService.loadGroupAttributes());
    }

    public ObjectDataResponse groups(ObjectDataRequest objectDataRequest) {
        return new ObjectDataResponse(authorizationService.loadGroups());
    }

    public ObjectDataResponse users(ObjectDataRequest objectDataRequest) {
        return new ObjectDataResponse();
    }

    public ObjectDataResponse usersAttributes(ObjectDataRequest objectDataRequest) {
        return new ObjectDataResponse();
    }
}