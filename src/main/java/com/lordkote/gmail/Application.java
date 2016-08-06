package com.lordkote.gmail;

import com.manywho.sdk.services.ServiceApplication;
import io.undertow.Undertow;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;

import javax.ws.rs.ApplicationPath;

/**
 * Created by Jose on 02/08/2016.
 */
@ApplicationPath("/")
public class Application extends ServiceApplication {

    public Application() {
        super();
        this.setModule(new ApplicationModule());

        this.initialize();
    }

    public static void main(String[] args) {
        UndertowJaxrsServer server = new UndertowJaxrsServer();
        Undertow.Builder serverBuilder = Undertow.builder().addHttpListener(8080, "0.0.0.0");
        server.start(serverBuilder);
        server.deploy(new Application(), "/api/gmail/1");
    }
}
