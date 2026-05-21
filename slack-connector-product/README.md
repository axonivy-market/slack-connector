# Slack Connector

The Slack Connector integrates Axon Ivy with Slack, enabling processes to post messages, handle slash-commands, and trigger workflows directly from Slack channels and users. It provides a callable subprocess for sending messages, a small Java helper API, and demo workflows to help you evaluate and extend the integration.

[![CI Build](https://github.com/axonivy-market/slack-connector/actions/workflows/ci.yml/badge.svg)](https://github.com/axonivy-market/slack-connector/actions/workflows/ci.yml)

### Key features

- Send messages to Slack channels and threads directly from Axon Ivy processes, enabling automated notifications and alerts.
- Simple Java helper API (`MessageService`) to send messages programmatically from Java code and process scripts.
- Handle Slack Slash Commands to start interactive workflows (e.g., create incidents) from Slack.
- Demo implementations for creating incidents and opening approval tasks to help you evaluate quickly.
- Easy configuration via product variables and ready-to-use demo installers for quick setup.

## Demo

The demo module shows how Slack commands, Axon Ivy cases, and Slack bot responses work together in one incident workflow. Use it to validate the end-to-end experience before integrating the connector into your own processes.

### Demo workflows

#### slack-connector-demo (slack-connector-demo)

##### Create incident from Slack

1. Open a Slack channel where your app is installed and run `/ivy-create-incident` with a severity such as `Low`, `Medium`, `High`, or `Critical`.

![Create incident slash command in Slack](images/demo-slash-commands.png)

2. Slack sends the form payload to the demo REST endpoint and Axon Ivy starts the `CreateIncident` case for the current user.

![Incident created message in Slack](images/demo-incident-created-message.png)

3. Open the approval task from the Slack message to review the incident details and choose the responsible role in the dialog.

![Incident approval task dialog](images/demo-incident-approval-task.png)

4. Confirm or reject the request. The demo posts the decision back to the original Slack channel with the selected role.

![Incident approved message in Slack](images/demo-incident-approved-message.png)

5. Run `/ivy-summary-incident` to receive a quick severity summary of the currently running incidents in Slack.

![Incident summary message in Slack](images/demo-incident-summary.png)

##### Start task listener

1. Launch the `Task listener` installer entry from the demo module.
2. Keep the listener running while you create or assign tasks in Axon Ivy.
3. Review the Slack notifications that are sent when new approval work becomes available.

## Setup

- **Roles:** Everybody (configured in `config/roles.xml`)
- **OpenAPI:** Slack Web API spec at https://github.com/slackapi/slack-api-specs/blob/master/web-api/slack_web_openapi_v2.json with namespace `com.slack.api.client`

### Variables

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

1. Install the connector artifacts into your Axon Ivy environment.
   1.1. Import `slack-connector` for the core integration.
   1.2. Import `slack-connector-demo` as well if you want the sample slash commands, dialog, and task listener.

2. Create the Slack app that will represent the Axon Ivy bot.
   2.1. Open https://api.slack.com/apps and click **Create New App**.
   2.2. Choose **From scratch**, enter a name such as `Axon Ivy Bot`, and select the target Slack workspace.

![Create a new Slack app from scratch](images/setup-create-new-slack-app.png)

3. Add the bot scopes required by the connector.
   3.1. Open **OAuth & Permissions** in your Slack app.
   3.2. Add `chat:write` so the bot can post incident updates.
   3.3. Add `commands` so Slack can execute the slash commands.
   3.4. Add `chat:write.public` as well if you want the bot to post to public channels before it is invited.

![Required Slack bot token scopes](images/setup-add-bot-token-scope.png)

4. Install the app into your workspace and copy the bot token.
   4.1. Open **Install App** and authorize the app for your workspace.
   4.2. Copy the **Bot User OAuth Token** shown after installation.
   4.3. Store that token in the Axon Ivy variable `com.axonivy.connector.slack.botToken`.
   4.4. Keep the value outside source control and replace local test tokens before sharing the project.

![Install the Slack app and copy the bot token](images/setup-install-slack-app.png)

5. Create the slash commands used by the demo.
   5.1. Open **Features** -> **Slash Commands** and click **Create New Command**.
   5.2. Create `/ivy-create-incident` and set the Request URL to your public Axon Ivy application base URL plus `/api/incident/create`.
   5.3. Use a short description such as `run create Incident process with severity`.
   5.4. Use a usage hint such as `Low, Medium, High, Critical`.
   5.5. Create `/ivy-summary-incident` as a second command and point it to `/api/incident/summary` on the same public base URL.
   5.6. Reinstall the app if Slack asks you to refresh permissions after saving the commands.

![Create the Slack slash command](images/setup-create-slack-slash-command.png)

6. Point Slack to the correct public Axon Ivy URL.
   6.1. The REST resource in the demo is implemented at `@Path("/incident")` with POST endpoints `/create` and `/summary`.
   6.2. A typical public command URL therefore looks like `https://<your-ivy-app-base-url>/api/incident/create`.
   6.3. If your Axon Ivy environment is published under an extra path segment such as `/designer`, keep that segment and append `/api/incident/create` or `/api/incident/summary`.

7. Verify the integration.
   7.1. Run `/ivy-create-incident Low` in Slack and confirm that Slack returns `Process CreateIncident has been started by user: ...`.
   7.2. Open the approval task, choose a responsible role, and confirm or reject the incident.
   7.3. Run `/ivy-summary-incident` and check that the channel receives the current severity counts.

## Components

### Connector processes

#### SendBotMessage.p.json

- **sendBotMessage(String message, String channel, String botToken) -> out: com.axonivy.connector.slack.SendBotMessageData**
  - Input:
    - `message` (String)
    - `channel` (String)
    - `botToken` (String)

### Form Components

#### IncidentDetailDialog — Review and resolve incident requests from Slack

- **Namespace:** `com.axonivy.connector.slack.IncidentDetailDialog`
- **Component type:** HTML_DIALOG
- **Fields:**
  - `channel` — Slack channel identifier used for the response message
  - `severity` — Incident severity passed in from the slash command
  - `responsible` — Selected role that is returned to the workflow on confirm or reject
- **Where used:** `CreateIncident` demo workflow
- **Purpose:** Presents the incident approval form, lets the user choose the responsible role, and sends the decision back to Slack.

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
