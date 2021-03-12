package de.codekeepers.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mail {
    public String subject;
    public Body body;
    public Contact sender;
    public Contact from;
    public Contact[] toRecipients;
    public Contact[] ccRecipients;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-d'T'hh:mm:ss'Z'")
    public Date sentDateTime;

    @Override
    public String toString() {
        return "Mail{" +
                "subject='" + subject + '\'' +
                ", body=" + body +
                ", sender=" + sender +
                ", from=" + from +
                ", toRecipients=" + Arrays.toString(toRecipients) +
                ", ccRecipients=" + Arrays.toString(ccRecipients) +
                ", sentDateTime=" + sentDateTime +
                '}';
    }
}
