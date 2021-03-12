package de.codekeepers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import de.codekeepers.domain.Folder;
import de.codekeepers.domain.Mail;
import de.codekeepers.response.FolderResponse;
import de.codekeepers.response.MailResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class Mailbox {
    private static String BASE_URL = "https://graph.microsoft.com/v1.0";

    public static List<Folder> getFolders(String userId, String accessToken) throws IOException {
        HttpURLConnection conn = getConnection(accessToken, new URL(String.format("%s/users/%s/mailFolders", BASE_URL, userId)));
        if (conn.getResponseCode() == HTTPResponse.SC_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String json = readJsonResponse(in);
                ObjectMapper objectMapper = new ObjectMapper();
                FolderResponse response = objectMapper.readValue(json, FolderResponse.class);
                return response.value;
            }
        }
        System.out.printf("Connection returned HTTP code: %s with message: %s%n", conn.getResponseCode(), conn.getResponseMessage());
        return Collections.emptyList();
    }

    public static List<Mail> getMails(String userId, String folderId, String accessToken) throws IOException {
        HttpURLConnection conn = getConnection(accessToken, new URL(String.format("%s/users/%s/mailFolders/%s/messages", BASE_URL, userId, folderId)));
        if (conn.getResponseCode() == HTTPResponse.SC_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String json = readJsonResponse(in);
                ObjectMapper objectMapper = new ObjectMapper();
                MailResponse response = objectMapper.readValue(json, MailResponse.class);
                return response.value;
            }
        }
        System.out.printf("Connection returned HTTP code: %s with message: %s%n", conn.getResponseCode(), conn.getResponseMessage());
        return Collections.emptyList();
    }

    private static HttpURLConnection getConnection(String accessToken, URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json");
        return conn;
    }

    private static String readJsonResponse(BufferedReader in) throws IOException {
        String inputLine;
        StringBuilder json = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            json.append(inputLine);
        }
        return json.toString();
    }
}
