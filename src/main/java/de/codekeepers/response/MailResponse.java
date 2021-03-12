package de.codekeepers.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.codekeepers.domain.Mail;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MailResponse {
    public List<Mail> value;
}
