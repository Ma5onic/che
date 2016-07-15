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
package org.eclipse.che;

import javax.inject.Provider;

import com.google.inject.name.Named;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Provides URI of Che API endpoint for usage inside machine to be able to connect to host machine using docker host IP.
 *
 * @author Alexander Garagatyi
 */
public class UriApiEndpointProvider implements Provider<URI> {

    private @Named("api.endpoint") String apiEndpoint; 

    @Override
    public URI get() {
        try {
            return new URI(apiEndpoint);
        } catch (URISyntaxException e) {
            throw new RuntimeException("System variable CHE_API_ENDPOINT contains invalid URL of Che api endpoint '" +
                                       apiEndpoint+"' : "+e.getMessage());
        }
    }
}
