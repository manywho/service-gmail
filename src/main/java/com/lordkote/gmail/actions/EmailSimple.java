package com.lordkote.gmail.actions;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;

import java.time.OffsetDateTime;

/**
 * Created by Jose on 02/08/2016.
 */
@Action.Metadata(name = "Send Email Simple", summary = "Send Email Simple", uri = "email-simple")
public class EmailSimple {
    public static class Input {

        @Action.Input(name = "To", contentType = ContentType.String)
        private String to;

        @Action.Input(name = "Subject", contentType = ContentType.String)
        private String subject;

        @Action.Input(name = "Body", contentType = ContentType.String)
        private String body;

        public String getTo() {
            return to;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }
    }

    public static class Output {
    }
}
