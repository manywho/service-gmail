package com.lordkote.gmail.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.lordkote.gmail.actions.SendEmailSimple;
import com.lordkote.gmail.services.EmailService;
import com.manywho.sdk.entities.run.elements.config.ServiceRequest;
import com.manywho.sdk.entities.run.elements.config.ServiceResponse;
import com.manywho.sdk.enums.InvokeType;
import com.manywho.sdk.services.controllers.AbstractController;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/actions")
@Consumes("application/json")
@Produces("application/json")
public class SendEmailController extends AbstractController {

    @Inject
    private EmailService emailService;

    @Path("/email-simple")
    @POST
    public ServiceResponse sendEmailSimple(ServiceRequest serviceRequest) throws Exception {
        SendEmailSimple mailParameters = this.parseInputs(serviceRequest,  SendEmailSimple.class);

        GoogleCredential credential = new GoogleCredential.Builder().build()
                .setAccessToken(getAuthenticatedWho().getToken());

        Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                .setApplicationName("Oauth2").build();

        Userinfoplus userinfoplus = oauth2.userinfo().get().execute();

        emailService.sendEmail(mailParameters.getTo(), userinfoplus.getEmail(), mailParameters.getSubject(),
                mailParameters.getBody(), userinfoplus.getId(), credential);

        return new ServiceResponse(InvokeType.Forward, serviceRequest.getToken());
    }

}
