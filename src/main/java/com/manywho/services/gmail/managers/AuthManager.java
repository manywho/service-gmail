package com.manywho.services.gmail.managers;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.manywho.services.gmail.services.AuthenticationService;
import com.manywho.services.gmail.services.AuthorizationService;
import com.manywho.sdk.entities.UserObject;
import com.manywho.sdk.entities.run.elements.type.ObjectCollection;
import com.manywho.sdk.entities.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.entities.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.entities.security.AuthenticatedWho;
import com.manywho.sdk.entities.security.AuthenticatedWhoResult;
import com.manywho.sdk.entities.security.AuthenticationCredentials;
import com.manywho.sdk.enums.AuthorizationType;
import com.manywho.sdk.services.PropertyCollectionParser;
import com.manywho.sdk.services.oauth.AbstractOauth2Provider;
import org.scribe.oauth.OAuthService;

import javax.inject.Inject;

public class AuthManager {
    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private PropertyCollectionParser propertyParser;

    @Inject
    private AbstractOauth2Provider gmailAppConfig;

    public AuthenticatedWhoResult authenticateUser(AuthenticationCredentials credentials) throws Exception {
        GoogleTokenResponse response =
                new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(), new JacksonFactory(),
                        gmailAppConfig.getClientId(), gmailAppConfig.getClientSecret(),
                        credentials.getCode(), gmailAppConfig.getRedirectUri())
                        .execute();

        System.out.println("Access token: " + response.getAccessToken());

        GoogleCredential credential = new GoogleCredential.Builder().build().setAccessToken( response.getAccessToken());
        Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                .setApplicationName(gmailAppConfig.getName()).build();

        Userinfoplus userInformation = oauth2.userinfo().get().execute();

        if (userInformation != null) {
            return authenticationService.buildAuthenticatedWhoResult(
                    gmailAppConfig.getName(),
                    userInformation.getEmail(),
                    userInformation.getEmail(),
                    gmailAppConfig.getClientId(),
                    userInformation.getId(),
                    response.getAccessToken()
            );
        }

        throw new Exception("Unable to authenticate with Box");
    }

    public ObjectDataResponse authorizeUser(OAuthService oauthService, AbstractOauth2Provider provider, AuthenticatedWho user, ObjectDataRequest objectDataRequest) {
        // Check if the logged-in user is authorized for this flow
        String authorizationStatus = authorizationService.getUserAuthorizationStatus(objectDataRequest.getAuthorization(), user);

        UserObject userObject = new UserObject(
                provider.getName(),
                AuthorizationType.Oauth2,
                oauthService.getAuthorizationUrl(null),
                authorizationStatus
        );

        return new ObjectDataResponse(userObject);
    }

    public ObjectDataResponse loadGroups(ObjectDataRequest objectDataRequest) throws Exception {
        return new ObjectDataResponse(new ObjectCollection());
    }

    public ObjectDataResponse loadGroupAttributes() {
        return new ObjectDataResponse(authorizationService.loadGroupAttributes());
    }

    public ObjectDataResponse loadUsers(ObjectDataRequest objectDataRequest) throws Exception {
        return new ObjectDataResponse(new ObjectCollection());
    }
}