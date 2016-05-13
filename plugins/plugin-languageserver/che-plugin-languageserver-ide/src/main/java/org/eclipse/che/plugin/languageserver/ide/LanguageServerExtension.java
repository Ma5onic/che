package org.eclipse.che.plugin.languageserver.ide;

import org.eclipse.che.ide.api.editor.EditorRegistry;
import org.eclipse.che.ide.api.extension.Extension;
import org.eclipse.che.ide.api.filetypes.FileType;
import org.eclipse.che.ide.api.filetypes.FileTypeRegistry;
import org.eclipse.che.plugin.languageserver.ide.editor.LanguageServerEditorProvider;

import com.google.inject.Inject;

@Extension(title = "LanguageServer")
public class LanguageServerExtension {

	@Inject
    protected void configureFileTypes(FileTypeRegistry fileTypeRegistry, LanguageServerResources resources, final EditorRegistry editorRegistry,
            final LanguageServerEditorProvider editorProvider) {
//FIXME    	FileType fileType = new FileType("Foo Lang", resources.file(), "text/foo", "foo");
//        fileTypeRegistry.registerFileType(fileType);
        // register editor provider
//        editorRegistry.registerDefaultEditor(fileType, editorProvider);
    }
}
