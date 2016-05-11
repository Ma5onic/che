package org.eclipse.che.plugin.languageserver.server;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.che.plugin.languageserver.shared.lsapi.CompletionItemDTO;
import org.eclipse.che.plugin.languageserver.shared.lsapi.TextDocumentPositionParamsDTO;

import com.google.inject.Singleton;

@Singleton
@Path("languageserver/{ws-id}")
public class CompletionService {

	@GET
	@Path("textDocument/completion")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<CompletionItemDTO> completion(TextDocumentPositionParamsDTO textDocumentPositionParams) {
		List<CompletionItemDTO> result = null;
		return result;
	}
}
