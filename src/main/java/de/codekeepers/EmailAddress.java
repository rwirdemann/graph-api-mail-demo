package de.codekeepers;

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
