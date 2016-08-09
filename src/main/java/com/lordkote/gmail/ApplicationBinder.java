package com.lordkote.gmail;

import com.lordkote.gmail.config.GmailAppConfig;
import com.lordkote.gmail.managers.AuthManager;
import com.lordkote.gmail.services.AuthorizationService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(AuthorizationService.class);
        bind(AuthManager.class);
        bind(GmailAppConfig.class);
    }
}
