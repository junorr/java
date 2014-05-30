package com.jpower.pstat;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.kohsuke.stapler.Stapler;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name="termControll")
@SessionScoped
public class WebAppMain implements ServletContextListener {
  
  private ServletContextEvent event;
  
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    this.event = servletContextEvent;
    Stapler.setRoot(servletContextEvent,new AjaxTerm(servletContextEvent.getServletContext()));
  }

  
  public void contextDestroyed(ServletContextEvent servletContextEvent) {}
  
  
  public String reconnect() {
    if(event == null) {
      HttpServletRequest req = (HttpServletRequest) FacesContext
          .getCurrentInstance().getExternalContext().getRequest();
      event = new ServletContextEvent(req.getServletContext());
    }
    Stapler.setRoot(event, new AjaxTerm(event.getServletContext()));
    return "new-term.xhtml";
  }
  
}
