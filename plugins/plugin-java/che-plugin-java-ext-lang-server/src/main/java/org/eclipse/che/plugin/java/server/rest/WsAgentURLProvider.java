/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.plugin.java.server.rest;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.eclipse.che.api.machine.shared.Constants.WSAGENT_REFERENCE;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.eclipse.che.api.core.ApiException;
import org.eclipse.che.api.core.rest.HttpJsonRequestFactory;
import org.eclipse.che.api.machine.shared.dto.ServerDto;
import org.eclipse.che.api.workspace.shared.dto.WorkspaceDto;
import org.eclipse.che.ide.rest.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides URL to workspace agent inside container.
 *
 * @author Anton Korneta
 */
public class WsAgentURLProvider implements Provider<String> {
    private static final Logger LOG = LoggerFactory.getLogger(WsAgentURLProvider.class);

    private final String                 wsId;
    private final String                 workspaceApiEndpoint;
    private final HttpJsonRequestFactory requestFactory;

    private String cachedAgentUrl;

    @Inject
    public WsAgentURLProvider(@Named("api.endpoint") String apiEndpoint,
                              @Named("env.CHE_WORKSPACE_ID") String wsId,
                              HttpJsonRequestFactory requestFactory) {
        this.wsId = wsId;
        try {
			this.workspaceApiEndpoint = new UrlBuilder(apiEndpoint).setPath(new URL(apiEndpoint).getPath() + "/workspace/").buildString();
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid api.endpoint URL : '"+apiEndpoint+"' : "+e.getMessage());
		}
        this.requestFactory = requestFactory;
    }

    @Override
    public String get() {
        if (isNullOrEmpty(cachedAgentUrl)) {
            try {
                final WorkspaceDto workspace = requestFactory.fromUrl(workspaceApiEndpoint + wsId)
                                                             .useGetMethod()
                                                             .request()
                                                             .asDto(WorkspaceDto.class);
                if (workspace.getRuntime() != null) {
                    final Collection<ServerDto> servers = workspace.getRuntime()
                                                                   .getDevMachine()
                                                                   .getRuntime()
                                                                   .getServers()
                                                                   .values();
                    for (ServerDto server : servers) {
                        if (WSAGENT_REFERENCE.equals(server.getRef())) {
                            cachedAgentUrl = server.getUrl();
                            return cachedAgentUrl;
                        }
                    }
                }
            } catch (ApiException | IOException ex) {
                LOG.warn(ex.getLocalizedMessage());
                throw new RuntimeException("Failed to configure wsagent endpoint");
            }
        }
        return cachedAgentUrl;
    }
}
