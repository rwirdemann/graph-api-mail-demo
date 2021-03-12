package de.codekeepers;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import de.codekeepers.domain.Folder;
import de.codekeepers.domain.Mail;

import java.io.IOException;
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
    private static String folderId;
    private static String userId;

    public static void main(String[] args) throws Exception {
        setUpSampleData();
        try {
            IAuthenticationResult result = getAccessTokenByClientCredentialGrant();
            List<Folder> folders = getFolders(userId, result.accessToken());
            for (Folder f : folders) {
                System.out.println(f);
            }
            List<Mail> mails = getMails(userId, folderId, result.accessToken());
            for (Mail m : mails) {
                System.out.println(m);
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

    private static void setUpSampleData() throws IOException {
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
        authority = properties.getProperty("AUTHORITY");
        clientId = properties.getProperty("CLIENT_ID");
        secret = properties.getProperty("SECRET");
        scope = properties.getProperty("SCOPE");
        folderId = properties.getProperty("FOLDER_ID");
        userId = properties.getProperty("USER_ID");
    }
}
