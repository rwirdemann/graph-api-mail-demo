package de.codekeepers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Folder {
    public String id;
    public String displayName;
}
