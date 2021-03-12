package de.codekeepers.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailAddress {
    public String name;
    public String address;

    @Override
    public String toString() {
        return "EMailAddress{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
