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
package org.eclipse.che.api.workspace.server.event;

import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.core.notification.EventService;
import org.eclipse.che.api.core.notification.EventSubscriber;
import org.eclipse.che.api.machine.server.MachineManager;
import org.eclipse.che.api.machine.server.model.impl.MachineImpl;
import org.eclipse.che.api.machine.shared.dto.event.MachineStatusEvent;
import org.eclipse.che.api.workspace.server.WorkspaceRuntimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import static org.eclipse.che.api.machine.shared.dto.event.MachineStatusEvent.EventType.CREATING;

/**
 * The class listens changing of machine status and perform some actions when new machine is creating.
 *
 * @author Mykola Morhun
 */
@Singleton
public class CreateMachineEventListener implements EventSubscriber<MachineStatusEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(DestroyedMachineEventListener.class);

    private final EventService      eventService;
    private final MachineManager    machineManager;
    private final WorkspaceRuntimes runtimes;

    @Inject
    public CreateMachineEventListener(EventService eventService,
                                      MachineManager machineManager,
                                      WorkspaceRuntimes runtimes) {
        this.eventService = eventService;
        this.machineManager = machineManager;
        this.runtimes = runtimes;
    }

    @Override
    public void onEvent(MachineStatusEvent event) {
        if (CREATING.equals(event.getEventType())) {
            if (!event.isDev()) {
                try {
                    String workspaceId = event.getWorkspaceId();
                    MachineImpl machine = machineManager.getMachine(event.getMachineId());
                    runtimes.addMachineIntoRuntime(workspaceId, machine);
                } catch (NotFoundException | ServerException | ConflictException exception) {
                    LOG.error(exception.getLocalizedMessage(), exception);
                }
            }
        }
    }

    @PostConstruct
    private void subscribe() {
        eventService.subscribe(this);
    }

    @PreDestroy
    private void unsubscribe() {
        eventService.unsubscribe(this);
    }
}
