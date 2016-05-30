package org.eclipse.che.plugin.languageserver.ide.inject;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

import org.eclipse.che.ide.api.extension.ExtensionGinModule;
import org.eclipse.che.plugin.languageserver.ide.editor.LanguageServerAnnotationModelFactory;

@ExtensionGinModule
public class LanguageServerGinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        install(new GinFactoryModuleBuilder().build(LanguageServerAnnotationModelFactory.class));
    }

}
