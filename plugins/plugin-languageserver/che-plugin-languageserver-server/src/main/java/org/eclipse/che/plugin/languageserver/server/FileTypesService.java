package org.eclipse.che.plugin.languageserver.server;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.che.plugin.languageserver.shared.FileTypeDTO;
import org.eclipse.che.plugin.languageserver.shared.dto.DtoServerImpls;
import org.eclipse.che.plugin.languageserver.shared.dto.DtoServerImpls.FileTypeDTOImpl;

import com.google.inject.Singleton;

@Singleton
@Path("languageserver/{ws-id}")
public class FileTypesService {
	
	@POST
	@Path("fileTypes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FileTypeDTO> getSupportedFileTypes() {
		List<FileTypeDTO> result = newArrayList();
		FileTypeDTOImpl type = DtoServerImpls.FileTypeDTOImpl.make();
		type.setId("Foo");
		type.setMimeTypes(newArrayList("text/foo"));
		type.setExtension("foo");
		result.add(type);
		return result;
	}
}
