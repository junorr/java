/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bb.disec.aplic.servlet;

import br.com.bb.disec.util.ServletRedirect;
import java.io.IOException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet base (opcional) a ser implementado como controlador.
 * @see br.com.bb.disec.servlet.BaseServlet#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) 
 * @author Juno Roesler - F6036477
 */
public abstract class BaseServlet extends GenericServlet {
	
	
	/**
	 * Método a ser implementado para tratar e 
	 * redirecionar a requisição para outros recursos 
	 * no servidor. Este método deve retornar uma string
	 * indicando o nome do recurso (página html/jsp ou Servlet)
	 * para o qual a requisição será redirecionada. No caso 
	 * de não haver redirecionamento, o método deve retornar NULL.
	 * @param req Requisição HTTP.
	 * @param resp Resposta HTTP.
	 * @return Nome do recurso (página html/jsp ou Servlet)
	 * para o qual a requisição será redirecionada.
	 * @throws ServletException em caso de erro no tratamento 
	 * da requisição.
	 */
	public abstract String perform(
			HttpServletRequest req, 
			HttpServletResponse resp
	) throws ServletException;

	
	/**
	 * Implementação padrão, não deve ser sobrescrito.
	 * @param req ServletRequest
	 * @param resp ServletResponse
	 * @throws ServletException
	 * @throws IOException 
	 */
	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
		String dest = this.perform(
				(HttpServletRequest)req, 
				(HttpServletResponse)resp
		);
		if(dest != null) {
			ServletRedirect.from(req, resp)
					.serverForward(dest);
		}
	}
	
}
