package com.axonivy.connector.slack.mock;

import java.io.IOException;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class MockFilter implements ContainerRequestFilter {
	private static final String MOCK_PREFIX = "slackDataMock";
	private static final String X_REQUESTED_BY = "X-Requested-By";

	@Override
	public void filter(ContainerRequestContext ctx) throws IOException {
		String path = ctx.getUriInfo().getPath();
		if (path != null && path.startsWith(MOCK_PREFIX)) {
			if (ctx.getHeaderString(X_REQUESTED_BY) == null) {
				ctx.getHeaders().putSingle(X_REQUESTED_BY, "mock");
			}
		}
	}
}