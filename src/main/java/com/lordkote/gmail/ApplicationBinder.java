package com.lordkote.gmail;

import com.lordkote.gmail.config.GmailAppConfig;
import com.lordkote.gmail.managers.AuthManager;
import com.lordkote.gmail.services.AuthenticationService;
import com.lordkote.gmail.services.AuthorizationService;
import com.lordkote.gmail.services.EmailService;
import com.manywho.sdk.services.oauth.AbstractOauth2Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(AuthManager.class).to(AuthManager.class);
        bind(AuthenticationService.class).to(AuthenticationService.class);
        bind(AuthorizationService.class).to(AuthorizationService.class);
        bind(EmailService.class).to(EmailService.class);
        bind(GmailAppConfig.class).to(AbstractOauth2Provider.class);
    }
}
