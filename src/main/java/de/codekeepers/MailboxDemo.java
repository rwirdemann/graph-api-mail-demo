package de.codekeepers;// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import de.codekeepers.Folder;
import de.codekeepers.Mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import static de.codekeepers.Mailbox.getFolders;
import static de.codekeepers.Mailbox.getMails;

class MailboxDemo {
    private static String authority;
    private static String clientId;
    private static String secret;
    private static String scope;

    private static final String FOLDER_ID_CRM = "AAMkADljYTg5ZDAxLWFhN2YtNDgxMi05MmU4LTQ1YjQ1ZGQ0NzdmNQAuAAAAAADRfUy7cPAQRr4pRB9mHHNzAQDYo6CezcgySaauGw-KNAd0AACIxhCMAAA=";
    public static final String USER_ID_RW = "591dfef2-9a07-48db-8ffe-4cc4e18af791";

    public static void main(String args[]) throws Exception {
        setUpSampleData();
        try {
            IAuthenticationResult result = getAccessTokenByClientCredentialGrant();
            List<Folder> folders = getFolders(USER_ID_RW, result.accessToken());
            for (Folder f : folders) {
                System.out.println(f.id + ": " + f.displayName);
            }
            List<Mail> mails = getMails("591dfef2-9a07-48db-8ffe-4cc4e18af791", FOLDER_ID_CRM, result.accessToken());
            for (Mail m : mails) {
                System.out.println(m.subject);
                System.out.println(m.body.content);
            }

        } catch (Exception ex) {
            System.out.println("Oops! We have an exception of type - " + ex.getClass());
            System.out.println("Exception message - " + ex.getMessage());
            throw ex;
        }
    }

    private static IAuthenticationResult getAccessTokenByClientCredentialGrant() throws Exception {

        ConfidentialClientApplication app = ConfidentialClientApplication.builder(
                clientId,
                ClientCredentialFactory.createFromSecret(secret))
                .authority(authority)
                .build();

        // With client credentials flows the scope is ALWAYS of the shape "resource/.default", as the
        // application permissions need to be set statically (in the portal), and then granted by a tenant administrator
        ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
                Collections.singleton(scope))
                .build();

        CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
        return future.get();
    }

    private static String getUsersListFromGraph(String accessToken) throws IOException {
        URL url = new URL("https://graph.microsoft.com/v1.0/users");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json");

        int httpResponseCode = conn.getResponseCode();
        if (httpResponseCode == HTTPResponse.SC_OK) {

            StringBuilder response;
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {

                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            return response.toString();
        } else {
            return String.format("Connection returned HTTP code: %s with message: %s",
                    httpResponseCode, conn.getResponseMessage());
        }
    }

    private static String getMailsFromGraph(String accessToken) throws IOException {
        URL url = new URL("https://graph.microsoft.com/v1.0/users/591dfef2-9a07-48db-8ffe-4cc4e18af791/mailFolders");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json");

        int httpResponseCode = conn.getResponseCode();
        if (httpResponseCode == HTTPResponse.SC_OK) {

            StringBuilder response;
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {

                String inputLine;
                response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
            }
            return response.toString();
        } else {
            return String.format("Connection returned HTTP code: %s with message: %s",
                    httpResponseCode, conn.getResponseMessage());
        }
    }

    /**
     * Helper function unique to this sample setting. In a real application these wouldn't be so hardcoded, for example
     * different users may need different authority endpoints or scopes
     */
    private static void setUpSampleData() throws IOException {
        // Load properties file and set properties used throughout the sample
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
        authority = properties.getProperty("AUTHORITY");
        clientId = properties.getProperty("CLIENT_ID");
        secret = properties.getProperty("SECRET");
        scope = properties.getProperty("SCOPE");
    }
}
