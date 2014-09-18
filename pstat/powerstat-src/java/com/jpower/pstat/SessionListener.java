/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jpower.pstat;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;



/**
 *
 * @author juno
 */
@WebListener
public class SessionListener implements HttpSessionListener {
  
  public SessionListener() {}

  @Override public void sessionCreated(HttpSessionEvent hse) {}


  @Override
  public void sessionDestroyed(HttpSessionEvent hse) {
    SessionController.destroy(hse.getSession());
  }
  
}
