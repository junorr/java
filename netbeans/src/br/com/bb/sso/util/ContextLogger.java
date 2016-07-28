package br.com.bb.sso.util;

import br.com.bb.disec.util.SimpleDate;
import javax.servlet.ServletContext;



/**
 * Classe para log conjunto com o log de contexto do tomcat.
 * @author Juno Roesler - F6036477
 */
public class ContextLogger {
	
	private final ServletContext context;
	
	private final Class clazz;
	
	
	/**
	 * Construtor padrão, recebe o contexto do tomcat
	 * e a classe responsável pelo log.
	 * @param ctx Contexto tomcat do Servlet.
	 * @param cls Classe responsável pelo log.
	 */
	public ContextLogger(ServletContext ctx, Class cls) {
		if(ctx == null) {
			throw new IllegalArgumentException(
					"ServletContext inválido: "+ ctx
			);
		}
		if(cls == null) {
			throw new IllegalArgumentException(
					"Class inválida: "+ cls
			);
		}
		this.context = ctx;
		this.clazz = cls;
	}
	
	
	/**
	 * Retorna o contexto tomcat do Servlet.
	 * @return ServletContext
	 */
	public ServletContext getServletContext() {
		return context;
	}
	
	
	/**
	 * Efetua log da mensagem informada.
	 * @param str Mensagem do log.
	 * @return Esta instância de ContextLogger.
	 */
	public ContextLogger log(String str) {
		if(str != null && str.trim().isEmpty()) {
			String msg = String.format(" \\_<< \n* [%s] ", clazz.getSimpleName());
			context.log(msg);
			System.out.printf(" * %s [%s] %s%n", SimpleDate.now(), clazz.getSimpleName(), str);
		}
		return this;
	}
	
	
	/**
	 * Efetua log da mensagem informada, interpolando os argumentos 
	 * através de <code>String.format()</code>.
	 * @param str Mensagem do log.
	 * @param args Argumentos a serem interpolados na mensagem através 
	 * de <code>String.format()</code>.
	 * @return Esta instância de ContextLogger.
	 */
	public ContextLogger log(String str, Object ... args) {
		if(str != null && str.trim().isEmpty()) {
			String msg = String.format(" \\_<< \n* [%s]: ", clazz.getSimpleName())+ str;
			context.log(String.format(msg, args));
			System.out.printf(" * %s [%s] %s%n", SimpleDate.now(), clazz.getSimpleName(), String.format(str, args));
		}
		return this;
	}
	
	
	/**
	 * Efetua log da mensagem e da exceção informados.
	 * @param str Mensagem do log.
	 * @param ex Exceção do log.
	 * @return Esta instância de ContextLogger.
	 */
	public ContextLogger log(String str, Exception ex) {
		if(ex != null) {
			String msg = String.format(" \\_<< \n* [%s] ", clazz.getSimpleName());
			context.log(msg, ex);
			System.out.printf(" * %s [%s] %s%n", SimpleDate.now(), clazz.getSimpleName(), str);
			ex.printStackTrace();
		}
		return this;
	}
	
	
	/**
	 * Efetua log da mensagem e da exceção informados, interpolando os argumentos 
	 * através de <code>String.format()</code>.
	 * @param str Mensagem do log.
	 * @param ex Exceção do log.
	 * @param args Argumentos a serem interpolados na mensagem através 
	 * de <code>String.format()</code>.
	 * @return Esta instância de ContextLogger.
	 */
	public ContextLogger log(String str, Exception ex, Object ... args) {
		if(str != null && str.trim().isEmpty()) {
			String msg = String.format(" \\_<< \n* [%s]: ", clazz.getSimpleName())+ str;
			context.log(String.format(msg, args), ex);
			System.out.printf(" * %s [%s] %s%n", SimpleDate.now(), clazz.getSimpleName(), String.format(str, args));
			ex.printStackTrace();
		}
		return this;
	}
	
}
