/**
 * Copyright (c) 2016 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.plugin.languageserver.shared.lsapi;

import org.eclipse.che.dto.shared.DTO;

import io.typefox.lsapi.CodeActionParams;

@DTO
public interface CodeActionParamsDTO extends CodeActionParams {
    /**
     * The document in which the command was invoked. Overridden to return the
     * DTO type.
     * 
     */
    public abstract TextDocumentIdentifierDTO getTextDocument();

    /**
     * The document in which the command was invoked.
     * 
     */
    public abstract void setTextDocument(final TextDocumentIdentifierDTO textDocument);

    /**
     * The range for which the command was invoked. Overridden to return the DTO
     * type.
     * 
     */
    public abstract RangeDTO getRange();

    /**
     * The range for which the command was invoked.
     * 
     */
    public abstract void setRange(final RangeDTO range);

    /**
     * Context carrying additional information. Overridden to return the DTO
     * type.
     * 
     */
    public abstract CodeActionContextDTO getContext();

    /**
     * Context carrying additional information.
     * 
     */
    public abstract void setContext(final CodeActionContextDTO context);
}
