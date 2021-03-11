package de.codekeepers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact {
    public EmailAddress emailAddress;

    @Override
    public String toString() {
        return "Contact{" +
                "eMailAddress=" + emailAddress +
                '}';
    }
}
