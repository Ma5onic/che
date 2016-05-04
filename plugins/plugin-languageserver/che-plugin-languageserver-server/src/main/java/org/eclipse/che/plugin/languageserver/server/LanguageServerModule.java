package org.eclipse.che.plugin.languageserver.server;

import org.eclipse.che.inject.DynaModule;

import com.google.inject.AbstractModule;

@DynaModule
public class LanguageServerModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(FileTypesService.class);
	}
	
}

