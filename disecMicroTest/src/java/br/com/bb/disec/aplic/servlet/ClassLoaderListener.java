/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.bb.disec.aplic.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;



/**
 *
 * @author juno
 */
public class ClassLoaderListener implements ServletContextListener {
	
	public static final String CLASS_USER = "br.com.bb.sso.bean.User";

	public static final String CLASS_USUARIO = "br.com.bb.sso.bean.Usuario";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			new LibClassLoader()
					.load(CLASS_USER)
					.load(CLASS_USUARIO);
		} catch(ClassNotFoundException e) {
			System.out.println("  ### Error in ClassLoaderListener: "+ e.getMessage());
			e.printStackTrace(System.out);
		}
	}

	@Override 
	public void contextDestroyed(ServletContextEvent sce) {}
	
}
