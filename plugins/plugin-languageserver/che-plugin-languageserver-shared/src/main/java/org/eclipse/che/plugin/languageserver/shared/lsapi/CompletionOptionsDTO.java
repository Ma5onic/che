/**
 * Copyright (c) 2016 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.plugin.languageserver.shared.lsapi;

import java.util.List;

import org.eclipse.che.dto.shared.DTO;

import io.typefox.lsapi.CompletionOptions;

@DTO
public interface CompletionOptionsDTO extends CompletionOptions {
    /**
     * The server provides support to resolve additional information for a
     * completion item.
     * 
     */
    public abstract void setResolveProvider(final boolean resolveProvider);

    /**
     * The characters that trigger completion automatically.
     * 
     */
    public abstract void setTriggerCharacters(final List<String> triggerCharacters);
}
