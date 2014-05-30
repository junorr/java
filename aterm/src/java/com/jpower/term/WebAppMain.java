/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.term;


import org.kohsuke.stapler.Stapler;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebAppMain implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Stapler.setRoot(servletContextEvent,new AjaxTerm(servletContextEvent.getServletContext()));
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
