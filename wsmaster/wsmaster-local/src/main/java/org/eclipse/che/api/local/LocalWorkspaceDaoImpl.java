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
package org.eclipse.che.api.local;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.core.model.machine.Recipe;
import org.eclipse.che.api.core.model.project.ProjectConfig;
import org.eclipse.che.api.core.model.workspace.WorkspaceStatus;
import org.eclipse.che.api.local.storage.LocalStorage;
import org.eclipse.che.api.local.storage.LocalStorageFactory;
import org.eclipse.che.api.machine.server.recipe.adapters.RecipeTypeAdapter;
import org.eclipse.che.api.workspace.server.model.impl.WorkspaceImpl;
import org.eclipse.che.api.workspace.server.spi.WorkspaceDao;
import org.eclipse.che.commons.env.EnvironmentContext;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;

/**
 * In memory based implementation of {@link WorkspaceDao}.
 *
 * <p>{@link #loadWorkspaces() Loads} & {@link #saveWorkspaces() stores} in memory workspaces
 * to/from filesystem, when component starts/stops.
 *
 * @implNote it is thread-safe, guarded by <i>this</i> instance
 *
 * @author Eugene Voevodin
 * @author Dmitry Shnurenko
 *
 */
@Singleton
public class LocalWorkspaceDaoImpl implements WorkspaceDao {

    private final Map<String, WorkspaceImpl> workspaces;
    private final LocalStorage               localStorage;

    @Inject
    public LocalWorkspaceDaoImpl(LocalStorageFactory factory) throws IOException {
        final Map<Class<?>, Object> adapters = ImmutableMap.of(Recipe.class, new RecipeTypeAdapter(),
                                                               ProjectConfig.class, new ProjectConfigAdapter());
        this.localStorage = factory.create("workspaces.json", adapters);
        this.workspaces = new HashMap<>();
    }

    @PostConstruct
    public synchronized void loadWorkspaces() {
        workspaces.putAll(localStorage.loadMap(new TypeToken<Map<String, WorkspaceImpl>>() {}));
        for (WorkspaceImpl workspace : workspaces.values()) {
            workspace.setRuntime(null);
        }
    }

    @PreDestroy
    public synchronized void saveWorkspaces() throws IOException {
        localStorage.store(workspaces);
    }

    @Override
    public synchronized WorkspaceImpl create(WorkspaceImpl workspace) throws ConflictException, ServerException {
        if (workspaces.containsKey(workspace.getId())) {
            throw new ConflictException("Workspace with id " + workspace.getId() + " already exists");
        }
        if (find(workspace.getConfig().getName(), workspace.getNamespace()).isPresent()) {
            throw new ConflictException(format("Workspace with name %s and owner %s already exists",
                                               workspace.getConfig().getName(),
                                               workspace.getNamespace()));
        }
        workspace.setRuntime(null);
        workspace.setStatus(WorkspaceStatus.STOPPED);
        workspaces.put(workspace.getId(), new WorkspaceImpl(workspace));
        return workspace;
    }

    @Override
    public synchronized WorkspaceImpl update(WorkspaceImpl workspace)
            throws NotFoundException, ConflictException, ServerException {
        if (!workspaces.containsKey(workspace.getId())) {
            throw new NotFoundException("Workspace with id " + workspace.getId() + " was not found");
        }
        if (!hasPermission(workspaces.get(workspace.getId()))) {
        	throw new ConflictException("Workspace with id " + workspace.getId() + " is not owned by user "+getCurrentUserName());
        }
        workspace.setStatus(null);
        workspace.setRuntime(null);
        workspaces.put(workspace.getId(), new WorkspaceImpl(workspace));
        return workspace;
    }

    @Override
    public synchronized void remove(String id) throws ConflictException, ServerException {
        workspaces.remove(id);
    }

    @Override
    public synchronized WorkspaceImpl get(String id) throws NotFoundException, ServerException {
        final WorkspaceImpl workspace = workspaces.get(id);
        if (workspace == null) {
            throw new NotFoundException("Workspace with id " + id + " was not found");
        }
        checkPermission(workspace);
        return new WorkspaceImpl(workspace);
    }

    @Override
    public synchronized WorkspaceImpl get(String name, String namespace) throws NotFoundException, ServerException {
        final Optional<WorkspaceImpl> wsOpt = find(name, namespace);
        if (!wsOpt.isPresent()) {
            throw new NotFoundException(format("Workspace with name %s and owner %s was not found", name, namespace));
        }
        checkPermission(wsOpt.get());
        return new WorkspaceImpl(wsOpt.get());
    }
    
    private boolean hasPermission(WorkspaceImpl ws) {
    	return getCurrentUserName().equals(ws.getNamespace());
    }
    
    private void checkPermission(WorkspaceImpl ws) throws NotFoundException {
    	if (!hasPermission(ws))
    		throw new NotFoundException("Workspace "+ws.getId()+" not found for user "+getCurrentUserName());
    }

	private String getCurrentUserName() {
		if (EnvironmentContext.getCurrent().getSubject() == null) {
			return "user123"; // test user
		}
		return EnvironmentContext.getCurrent().getSubject().getUserName();
	}

    @Override
    public synchronized List<WorkspaceImpl> getByNamespace(String namespace) throws ServerException {
        return workspaces.values()
                         .stream()
                         .filter(this::hasPermission)
                         .map(WorkspaceImpl::new)
                         .collect(toList());
    }

    @Override
    public List<WorkspaceImpl> getWorkspaces(String userId) throws ServerException {
        return workspaces.values().stream().filter(this::hasPermission).collect(Collectors.toList());
    }

    private Optional<WorkspaceImpl> find(String name, String owner) {
        return workspaces.values()
                         .stream()
                         .filter(this::hasPermission)
                         .filter(ws -> ws.getConfig().getName().equals(name) && ws.getNamespace().equals(owner))
                         .findFirst();
    }
}
