package com.axonivy.connector.slack;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

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