/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.server;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import java.util.Objects;
import net.keepout.KeepoutConstants;
import net.keepout.config.Config;
import org.jboss.logging.Logger;


/**
 *
 * @author juno
 */
public class AuthHandler implements HttpHandler {
  
  private final Config config;
  
  private final HttpHandler next;
  
  public AuthHandler(Config cfg, HttpHandler next) {
    this.config = Objects.requireNonNull(cfg);
    this.next = Objects.requireNonNull(next);
  }
  
  private boolean isAuthorized(Cookie ck) {
    return config.getTokens().stream().anyMatch(t -> t.equals(ck.getValue()));
  }

  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    Cookie ck = hse.getRequestCookies().get(KeepoutConstants.AUTH_COOKIE);
    Logger lg = Logger.getLogger(getClass().getName());
    lg.infof("Connection received: %s >> %s %s", hse.getSourceAddress(), hse.getRequestMethod(), hse.getRequestURI());
    if(ck == null || !isAuthorized(ck)) {
      hse.setStatusCode(401)
          .setReasonPhrase("Unauthorized")
          .endExchange();
      return;
    }
    lg.infof("Connection authorized: %s", ck.getValue());
    next.handleRequest(hse);
  }
  
}
