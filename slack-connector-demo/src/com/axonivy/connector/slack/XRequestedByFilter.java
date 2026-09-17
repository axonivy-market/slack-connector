package com.axonivy.connector.slack;

import java.io.IOException;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class XRequestedByFilter implements ContainerRequestFilter {
	private static final String SLACK_PREFIX = "incident";

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {
		String path = ctx.getUriInfo().getPath();
		if (path != null && path.startsWith(SLACK_PREFIX)) {
			if (ctx.getHeaderString("X-Requested-By") == null) {
				ctx.getHeaders().putSingle("X-Requested-By", "slack-integration");
			}
		}
	}
}