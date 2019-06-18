/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.keepout.server;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import net.keepout.KeepoutConstants;
import net.keepout.config.Config;
import net.powercoder.cdr.digest.Digester;
import net.powercoder.cdr.hex.HexCoder;
import net.powercoder.cdr.hex.HexStringCoder;
import org.jboss.logging.Logger;


/**
 *
 * @author juno
 */
public class ConnectionsHandler implements HttpHandler {
  
  private final Map<String,SocketChannel> channels;
  
  private final Config config;
  
  private final HttpHandler next;
  
  public ConnectionsHandler(Config cfg, HttpHandler next) {
    this.next = Objects.requireNonNull(next);
    this.channels = new ConcurrentHashMap<>();
    this.config = Objects.requireNonNull(cfg);
  }
  
  @Override
  public void handleRequest(HttpServerExchange hse) throws Exception {
    if(hse.isInIoThread()) {
      hse.dispatch(this);
      return;
    }
    //Logger lg = Logger.getLogger(getClass().getName());
    String target = getTargetString(hse);
    Optional<String> clid = getClientId(hse);
    if(target.isBlank() || clid.isEmpty()) {
      return;
    }
    String conHash = HexCoder.toHexString(
        Digester.toSHA256(target + clid.get())
    );
    if(Methods.DELETE.equals(hse.getRequestMethod()) && channels.containsKey(conHash)) {
      //lg.infof("Closing connection: %s", target);
      channels.remove(conHash).close();
      hse.setStatusCode(200).endExchange();
      return;
    }
    if(!Methods.POST.equals(hse.getRequestMethod())) return;
    SocketChannel sch;
    if(channels.containsKey(conHash)) {
      sch = channels.get(conHash);
    }
    else {
      //lg.infof("Opening connection: %s >> %s", conHash, target);
      sch = SocketChannel.open(parseAddress(target));
      channels.put(conHash, sch);
    }
    hse.putAttachment(KeepoutConstants.ATTACHMENT_SOCKET, sch);
    next.handleRequest(hse);
  }
  
  private String getTargetString(HttpServerExchange hse) {
    String target = config.getServer().getTarget().toString();
    if(hse.getRequestHeaders().contains(KeepoutConstants.HEADER_TARGET)) {
      HexStringCoder hex = new HexStringCoder();
      String str = hex.decode(hse.getRequestHeaders().getFirst(KeepoutConstants.HEADER_TARGET));
      if(str != null && !str.isBlank() && str.contains(":")) {
        target = str;
      }
    }
    return target;
  }
  
  private Optional<String> getClientId(HttpServerExchange hse) {
    if(!hse.getRequestHeaders().contains(KeepoutConstants.HEADER_CLIENT_ID)) {
      badRequest(hse, "Client ID missing");
      return Optional.empty();
    }
    return Optional.of(hse.getRequestHeaders().getFirst(KeepoutConstants.HEADER_CLIENT_ID));
  }
  
  private InetSocketAddress parseAddress(String str) {
    String[] ss = str.split(":");
    return new InetSocketAddress(ss[0], Integer.parseInt(ss[1]));
  }
  
  private void badRequest(HttpServerExchange hse, String msg) {
    String reason = "Bad Request".concat(
        msg != null && !msg.isBlank() ? String.format(" (%s)", msg) : ""
    );
    hse.setStatusCode(400).setReasonPhrase(reason).endExchange();
  }
  
}
