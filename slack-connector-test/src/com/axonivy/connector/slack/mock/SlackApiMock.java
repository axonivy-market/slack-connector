package com.axonivy.connector.slack.mock;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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