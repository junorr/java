package br.com.bb.disec.micro.sso;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;



/**
 * Classe para interpretar as informações retornadas 
 * pelo servidor SSO na forma de um mapa com nome e 
 * valor das informações do usuário.
 * @author Juno Roesler - F6036477
 */
public class SSOParser {
	
	private final String response;
	
	private final Map<String,String> map;
	
	
	/**
	 * Construtor padrão que recebe a String de resposta 
	 * do servidor SSO.
	 * @param httpResponse String de resposta do servidor SSO.
	 */
	public SSOParser(String httpResponse) {
		if(httpResponse == null 
				|| httpResponse.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Resposta HTTP inválida: "+ httpResponse
			);
		}
		this.response = httpResponse;
		map = new HashMap<>();
	}
	
	
	/**
	 * Retorna a string de resposta do servidor SSO.
	 * @return String
	 */
	public String getHttpResponse() {
		return response;
	}
	
	
	/**
	 * Retorna o mapa com nome-valor das informações do 
	 * usuário recuperadas do servidor SSO.
	 * @return mapa com nome-valor das informações do 
	 * usuário
	 */
	public Map<String,String> getMap() {
		return map;
	}
	
	
	/**
	 * Interpretar as informações retornadas pelo servidor 
	 * SSO na forma de um mapa com nome e valor das 
	 * informações do usuário.
	 * @return Um novo objeto MappedUser com o mapa de 
	 * informações do usuário.
	 */
	public MappedUser parse() {
		int index1 = response.indexOf("userdetails.role=id=");
		int index2 = response.indexOf("userdetails.attribute.name=");
		if(index1 < 0 || index2 < 0) {
			return null;
		}
		String acessos = response.substring(index1, index2);
		String atributos = response.substring(index2, response.length());
		String splitAcessos[] = acessos.split("\n");
		StringTokenizer st = new StringTokenizer(atributos, "\n");
		while (st.hasMoreElements()) {
			try{
				String key = (String) st.nextElement();
				String value = (String) st.nextElement();
				if( value.startsWith("userdetails.attribute.name=") ){
					key = value;
					value = (String) st.nextElement();
				}
				map.put(key.substring(27, key.length()), value.substring(28, value.length()));
			} catch(NoSuchElementException nsee){
				throw new RuntimeException(nsee);
			}
		}
		String acessosUsu = "";
		for(String splitAcesso : splitAcessos) {
			acessosUsu += splitAcesso.substring(20, splitAcesso.indexOf(",")) + ",";
		}
		map.put("tokenId", response.substring(21, index1).trim());
		map.put("acessosUsu", acessosUsu);
		return new MappedUser(map);
	}
	
}
