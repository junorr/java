package br.com.bb.disec.micro.sso;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;



/**
 * Conector HTTP para recuperar as informações do
 * usuario do servidor de autenticação SSO.
 * @author Juno Roesler - F6036477
 */
public class SSOConnector {
	
	private final String server;
	
	private final String token;
	
	private String response;
	
	
	/**
	 * Construtor padrão que recebe o endereço do servidor SSO
	 * e o token de autenticação.
	 * @param ssoServer Endereço do servidor SSO.
	 * @param tokenId Token de autenticação SSO.
	 */
	public SSOConnector(String ssoServer, String tokenId) {
		if(ssoServer == null || ssoServer.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Servidor SSO invalido: "+ ssoServer
			);
		}
		if(tokenId == null || tokenId.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"SSO Token ID invalido: "+ tokenId
			);
		}
		this.server = ssoServer;
		this.token = tokenId;
		response = null;
	}
	
	
	/**
	 * Retorna o endereço do servidor SSO.
	 * @return String
	 */
	public String getSSOServer() {
		return server;
	}
	
	
	/**
	 * Retorna o token de autenticação SSO.
	 * @return String
	 */
	public String getSSOTokenId() {
		return token;
	}
	
	
	/**
	 * Retorna a resposta do servidor SSO como String.
	 * @return String
	 */
	public String getResponseString() {
		return response;
	}
	
	
	/**
	 * Cria uma URI para obter as informações do usuário
	 * a partir do endereço do servidor SSO.
	 * @return URI
	 * @throws URISyntaxException Em caso de erro na
	 * formação da URI
	 */
	public URI getURI() throws URISyntaxException {
		return new URIBuilder()
				.setScheme("http")
				.setHost(server)
				.setPath("/sso/identity/attributes")
				.setParameter("subjectid", token)
				.build();
	}
	
	
	/**
	 * Conecta no servidor SSO e retorna um objeto SSOParser
	 * com as informações recuperadas.
	 * @return SSOParser com as informações recuperadas.
	 * @throws IOException em caso de erro na conexão HTTP.
	 */
	public SSOParser connect() throws IOException {
		try {
			Response resp = Request.Get(getURI()).execute();
			HttpResponse hresp = resp.returnResponse();
			response = EntityUtils.toString(hresp.getEntity());
			return new SSOParser(response);
		} catch(URISyntaxException e) {
			throw new IOException(e);
		}
	}
	
}
