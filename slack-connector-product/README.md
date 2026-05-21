<!--
Dear developer!

When you create your very valuable documentation, please be aware that this Readme.md is not only published on github. This documentation is also processed automatically and published on our website. For this to work, the two headings "Demo" and "Setup" must not be changed. Do also not change the order of the headings. Feel free to add sub-sections wherever you want.
-->

# Slack Connector Connector

The Slack Connector integrates Axon Ivy with Slack, enabling processes to post messages, handle slash-commands, and trigger workflows directly from Slack channels and users. It provides a callable subprocess for sending messages, a small Java helper API, and demo workflows to help you evaluate and extend the integration.

## Key features

- Send messages to Slack channels and threads directly from Axon Ivy processes, enabling automated notifications and alerts.
- Post rich Block/Attachment messages to format interactive notifications and complex updates.
- Simple Java helper API (`MessageService`) to send messages programmatically from Java code and process scripts.
- Handle Slack Slash Commands to start interactive workflows (e.g., create incidents) from Slack.
- Demo implementations for creating incidents and opening approval tasks to help you evaluate quickly.
- Easy configuration via product variables and ready-to-use demo installers for quick setup.

## Demo

See the demo implementations in the `slack-connector-demo` module. The demo shows how a Slack slash-command maps into an Axon Ivy workflow that opens the Incident Details dialog and triggers tasks.

### Demo workflows

#### slack-connector-demo (slack-connector-demo)

##### Create Incident

1. Install and configure the demo as described in the Setup section.
2. From Slack, run the configured slash command (for example, `/create-incident critical`) in a channel or direct message.
3. The demo maps the command payload to an Axon Ivy workflow and opens the Incident Details dialog pre-filled with the command parameters.
4. Complete the dialog to create the incident and trigger the approval task.

## Setup

- **Roles:** Everybody (configured in config/roles.xml)
- **OpenAPI:** No public OpenAPI specification is available. A local OpenAPI spec is referenced in `config/rest-clients.yaml` as `file:///C:/Users/pvquan/Downloads/slack_web_openapi_v2.json` (namespace: `com.slack.api.client`).

### Variables

```
@variables.yaml@
```

1. Create a Slack App and Bot
   1.1. Visit https://api.slack.com/apps and click "Create New App → From scratch". Give the app a name and select your workspace.
   1.2. Under "OAuth & Permissions" add the Bot Token Scopes required by your workflows: - Required: `chat:write` (post messages), `commands` (slash commands) - Optional: `chat:write.public`, `channels:read`, `users:read` (depending on features)
   1.3. Install the App to your workspace via "OAuth & Permissions → Install App to Workspace" and authorize it.
   1.4. After installation copy the **Bot User OAuth Token** (starts with `xoxb-...`). This is the `botToken` used by the connector.
   1.5. Under "Basic Information → App Credentials" copy the **Signing Secret**. Use this to verify incoming requests.

2. Configure Axon Ivy
   2.1. Set `com.axonivy.connector.slack.botToken` to the Bot User OAuth Token (keep it secret).
   2.2. (Recommended) Store the Signing Secret in a secure product variable (for example `com.axonivy.connector.slack.signingSecret`) and use it to validate incoming Slack requests.
   2.3. Ensure `com.axonivy.connector.slack.baseUrl` is `https://slack.com/api` (default).

3. Create a Slash Command
   3.1. In your Slack App, go to "Features → Slash Commands" and click "Create New Command".
   3.2. Choose the command name (for example `/create-incident`).
   3.3. Set the Request URL to a public HTTPS endpoint on your Axon Ivy instance that will receive command payloads. For local testing use a tunnel such as ngrok (see step 4).
   3.4. Add a short description (e.g., "Create an incident from Slack") and a usage hint (e.g., `/create-incident <severity> [description]`). Save the command.
   3.5. Reinstall the app if Slack prompts you to accept updated scopes.

4. Local testing with ngrok
   4.1. Start a tunnel to your Ivy instance (for example `ngrok http 8080`).
   4.2. Use the generated HTTPS URL as the Slash Command Request URL, e.g. `https://<ngrok-id>.ngrok.io/slack/commands`.
   4.3. Execute the slash command from Slack and observe the demo workflow behavior in Ivy.

5. Verify and secure incoming requests
   5.1. Slack sends requests with headers `X-Slack-Request-Timestamp` and `X-Slack-Signature`. To verify: - Build the basestring: `v0:` + timestamp + `:` + raw request body. - Compute HMAC-SHA256 of the basestring using the Signing Secret. - Prepend `v0=` to the hex digest and compare to `X-Slack-Signature`. - Reject requests older than 5 minutes to prevent replay attacks.
   5.2. Respond to Slack within ~3 seconds. If processing takes longer, return `200 OK` immediately and use `response_url` to send follow-up messages.

6. Test posting messages directly

```bash
curl -X POST \
  -H "Authorization: Bearer xoxb-..." \
  -d "channel=#general&text=Hello from Axon Ivy" \
  https://slack.com/api/chat.postMessage
```

7. Mapping to demo workflows
   7.1. The demo `CreateIncident` maps the incoming slash command payload to `slashCommandData` and extracts `text` as the severity (for example `/create-incident critical` → severity `critical`).
   7.2. Invoke the command from Slack and confirm the demo opens the Incident Details dialog as configured.

8. Security & production notes

- Keep tokens and signing secrets in a secret manager or environment variables; do not commit them to source control.
- Rotate tokens periodically and restrict scopes to the minimum required.
- Ensure the Slash Command endpoint is HTTPS and reachable by Slack in production.

## Components

### Connector Processes

#### SendBotMessage.p.json

- **sendBotMessage(String message, String channel, String botToken)**
  - Input:
    - `message` (String) — message text to post
    - `channel` (String) — channel ID or name
    - `botToken` (String) — Bot OAuth token (used if provided)
  - Result:
    - `out.botToken` (String) — from `param.botToken`
    - `out.channelId` (String) — from `param.channel`
    - `out.message` (String) — from `param.message`

### Form Components

- No information was delivered for this section.

### Maven artifacts

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
