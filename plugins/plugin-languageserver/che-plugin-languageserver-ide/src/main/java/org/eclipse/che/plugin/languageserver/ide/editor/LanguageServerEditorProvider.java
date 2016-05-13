package org.eclipse.che.plugin.languageserver.ide.editor;

import javax.inject.Inject;

import org.eclipse.che.ide.api.debug.HasBreakpointRenderer;
import org.eclipse.che.ide.api.editor.EditorPartPresenter;
import org.eclipse.che.ide.api.editor.EditorProvider;
import org.eclipse.che.ide.api.editor.defaulteditor.DefaultTextEditorProvider;
import org.eclipse.che.ide.api.notification.NotificationManager;

public class LanguageServerEditorProvider implements EditorProvider {

    @Inject
    public LanguageServerEditorProvider(final DefaultTextEditorProvider editorProvider,
                             final NotificationManager notificationManager) {
        this.editorProvider = editorProvider;
        this.notificationManager = notificationManager;
    }

    private final DefaultTextEditorProvider editorProvider;
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
        //FIXME
//        if (textEditor instanceof EmbeddedTextEditorPresenter) {
//            final EmbeddedTextEditorPresenter<?> editor = (EmbeddedTextEditorPresenter<?>)textEditor;
//            editor.initialize(new AutoSaveTextEditorConfiguration(), notificationManager);
//        }
        return textEditor;
    }
    
    HasBreakpointRenderer foo = null;
}
