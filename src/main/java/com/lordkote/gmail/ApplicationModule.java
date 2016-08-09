package com.lordkote.gmail;

import com.google.inject.AbstractModule;
import com.lordkote.gmail.managers.IdentityManager;
import com.lordkote.gmail.services.AuthorizationService;

/**
 * Created by Jose on 02/08/2016.
 */
public class ApplicationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IdentityManager.class);
        bind(AuthorizationService.class);
    }
}
