package com.manywho.services.gmail;

import com.manywho.sdk.services.BaseApplication;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class Application extends BaseApplication{
    public Application(){
        registerSdk()
                .packages("com.manywho.services.gmail")
                .register(new ApplicationBinder());
    }

    public static void main(String[] args) {
        startServer(new Application(), "api/gmail/1");
    }
}