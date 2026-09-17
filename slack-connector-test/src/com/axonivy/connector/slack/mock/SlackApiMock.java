package com.axonivy.connector.slack.mock;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Hidden;

@PermitAll
@Path("slackDataMock")
@Hidden
public class SlackApiMock {

	private static final AtomicInteger CALL_COUNT = new AtomicInteger();

	@POST
	@Path("chat.postMessage")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendBotMessage(@HeaderParam("Authorization") String authorization,
			@FormParam("channel") String channel, @FormParam("text") String text) {
		reset();
		CALL_COUNT.incrementAndGet();

		if (authorization == null || authorization.isBlank()) {
			return Response.status(400).build();
		}
		return Response.status(201).build();
	}

	public static void reset() {
		CALL_COUNT.set(0);
	}

	public static int getCallCount() {
		return CALL_COUNT.get();
	}

}