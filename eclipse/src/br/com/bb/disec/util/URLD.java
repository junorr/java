package br.com.bb.disec.util;

import java.net.MalformedURLException;
import java.net.URL;



/**
 * URL Disassembler (Desmontador de URLs).
 * @author Juno Roesler - F6036477
 */
public class URLD {
	
	private final String url;
	
	private final String protocol;
	
	private final String host;
	
	private final int port;
	
	private final String context;
	
	private final String[] paths;
	
	
	/**
	 * Construtor padrão que recebe a URL.
	 * @param url url
	 */
	public URLD(String url) {
		if(url == null || url.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"URL inválida: "+ url
			);
		}
		this.url = url;
		URLI ui = disassemble();
		this.protocol = ui.protocol;
		this.host = ui.host;
		this.port = ui.port;
		this.context = ui.context;
		this.paths = ui.paths;
	}
	
	
	/**
	 * Método estático que retorna um novo 
	 * objeto URLD para a url informada.
	 * @param url url
	 * @return Novo objeto URLD
	 */
	public static URLD of(String url) {
		return new URLD(url);
	}


	/**
	 * Retorna a URL.
	 * @return String
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * Retorna o protocolo da URL (Ex: https).
	 * Exemplo:
	 * <pre>
	 * http://disec.intranet.bb.com.br/header
	 * ----  protocolo da URL
	 * </pre>
	 * @return String
	 */
	public String getProtocol() {
		return protocol;
	}


	/**
	 * Retorna o host da URL. 
	 * Exemplo:
	 * <pre>
	 * http://disec.intranet.bb.com.br/header
	 *        ------------------------  host da URL
	 * </pre>
	 * @return String
	 */
	public String getHost() {
		return host;
	}


	/**
	 * Retorna a porta da URL. 
	 * Exemplo:
	 * <pre>
	 * http://disec.intranet.bb.com.br:8080/header
	 *                                 ----  porta da URL
	 * </pre>
	 * @return int
	 */
	public int getPort() {
		return port;
	}


	/**
	 * Retorna o contexto da URL. 
	 * Exemplo:
	 * <pre>
	 * http://disec.intranet.bb.com.br/header
	 *                                 ------  contexto da URL
	 * </pre>
	 * @return String
	 */
	public String getContext() {
		return context;
	}


	/**
	 * Retorna os demais caminhos da URL. 
	 * Exemplo:
	 * <pre>
	 * http://disec.intranet.bb.com.br/header/testeira/testeira.jsp
	 *                                        ---------------------  caminhos da URL
	 * </pre>
	 * @return String[]
	 */
	public String[] getPaths() {
		return paths;
	}
	
	
	/**
	 * Separa as partes da URL.
	 * @return URLI
	 */
	private URLI disassemble() {
		URLI ui = new URLI();
		if(url == null) return ui;
		int iproto = url.indexOf("://");
		if(iproto < 0) return ui;
		int iport = url.indexOf(":", iproto + 3);
		int ictx = url.indexOf("/", iproto + 3);
		//protocol
		if(iproto > 0) {
			ui.protocol = url.substring(0, iproto);
		}
		//port
		if(iport > iproto) {
			String sport = url.substring(iport+1);
			if(ictx > 0) {
				sport = url.substring(iport+1, ictx);
			}
			ui.port = Integer.parseInt(sport);
		} else if(ui.protocol != null) {
			ui.port = portByProto(ui.protocol);
		}
		//host
		if(iproto > 0) {
			ui.host = url.substring(iproto+3);
			if(iport > iproto) {
				ui.host = url.substring(iproto+3, iport);
			} else if(ictx > iproto) {
				ui.host = url.substring(iproto+3, ictx);
			} else {
				ui.host = url.substring(iproto+3);
			}
		}
		int ipath = url.indexOf("/", ictx +1);
		//context
		if(ictx > iproto) {
			ui.context = url.substring(ictx+1);
			if(ipath > ictx) {
				ui.context = url.substring(ictx+1, ipath);
			}
		}
		//path
		if(iproto > 0 && ictx > iproto && ipath > ictx) {
			ui.paths = url.substring(ipath+1).split("/");
		}
		return ui;
	}
	
	
	private int portByProto(String proto) {
		int port = -1;
		if(proto != null) {
			switch(proto.toLowerCase()) {
				case "http":
					port = 80;
					break;
				case "https":
					port = 443;
					break;
				case "ftp":
					port = 21;
					break;
				case "ssh":
					port = 22;
					break;
				default:
					port = 0;
			}
		}
		return port;
	}
	
	
	public URL toURL() {
		try {
			return new URL(url);
		} catch(MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	@Override
	public String toString() {
		return url;
	}
	
	
	
	private class URLI {
		public String protocol;
		public String host;
		public String context;
		public String[] paths;
		public int port;
	}
	
}
