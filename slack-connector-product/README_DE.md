<!--
Dear developer!

When you create your very valuable documentation, please be aware that this Readme.md is not only published on github. This documentation is also processed automatically and published on our website. For this to work, the two headings "Demo" and "Setup" must not be changed. Do also not change the order of the headings. Feel free to add sub-sections wherever you want.
-->

# Slack Connector Connector

Der Slack Connector verbindet Axon Ivy mit Slack und ermöglicht dir, aus Prozessen heraus Nachrichten zu senden, Slash-Commands zu verarbeiten und Workflows direkt aus Slack auszulösen. Er stellt einen `CALLABLE_SUB` zum Versenden von Nachrichten, eine kleine Java-Hilfs-API (`MessageService`) und Beispiel-Workflows zur schnellen Evaluierung bereit.

## Hauptfunktionen

- Sende Nachrichten an Slack-Kanäle und Threads direkt aus Axon Ivy-Prozessen, um automatisierte Benachrichtigungen und Alerts zu realisieren.
- Poste reichhaltige Block-/Attachment-Nachrichten, um interaktive Benachrichtigungen und strukturierte Updates darzustellen.
- Einfache Java-Hilfs-API (`MessageService`), um Nachrichten programmatisch aus Java-Code und Prozessskripten zu senden.
- Verarbeite Slack-Slash-Commands, um interaktive Workflows (z. B. Incident-Erstellung) direkt aus Slack zu starten.
- Demo-Implementierungen zum Erstellen von Incidents und Öffnen von Genehmigungsaufgaben, damit du schnell testen kannst.
- Einfache Konfiguration über Produktvariablen und fertige Demo-Installer für eine schnelle Inbetriebnahme.

## Demo

Sieh dir die Demo-Implementierungen im Modul `slack-connector-demo` an. Die Demo zeigt, wie ein Slack-Slash-Command in einen Axon Ivy-Workflow gemappt wird, der den Incident-Details-Dialog öffnet und Aufgaben auslöst.

### Demo-Workflows

#### slack-connector-demo (slack-connector-demo)

##### Create Incident

1. Installiere und konfiguriere die Demo wie unter Setup beschrieben.
2. Führe in Slack den konfigurierten Slash-Command aus (zum Beispiel `/create-incident critical`) in einem Kanal oder Direkt-Chat.
3. Die Demo mappt die Command-Payload in einen Axon Ivy-Workflow und öffnet den Incident-Details-Dialog mit vorausgefüllten Parametern.
4. Vervollständige das Dialogformular, um den Incident zu erstellen und die Genehmigungsaufgabe zu starten.

## Konfiguration und Slack-Setup

- **Rollen:** Everybody (konfiguriert in `config/roles.xml`)
- **OpenAPI:** Es ist keine öffentliche OpenAPI-Spezifikation verfügbar. In `config/rest-clients.yaml` wird eine lokale Spec referenziert: `file:///C:/Users/pvquan/Downloads/slack_web_openapi_v2.json` (Namespace: `com.slack.api.client`).

### Variablen

```
@variables.yaml@
```

1. Erstelle eine Slack-App und einen Bot
   1.1. Besuche https://api.slack.com/apps und klicke auf "Create New App → From scratch". Vergib der App einen Namen und wähle deinen Workspace.
   1.2. Unter "OAuth & Permissions" füge die benötigten Bot-Scopes hinzu: - Erforderlich: `chat:write` (Nachrichten posten), `commands` (Slash-Commands) - Optional: `chat:write.public`, `channels:read`, `users:read` (je nach Bedarf)
   1.3. Installiere die App im Workspace über "OAuth & Permissions → Install App to Workspace" und autorisiere sie.
   1.4. Nach der Installation kopiere das **Bot User OAuth Token** (beginnt mit `xoxb-...`). Dies ist das `botToken`, das der Connector verwendet.
   1.5. Unter "Basic Information → App Credentials" kopiere das **Signing Secret**. Damit verifizierst du eingehende Requests von Slack.

2. Axon Ivy konfigurieren
   2.1. Setze `com.axonivy.connector.slack.botToken` auf das Bot User OAuth Token (geheim halten).
   2.2. (Empfohlen) Lege das Signing Secret in einer sicheren Produkt-Variable (z. B. `com.axonivy.connector.slack.signingSecret`) ab und nutze es zur Validierung eingehender Slack-Requests.
   2.3. Stelle sicher, dass `com.axonivy.connector.slack.baseUrl` auf `https://slack.com/api` gesetzt ist (Standard).

3. Erstelle einen Slash-Command
   3.1. In der Slack-App unter "Features → Slash Commands" auf "Create New Command" klicken.
   3.2. Wähle den Befehlsnamen (z. B. `/create-incident`).
   3.3. Setze die Request URL auf einen öffentlichen HTTPS-Endpunkt deiner Axon Ivy-Instanz, der Command-Payloads empfängt. Für lokale Tests nutze einen Tunnel wie ngrok (siehe Schritt 4).
   3.4. Vergib eine kurze Beschreibung (z. B. "Create an incident from Slack") und einen Usage-Hint (z. B. `/create-incident <severity> [description]`). Speichere den Befehl.
   3.5. Installiere die App ggf. neu, falls Slack dies nach Scope-Änderungen fordert.

4. Lokales Testen mit ngrok
   4.1. Starte einen Tunnel zu deiner Ivy-Instanz, z. B. `ngrok http 8080`.
   4.2. Verwende die generierte HTTPS-URL als Request URL des Slash-Commands, z. B. `https://<ngrok-id>.ngrok.io/slack/commands`.
   4.3. Führe den Slash-Command in Slack aus und beobachte das Verhalten der Demo-Workflows in Ivy.

5. Verifizieren und absichern eingehender Requests
   5.1. Slack sendet Header `X-Slack-Request-Timestamp` und `X-Slack-Signature`. Zur Verifikation: - Baue den Basestring: `v0:` + Zeitstempel + `:` + roher Request-Body. - Berechne HMAC-SHA256 des Basestrings mit dem Signing Secret. - Füge `v0=` vor die hex-kodierte Signatur und vergleiche mit `X-Slack-Signature`. - Lehne Requests ab, die älter als 5 Minuten sind (Replay-Schutz).
   5.2. Antworte Slack innerhalb von ~3 Sekunden. Dauert die Verarbeitung länger, sende sofort `200 OK` und verwende `response_url` für Folge-Nachrichten.

6. Test: Bot-Nachricht per cURL senden

```bash
curl -X POST \
  -H "Authorization: Bearer xoxb-..." \
  -d "channel=#general&text=Hello from Axon Ivy" \
  https://slack.com/api/chat.postMessage
```

7. Zuordnung zu Demo-Workflows
   7.1. Die Demo `CreateIncident` mappt die eingehende Slash-Command-Payload auf `slashCommandData` und extrahiert `text` als Schweregrad (z. B. `/create-incident critical` → `severity = critical`).
   7.2. Führe den Befehl in Slack aus und prüfe, ob die Demo wie erwartet den Incident-Details-Dialog öffnet.

8. Sicherheits- & Produktionshinweise

- Bewahre Tokens und Signing Secrets sicher auf (Secret Manager, Umgebungsvariablen); committe sie nicht in den Quellcode.
- Rötiere Tokens regelmäßig und beschränke Scopes auf das Minimum.
- Stelle sicher, dass die Slash-Command-Endpoint in Produktion per HTTPS erreichbar ist.

## Komponenten

### Connector-Prozesse

#### SendBotMessage.p.json

- **sendBotMessage(String message, String channel, String botToken)**
  - Eingabe:
    - `message` (String) — Nachrichtentext, der gepostet werden soll
    - `channel` (String) — Kanal-ID oder Name
    - `botToken` (String) — Bot-OAuth-Token (falls angegeben)
  - Ergebnis:
    - `out.botToken` (String) — übernommen von `param.botToken`
    - `out.channelId` (String) — übernommen von `param.channel`
    - `out.message` (String) — übernommen von `param.message`

### Formular-Komponenten

- Es wurden keine Formular-Komponenten geliefert.

### Maven-Artefakte

1.  <dependency>
    	<groupId>com.axonivy.connector.slack.connector</groupId>
    	<artifactId>slack-connector-demo</artifactId>
    	<version>${version}</version>
    	<type>iar</type>
     </dependency>

2.  <dependency>
    	<groupId>com.axonivy.connector.slack.connector</groupId>
    	<artifactId>slack-connector</artifactId>
    	<version>${version}</version>
    	<type>iar</type>
     </dependency>
