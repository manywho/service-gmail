package com.lordkote.gmail.config;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jose on 06/08/2016.
 */
public class GmailAppConfig {

    private JSONObject jsonObject;

    public GmailAppConfig() {
        jsonObject = null;
    }

    public String getApplicationName() {
        return "Oauth2";
    }

    public String getClientId(){ return getJsonObjectWeb().getString("client_id");}

    public String getProjectId(){ return getJsonObjectWeb().getString("project_id");}

    public String getAuthUri(){ return getJsonObjectWeb().getString("auth_uri");}

    public String getTokenUri(){ return getJsonObjectWeb().getString("token_uri");}

    public String getAuthProviderX509CertUrl(){ return getJsonObjectWeb().getString("auth_provider_x509_cert_url");}

    public String getClientSecret(){ return getJsonObjectWeb().getString("client_secret");}

    public List<String> getRedirectUris() {
        List<String> redirectsUris = new ArrayList<>();
        for (Object item: getJsonObjectWeb().getJSONArray("redirect_uris")) {
            redirectsUris.add(String.valueOf(item));
        }

        return redirectsUris;
    }
    public List<String> getJavascriptOrigins() {
        List<String> origins = new ArrayList<>();

        for (Object item: getJsonObjectWeb().getJSONArray("javascript_origins")) {
            origins.add(String.valueOf(item));
        }

        return origins;
    }

    private JSONObject getJsonObjectWeb(){

        if (jsonObject!= null) {
            return jsonObject;
        }

        String credentialsString = System.getenv().get("GMAIL_APP_CREDENTIALS");

        if(credentialsString == null || credentialsString.isEmpty()) {
            URL url = Resources.getResource("credentials.properties");
            try {
                credentialsString = Resources.toString(url, Charsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("not possible to read GMail credentails");
            }
        }

        jsonObject = new JSONObject(credentialsString).getJSONObject("web");

        return jsonObject;
    }
}
