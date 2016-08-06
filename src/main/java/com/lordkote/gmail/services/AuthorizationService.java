package com.lordkote.gmail.services;

import com.google.api.services.gmail.GmailScopes;
import com.google.common.collect.Lists;
import com.lordkote.gmail.config.GmailAppConfig;
import com.lordkote.gmail.repositories.UserRepository;
import com.manywho.sdk.api.AuthorizationType;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.security.AuthenticatedWho;
import com.manywho.sdk.services.types.TypeBuilder;
import com.manywho.sdk.services.types.system.$User;
import com.manywho.sdk.services.types.system.AuthorizationAttribute;
import com.manywho.sdk.services.types.system.AuthorizationGroup;
import com.manywho.sdk.api.run.elements.config.Group;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by Jose on 02/08/2016.
 */
public class AuthorizationService {
    private final TypeBuilder typeBuilder;
    private final UserRepository userRepository;
    private final GmailAppConfig gmailAppConfig;

    @Inject
    public AuthorizationService(TypeBuilder typeBuilder, UserRepository userRepository, GmailAppConfig gmailAppConfig) {
        this.typeBuilder = typeBuilder;
        this.userRepository = userRepository;
        this.gmailAppConfig = gmailAppConfig;
    }

    /**
     * Create an MObject based on the $User system type, using the details of the currently authorized user.
     *
     * @param user   the currently authorized user
     * @param status the status of the user (either 200 for authorized, or 401 for unauthorized)
     * @return a $User MObject containing the details of the currently authorized user
     */
    public List<MObject> createUserObject(AuthenticatedWho user, String status) {
        $User object = new $User();
        object.setAuthenticationType(AuthorizationType.Oauth2);
        object.setDirectoryId("Example");
        object.setDirectoryName("Example");
        object.setEmail(user.getEmail());
        object.setFirstName(user.getFirstName());
        object.setLastName(user.getLastName());
        object.setStatus(status);
        object.setUserId(user.getUserId());
        object.setUsername(user.getUsername());
        object.setLoginUrl(createLoginUrl());

        return typeBuilder.from(object);
    }

    private String createLoginUrl() {
        String url = "https://accounts.google.com/o/oauth2/v2/auth";
        String client_id = gmailAppConfig.getClientId();
        String response_type = "code";
        String scope = GmailScopes.GMAIL_SEND + " " + "email" + " " + "openid";

        return String.format("%s?client_id=%s&response_type=%s&scope=%s",
                url, client_id, response_type, scope);
    }

    /**
     * Check if the currently authorized user is the default public user. If no user is currently logged in, ManyWho
     * will send the public user as the currently authorized user, which is what we're checking for.
     *
     * @param user the currently authorized user
     * @return whether the currently authorized user is the default public user or not
     */
    public boolean isPublicUser(AuthenticatedWho user) {
        return user.getUserId().equals("PUBLIC_USER");
    }

    public List<MObject> loadGroupAttributes() {
        AuthorizationAttribute attributeOne = new AuthorizationAttribute();
        attributeOne.setLabel("Members");
        attributeOne.setValue("MEMBERS");

        AuthorizationAttribute attributeTwo = new AuthorizationAttribute();
        attributeTwo.setLabel("Owners");
        attributeTwo.setValue("OWNERS");

        return typeBuilder.from(Lists.newArrayList(attributeOne, attributeTwo));
    }

    public List<MObject> loadGroups() {
        AuthorizationGroup groupOne = new AuthorizationGroup();
        groupOne.setId("1");
        groupOne.setName("Group One");

        AuthorizationGroup groupTwo = new AuthorizationGroup();
        groupTwo.setId("2");
        groupTwo.setName("Group Two");

        return typeBuilder.from(Lists.newArrayList(groupOne, groupTwo));
    }

    public List<MObject> withAllUsers(AuthenticatedWho user) {
        if (isPublicUser(user)) {
            return createUserObject(user, "401");
        }

        return createUserObject(user, "200");
    }

    public List<MObject> withPublic(AuthenticatedWho user) {
        return createUserObject(user, "200");
    }

    public List<MObject> withSpecified(AuthenticatedWho user, ObjectDataRequest objectDataRequest) {
        if (isPublicUser(user)) {
            return createUserObject(user, "401");
        }

        // Check the logged-in user is a member of the given groups
        List<Group> groups = objectDataRequest.getAuthorization().getGroups();
        if (groups != null) {
            boolean exists = groups.stream()
                    .anyMatch(group -> userRepository.isUserInGroup(user.getUserId(), group.getAuthenticationId()));
        }

        return createUserObject(user, "200");
    }
}