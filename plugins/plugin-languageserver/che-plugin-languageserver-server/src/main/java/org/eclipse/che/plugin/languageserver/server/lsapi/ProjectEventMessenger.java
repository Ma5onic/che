package org.eclipse.che.plugin.languageserver.server.lsapi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.che.api.core.notification.EventService;
import org.eclipse.che.api.core.notification.EventSubscriber;
import org.eclipse.che.api.project.server.ProjectCreatedEvent;
import org.eclipse.che.api.project.server.ProjectDeletedEvent;
import org.eclipse.che.plugin.languageserver.server.LanguageServerRegistry;

import com.google.inject.Inject;

public class ProjectEventMessenger {

	private EventService eventService;
	private EventSubscriber<ProjectCreatedEvent> createEventSubscriber;
	private EventSubscriber<ProjectDeletedEvent> deleteEventSubscriber;
	private LanguageServerRegistry languageServerRegistry;

	@Inject
	public ProjectEventMessenger(final EventService eventService, LanguageServerRegistry languageServerRegistry) {
		this.eventService = eventService;
		this.languageServerRegistry = languageServerRegistry;
	}
	
	public void fireConfigurationChanged(final Object event) {
		languageServerRegistry.getLanguageServers().forEach(
				server-> server.getWorkspaceService().didChangeConfiguraton(()->event));
	}
	
	@PostConstruct
    public void subscribe() {
        createEventSubscriber = new EventSubscriber<ProjectCreatedEvent>() {
			@Override
			public void onEvent(ProjectCreatedEvent event) {
				fireConfigurationChanged(event);
			}
		};
		eventService.subscribe(createEventSubscriber);
        deleteEventSubscriber = new EventSubscriber<ProjectDeletedEvent>() {
			@Override
			public void onEvent(ProjectDeletedEvent event) {
				fireConfigurationChanged(event);
			}
		};
		eventService.subscribe(createEventSubscriber);
    }

    @PreDestroy
    public void unsubscribe() {
        eventService.unsubscribe(createEventSubscriber);
        eventService.unsubscribe(deleteEventSubscriber);
    }
}
