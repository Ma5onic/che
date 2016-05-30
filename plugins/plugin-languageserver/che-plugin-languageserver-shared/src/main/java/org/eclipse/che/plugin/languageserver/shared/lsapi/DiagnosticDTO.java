/**
 * Copyright (c) 2016 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.plugin.languageserver.shared.lsapi;

import org.eclipse.che.dto.shared.DTO;

import io.typefox.lsapi.Diagnostic;

@DTO
public interface DiagnosticDTO extends Diagnostic {
    /**
     * The range at which the message applies Overridden to return the DTO type.
     * 
     */
    public abstract RangeDTO getRange();

    /**
     * The range at which the message applies
     * 
     */
    public abstract void setRange(final RangeDTO range);

    /**
     * The diagnostic's severity. Can be omitted. If omitted it is up to the
     * client to interpret diagnostics as error, warning, info or hint.
     * 
     */
    public abstract void setSeverity(final Integer severity);

    /**
     * The diagnostic's code. Can be omitted.
     * 
     */
    public abstract void setCode(final String code);

    /**
     * A human-readable string describing the source of this diagnostic, e.g.
     * 'typescript' or 'super lint'.
     * 
     */
    public abstract void setSource(final String source);

    /**
     * The diagnostic's message.
     * 
     */
    public abstract void setMessage(final String message);
}
