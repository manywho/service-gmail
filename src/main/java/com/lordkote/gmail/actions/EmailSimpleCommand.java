package com.lordkote.gmail.actions;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.lordkote.gmail.ServiceConfiguration;
import com.lordkote.gmail.services.EmailService;
import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.api.security.AuthenticatedWho;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import javax.mail.MessagingException;
import java.io.IOException;

/**
 * Created by Jose on 02/08/2016.
 */
public class EmailSimpleCommand implements ActionCommand<EmailSimple, EmailSimple.Input, EmailSimple.Output, ServiceConfiguration> {

    private final Provider<AuthenticatedWho> authenticatedWhoProvider;
    private EmailService emailService;

    @Inject
    public  EmailSimpleCommand(Provider<AuthenticatedWho> authenticatedWhoProvider, EmailService emailService) {
        this.authenticatedWhoProvider = authenticatedWhoProvider;
        this.emailService = emailService;
    }

    @Override
    public ActionResponse<EmailSimple.Output> execute(ServiceConfiguration configuration, ServiceRequest request, EmailSimple.Input input) {
        try {
            GoogleCredential credential = new GoogleCredential.Builder().build()
                    .setAccessToken(authenticatedWhoProvider.get().getToken());

            Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                    .setApplicationName("Oauth2").build();

            Userinfoplus userinfoplus = oauth2.userinfo().get().execute();

            try {
                emailService.sendEmail(input.getTo(), userinfoplus.getEmail(), input.getSubject(), input.getBody(),
                        userinfoplus.getId(), credential);

            } catch (MessagingException e) {
                throw new RuntimeException("Error sending email");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ActionResponse<>(new EmailSimple.Output());
    }
}
