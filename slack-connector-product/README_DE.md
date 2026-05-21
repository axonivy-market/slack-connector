# Slack Connector

Der Slack Connector verbindet Axon Ivy mit Slack und ermöglicht dir, aus Prozessen heraus Nachrichten zu senden, Slash-Commands zu verarbeiten und Workflows direkt aus Slack auszulösen. Er stellt einen `CALLABLE_SUB` zum Versenden von Nachrichten, eine kleine Java-Hilfs-API (`MessageService`) und Beispiel-Workflows zur schnellen Evaluierung bereit.

[![CI Build](https://github.com/axonivy-market/slack-connector/actions/workflows/ci.yml/badge.svg)](https://github.com/axonivy-market/slack-connector/actions/workflows/ci.yml)

### Wichtigste Funktionen

- Sende Nachrichten an Slack-Kanäle und Threads direkt aus Axon Ivy-Prozessen, um automatisierte Benachrichtigungen und Alerts zu realisieren.
- Poste reichhaltige Block-/Attachment-Nachrichten, um interaktive Benachrichtigungen und strukturierte Updates darzustellen.
- Einfache Java-Hilfs-API (`MessageService`), um Nachrichten programmatisch aus Java-Code und Prozessskripten zu senden.
- Verarbeite Slack-Slash-Commands, um interaktive Workflows (z. B. Incident-Erstellung) direkt aus Slack zu starten.
- Demo-Implementierungen zum Erstellen von Incidents und Öffnen von Genehmigungsaufgaben, damit du schnell testen kannst.
- Einfache Konfiguration über Produktvariablen und fertige Demo-Installer für eine schnelle Inbetriebnahme.

## Demo

Das Demo-Modul zeigt, wie Slack-Commands, Axon Ivy-Fälle und Slack-Bot-Antworten in einem Incident-Ablauf zusammenspielen. Nutze es, um die End-to-End-Erfahrung zu prüfen, bevor du den Connector in deine eigenen Prozesse integrierst.

### Demo-Workflows

#### slack-connector-demo (slack-connector-demo)

##### Incident aus Slack erstellen

1. Öffne einen Slack-Kanal, in dem deine App installiert ist, und führe `/ivy-create-incident` mit einer Severity wie `Low`, `Medium`, `High` oder `Critical` aus.

![Slash-Command zum Erstellen eines Incidents in Slack](images/demo-slash-commands.png)

2. Slack sendet das Formular an den Demo-REST-Endpunkt und Axon Ivy startet den Fall `CreateIncident` für den aktuellen Benutzer.

![Slack-Meldung für den erstellten Incident](images/demo-incident-created-message.png)

3. Öffne die Freigabeaufgabe aus der Slack-Nachricht, prüfe die Incident-Details und wähle im Dialog die verantwortliche Rolle.

![Dialog für die Incident-Freigabeaufgabe](images/demo-incident-approval-task.png)

4. Bestätige oder lehne die Anfrage ab. Das Demo postet die Entscheidung mit der gewählten Rolle zurück in den ursprünglichen Slack-Kanal.

![Slack-Meldung nach freigegebenem Incident](images/demo-incident-approved-message.png)

5. Führe `/ivy-summary-incident` aus, um eine schnelle Severity-Zusammenfassung der aktuell laufenden Incidents in Slack zu erhalten.

![Slack-Meldung mit Incident-Zusammenfassung](images/demo-incident-summary.png)

##### Task Listener starten

1. Starte den Installer-Einstieg `Task listener` aus dem Demo-Modul.
2. Lasse den Listener laufen, während du in Axon Ivy Aufgaben erstellst oder zuweist.
3. Prüfe die Slack-Benachrichtigungen, die gesendet werden, sobald neue Freigabeaufgaben verfügbar sind.

## Setup

- **Roles:** Everybody (konfiguriert in `config/roles.xml`)
- **OpenAPI:** Slack-Web-API-Spezifikation unter https://github.com/slackapi/slack-api-specs/blob/master/web-api/slack_web_openapi_v2.json mit Namespace `com.slack.api.client`

### Variablen

```yaml
# yaml-language-server: $schema=https://json-schema.axonivy.com/app/12.0.0/variables.json
Variables:
  com:
    axonivy:
      connector:
        slack:
          # the base url for Slack api
          baseUrl: https://slack.com/api
          notification:
            # enables the Slack notification for new tasks
            enabled: "true"
          # the token from Slack bot
          botToken: ""
```

1. Installiere die Connector-Artefakte in deiner Axon Ivy-Umgebung.
   1.1. Importiere `slack-connector` für die Kernintegration.
   1.2. Importiere zusätzlich `slack-connector-demo`, wenn du die Beispiel-Slash-Commands, den Dialog und den Task Listener nutzen willst.

2. Erstelle die Slack-App, die deinen Axon-Ivy-Bot repräsentiert.
   2.1. Öffne https://api.slack.com/apps und klicke auf **Create New App**.
   2.2. Wähle **From scratch**, vergebe einen Namen wie `Axon Ivy Bot` und wähle den Ziel-Workspace aus.

![Neue Slack-App von Grund auf erstellen](images/setup-create-new-slack-app.png)

3. Füge die Bot-Scopes hinzu, die der Connector benötigt.
   3.1. Öffne **OAuth & Permissions** in deiner Slack-App.
   3.2. Füge `chat:write` hinzu, damit der Bot Incident-Updates posten kann.
   3.3. Füge `commands` hinzu, damit Slack die Slash-Commands ausführen kann.
   3.4. Ergänze `chat:write.public`, wenn der Bot auch in öffentliche Kanäle posten soll, bevor er eingeladen wurde.

![Erforderliche Slack-Bot-Token-Scopes](images/setup-add-bot-token-scope.png)

4. Installiere die App im Workspace und kopiere das Bot-Token.
   4.1. Öffne **Install App** und autorisiere die App für deinen Workspace.
   4.2. Kopiere das **Bot User OAuth Token**, das nach der Installation angezeigt wird.
   4.3. Speichere dieses Token in der Axon Ivy-Variable `com.axonivy.connector.slack.botToken`.
   4.4. Halte den Wert aus der Versionsverwaltung heraus und ersetze lokale Test-Tokens, bevor du das Projekt teilst.

![Slack-App installieren und Bot-Token kopieren](images/setup-install-slack-app.png)

5. Erstelle die Slash-Commands, die das Demo verwendet.
   5.1. Öffne **Features** -> **Slash Commands** und klicke auf **Create New Command**.
   5.2. Erstelle `/ivy-create-incident` und setze die Request URL auf deine öffentliche Axon Ivy-Anwendungsbasis-URL plus `/api/incident/create`.
   5.3. Verwende eine kurze Beschreibung wie `run create Incident process with severity`.
   5.4. Verwende einen Usage Hint wie `Low, Medium, High, Critical`.
   5.5. Erstelle `/ivy-summary-incident` als zweiten Command und verweise ihn auf `/api/incident/summary` unter derselben öffentlichen Basis-URL.
   5.6. Installiere die App erneut, wenn Slack nach dem Speichern der Commands eine Aktualisierung der Berechtigungen verlangt.

![Slack-Slash-Command erstellen](images/setup-create-slack-slash-command.png)

6. Richte Slack auf die korrekte öffentliche Axon Ivy-URL aus.
   6.1. Die REST-Ressource im Demo ist mit `@Path("/incident")` implementiert und stellt die POST-Endpunkte `/create` und `/summary` bereit.
   6.2. Eine typische öffentliche Command-URL sieht daher wie `https://<your-ivy-app-base-url>/api/incident/create` aus.
   6.3. Wenn deine Axon Ivy-Umgebung unter einem zusätzlichen Pfadsegment wie `/designer` veröffentlicht ist, behältst du dieses Segment bei und hängst `/api/incident/create` oder `/api/incident/summary` an.

7. Prüfe die Integration.
   7.1. Führe `/ivy-create-incident Low` in Slack aus und prüfe, dass Slack `Process CreateIncident has been started by user: ...` zurückmeldet.
   7.2. Öffne die Freigabeaufgabe, wähle eine verantwortliche Rolle und bestätige oder lehne den Incident ab.
   7.3. Führe `/ivy-summary-incident` aus und kontrolliere, dass der Kanal die aktuellen Severity-Zahlen erhält.

## Components

### Connector processes

#### SendBotMessage.p.json

- **sendBotMessage(String message, String channel, String botToken) -> out: com.axonivy.connector.slack.SendBotMessageData**
  - Input:
    - `message` (String)
    - `channel` (String)
    - `botToken` (String)

### Form Components

#### IncidentDetailDialog — Incident-Anfragen aus Slack prüfen und bearbeiten

- **Namespace:** `com.axonivy.connector.slack.IncidentDetailDialog`
- **Component type:** HTML_DIALOG
- **Fields:**
  - `channel` — Slack-Kennung des Kanals, in den die Antwort zurückgesendet wird
  - `severity` — Severity des Incidents, die aus dem Slash-Command übernommen wird
  - `responsible` — Gewählte Rolle, die bei Bestätigung oder Ablehnung in den Workflow zurückgegeben wird
- **Where used:** `CreateIncident` Demo-Workflow
- **Purpose:** Zeigt das Incident-Freigabeformular an, lässt den Benutzer die verantwortliche Rolle wählen und sendet die Entscheidung zurück nach Slack.

### Maven artifacts

1. com.axonivy.connector.slack.connector:slack-connector (@version@)

```xml
<dependency>
  <groupId>com.axonivy.connector.slack.connector</groupId>
  <artifactId>slack-connector</artifactId>
  <version>@version@</version>
  <type>iar</type>
</dependency>
```

2. com.axonivy.connector.slack.connector:slack-connector-demo (@version@) _(optional)_

```xml
<dependency>
  <groupId>com.axonivy.connector.slack.connector</groupId>
  <artifactId>slack-connector-demo</artifactId>
  <version>@version@</version>
  <type>iar</type>
</dependency>
```
