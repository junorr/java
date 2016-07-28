package br.com.bb.disec.util;

import java.io.IOException;
import java.io.InputStreamReader;



/**
 * Classe para recuperar o conteúdo String de um arquivo.
 * @author Juno Roesler - F6036477
 */
public class StringResource {
	
	private final String resource;
	
	private final StringBuilder content;
	
	
	/**
	 * Construtor padrão que recebe o caminho e nome do arquivo.
	 * @param resource caminho e nome do arquivo.
	 */
	public StringResource(String resource) {
		if(resource == null || resource.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Nome do recurso inválido: "+ resource
			);
		}
		this.resource = resource;
		content = new StringBuilder();
	}
	
	
	/**
	 * Retorna o caminho do arquivo.
	 * @return String
	 */
	public String getResource() {
		return this.resource;
	}
	
	
	/**
	 * Retorna o conteúdo String do arquivo.
	 * @return String
	 */
	public String getContent() {
		return this.content.toString();
	}
	
	
	/**
	 * Lê o conteúdo do arquivo.
	 * @return Esta instância modificada de StringResource.
	 * @throws IOException Em caso de erro lendo o arquivo.
	 */
	public StringResource readResource() throws IOException {
		InputStreamReader is = new InputStreamReader(this.getClass()
				.getResourceAsStream(resource), "UTF-8");
		if(is == null) {
			throw new IOException(
					"Recurso inexistente: "+ resource
			);
		}
		char[] cs = new char[1024];
		int read = 0;
		while((read = is.read(cs)) > 0) {
			this.content.append(new String(cs, 0, read));
		}
		return this;
	}
	
}
