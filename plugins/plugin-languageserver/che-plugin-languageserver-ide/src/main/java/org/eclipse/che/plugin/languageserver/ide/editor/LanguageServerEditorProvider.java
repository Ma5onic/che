package org.eclipse.che.plugin.languageserver.ide.editor;

import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.editor.EditorProvider;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.debug.HasBreakpointRenderer;
import org.eclipse.che.ide.jseditor.client.defaulteditor.DefaultEditorProvider;
import org.eclipse.che.ide.jseditor.client.editorconfig.AutoSaveTextEditorConfiguration;
import org.eclipse.che.ide.jseditor.client.texteditor.EmbeddedTextEditorPresenter;

import javax.inject.Inject;

public class LanguageServerEditorProvider implements EditorProvider {

    @Inject
    public LanguageServerEditorProvider(final DefaultEditorProvider editorProvider,
                             final NotificationManager notificationManager) {
        this.editorProvider = editorProvider;
        this.notificationManager = notificationManager;
    }

    private final DefaultEditorProvider editorProvider;
    private final NotificationManager   notificationManager;


    @Override
    public String getId() {
        return "LanguageServerEditor";
    }

    @Override
    public String getDescription() {
        return "Code Editor";
    }

    @Override
    public EditorPartPresenter getEditor() {
        final EditorPartPresenter textEditor = editorProvider.getEditor();
        if (textEditor instanceof EmbeddedTextEditorPresenter) {
            final EmbeddedTextEditorPresenter<?> editor = (EmbeddedTextEditorPresenter<?>)textEditor;
            editor.initialize(new AutoSaveTextEditorConfiguration(), notificationManager);
        }
        return textEditor;
    }
    
    HasBreakpointRenderer foo = null;
}
