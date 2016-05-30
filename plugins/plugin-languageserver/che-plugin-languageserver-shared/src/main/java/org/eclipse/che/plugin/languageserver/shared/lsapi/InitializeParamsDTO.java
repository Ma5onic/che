/**
 * Copyright (c) 2016 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.plugin.languageserver.shared.lsapi;

import org.eclipse.che.dto.shared.DTO;

import io.typefox.lsapi.InitializeParams;

@DTO
public interface InitializeParamsDTO extends InitializeParams {
    /**
     * The process Id of the parent process that started the server.
     * 
     */
    public abstract void setProcessId(final int processId);

    /**
     * The rootPath of the workspace. Is null if no folder is open.
     * 
     */
    public abstract void setRootPath(final String rootPath);

    /**
     * The capabilities provided by the client (editor)
     * 
     */
    public abstract void setCapabilities(final ClientCapabilitiesDTO capabilities);
    
    /**
     * The capabilities provided by the client (editor)
     */
    public abstract ClientCapabilitiesDTO getCapabilities();
}
