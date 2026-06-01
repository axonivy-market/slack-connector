# Slack Connector

Der Slack Connector integriert Axon Ivy mit Slack und ermöglicht es Prozessen, Nachrichten zu senden, Slash‑Commands zu verarbeiten und Workflows direkt aus Slack‑Kanälen und von Slack‑Benutzern auszulösen. Er stellt einen aufrufbaren Subprozess zum Senden von Nachrichten, eine kleine Java‑Hilfs‑API und Demo‑Workflows bereit, damit du die Integration schnell evaluieren und erweitern kannst.

[![CI Build](https://github.com/axonivy-market/slack-connector/actions/workflows/ci.yml/badge.svg)](https://github.com/axonivy-market/slack-connector/actions/workflows/ci.yml)

### Wichtigste Funktionen

- Sende Nachrichten an Slack‑Kanäle und -Threads direkt aus Axon Ivy‑Prozessen, um automatisierte Benachrichtigungen und Alerts zu ermöglichen.
- Einfache Java‑Hilfs‑API (`MessageService`) zum programmatischen Senden von Nachrichten aus Java‑Code und Prozessskripten.
- Verarbeite Slack‑Slash‑Commands, um interaktive Workflows (z. B. Incident‑Erstellung) in Slack zu starten.
- Demo‑Implementierungen zum Erstellen von Incidents und Öffnen von Freigabeaufgaben, damit du die Lösung schnell evaluieren kannst.
- Einfache Konfiguration über Produkt‑Variablen und sofort nutzbare Demo‑Installer für ein schnelles Setup.

## Demo

Das Demo‑Modul zeigt, wie Slash‑Commands, Axon Ivy‑Fälle und Slack‑Bot‑Antworten in einem Incident‑Workflow zusammenwirken. Nutze es, um die End‑to‑End‑Erfahrung zu prüfen, bevor du den Connector in deine eigenen Prozesse integrierst.

### Demo workflows

##### Incident aus Slack erstellen

1. Öffne einen Slack‑Kanal, in dem deine App installiert ist, und führe `/ivy-create-incident` mit einer Severity wie `Low`, `Medium`, `High` oder `Critical` aus.

![Create incident slash command in Slack](images/demo-slash-commands.png)

2. Slack sendet die Formular‑Payload an den Demo‑REST‑Endpoint und Axon Ivy startet den Fall `CreateIncident` für den aktuellen Benutzer.

![Incident created message in Slack](images/demo-incident-created-message.png)

3. Öffne die Freigabeaufgabe aus der Slack‑Nachricht, prüfe die Incident‑Details und wähle im Dialog die verantwortliche Rolle.

![Incident approval task dialog](images/demo-incident-approval-task.png)

4. Bestätige oder lehne die Anfrage ab. Das Demo postet die Entscheidung zurück in den ursprünglichen Slack‑Kanal mit der gewählten Rolle.

![Incident approved message in Slack](images/demo-incident-approved-message.png)

5. Führe `/ivy-summary-incident` aus, um eine schnelle Severity‑Zusammenfassung der aktuell laufenden Incidents in Slack zu erhalten.

![Incident summary message in Slack](images/demo-incident-summary.png)

##### Start task listener

1. Starte den Installer‑Eintrag `Task listener` aus dem Demo‑Modul.
2. Lasse den Listener laufen, während du Aufgaben erstellst oder zuweist.
3. Prüfe die Slack‑Benachrichtigungen, die gesendet werden, sobald neue Freigabeaufgaben verfügbar sind.

## Setup

### Variables

```
@variables.yaml@
```

1. Installiere die Connector‑Artefakte in deiner Axon Ivy‑Umgebung.

- Importiere `slack-connector` für die Kernintegration.
- Importiere `slack-connector-demo` ebenfalls, wenn du die Beispiel‑Slash‑Commands, Dialoge und den Task‑Listener nutzen möchtest.

2. Erstelle die Slack‑App, die deinen Axon Ivy‑Bot repräsentiert.

- Öffne https://api.slack.com/apps und klicke auf **Create New App**.
- Wähle **From scratch**, vergebe einen Namen wie `Axon Ivy Bot` und wähle den Ziel‑Workspace aus.

![Create a new Slack app from scratch](images/setup-create-new-slack-app.png)

3. Füge die Bot‑Scopes hinzu, die der Connector benötigt.

- Öffne **OAuth & Permissions** in deiner Slack‑App.
- Füge `chat:write` hinzu, damit der Bot Incident‑Updates posten kann.
- Füge `commands` hinzu, damit Slack die Slash‑Commands ausführen kann.
- Optional: `chat:write.public`, wenn der Bot auch in öffentliche Kanäle posten soll.

![Required Slack bot token scopes](images/setup-add-bot-token-scope.png)

4. Installiere die App in deinem Workspace und kopiere das Bot‑Token.

- Öffne **Install App** und autorisiere die App für deinen Workspace.
- Kopiere das **Bot User OAuth Token**, das nach der Installation angezeigt wird.
- Speichere dieses Token in der Axon Ivy‑Variable `com.axonivy.connector.slack.botToken`.
- Halte den Wert außerhalb der Versionsverwaltung und ersetze lokale Test‑Tokens, bevor du das Projekt teilst.

![Install the Slack app and copy the bot token](images/setup-install-slack-app.png)

5. Erstelle die Slash‑Commands, die das Demo verwendet.

- Öffne **Features → Slash Commands** und klicke auf **Create New Command**.
- Erstelle den Slash‑Command, z. B. `/ivy-create-incident`, und setze die Request URL auf deine öffentliche Axon Ivy‑URL.
- Ein Usage‑Hint wie `Low, Medium, High, Critical` kann verwendet werden, um Parameter von Slack an die Axon Ivy‑Anwendung zu übergeben.
- Reinstalliere die App, falls Slack nach dem Speichern der Commands eine Aktualisierung der Berechtigungen verlangt.

![Create the Slack slash command](images/setup-create-slack-slash-command.png)

## Components

### Callable Subprocesses

#### SendBotMessage.p.json

- **sendBotMessage(String message, String channel, String botToken)**
  - Input:
    - `message` (String) — Nachrichtentext, der gesendet wird
    - `channel` (String) — Kanal‑ID oder Name
    - `botToken` (String) — optionales Bot‑OAuth‑Token, falls überschrieben

### Dialog Components

- Keine Dialogkomponenten verfügbar.

### Web Services

- **Slack API (Slack Web API)** — Spec URL: `https://github.com/slackapi/slack-api-specs/blob/master/web-api/slack_web_openapi_v2.json` (Namespace: `com.slack.api.client`)

### Maven Artifacts

1. com.axonivy.connector.slack.connector:slack-connector

```xml
<dependency>
  <groupId>com.axonivy.connector.slack.connector</groupId>
  <artifactId>slack-connector</artifactId>
  <type>iar</type>
</dependency>
```

2. com.axonivy.connector.slack.connector:slack-connector-demo (optional)

```xml
<dependency>
  <groupId>com.axonivy.connector.slack.connector</groupId>
  <artifactId>slack-connector-demo</artifactId>
  <type>iar</type>
</dependency>
```
