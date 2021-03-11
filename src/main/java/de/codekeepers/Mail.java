package de.codekeepers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mail {
    public String subject;
    public Body body;
}
