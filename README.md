# Microsoft Graph API Demo App

Die Anwendung nutzt die Microsoft Authentication Library für Java (MSAL4J)

## Setup
1. Anwendung unter [Azure portal](https://portal.azure.com) registrieren
1. Client Secret anlegen
1. application.properties mit den Daten aus der registrierten App anpassen

```
# src/main/resources/application.properties
AUTHORITY=https://login.microsoftonline.com/<TENANT_ID>/
CLIENT_ID=<CLIENT_ID>
SECRET=<CLIENT_SECRET>
SCOPE=https://graph.microsoft.com/.default
FOLDER_ID=
USER_ID=
```

## Build and Run

```
mvn clean install
java -jar target/graph-api-mail-demo-1.0.0.jar
```

Oder MailboxDemo in der IDE starten.

## Beispieldaten

In der Datei [mail.json](docs/mail.json) findet sich ein Beispiel für das von Exchange gelieferte Mail-Format.