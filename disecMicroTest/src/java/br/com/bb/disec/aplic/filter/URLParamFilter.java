/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bb.disec.aplic.filter;

import br.com.bb.disec.util.URLBarParameters;
import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;



/**
 *
 * @author juno
 */
public class URLParamFilter implements Filter {

	@Override	public void init(FilterConfig fc) throws ServletException {}
	@Override public void destroy() {}

	
	@Override
	public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) sr;
		List<String> list = URLBarParameters.of(req).parseURL();
		req.getSession();
		req.setAttribute("paramList", list);
		if(fc != null) {
			fc.doFilter(sr, sr1);
		}
	}

}
