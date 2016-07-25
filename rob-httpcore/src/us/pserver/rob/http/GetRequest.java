/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou qualquer
 * versão posterior.
 * 
 * Esta biblioteca é distribuída na expectativa de que seja útil, porém, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE
 * OU ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA. Consulte a Licença Pública
 * Geral Menor do GNU para mais detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto
 * com esta biblioteca; se não, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endereço 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.rob.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import static us.pserver.rob.http.GetConsts.METHOD;
import static us.pserver.rob.http.HttpConsts.*;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 27/02/2015
 */
public class GetRequest implements RequestLine {
  
  
  private Map<String, String> pars;
  
  private String address;
  
  private String path;
  
  private String proto;
  
  
  public GetRequest(String address) {
    this(address, 0);
  }
  
  
  public GetRequest(String address, int port) {
    if(address == null || address.trim().isEmpty())
      throw new IllegalArgumentException(
          "[GetRequest( String )] Invalid address {address="+ address+ "]");
    pars = new HashMap<>();
    proto = HTTP;
    setAddress(address, port);
  }
  
  
  public Map<String, String> queryMap() {
    return pars;
  }
  
  
  public GetRequest query(String key, Object value) {
    if(key != null && !key.trim().isEmpty()
        && value != null) {
      pars.put(key, Objects.toString(value));
    }
    return this;
  }
  
  
  public GetRequest queryList(String key, Object ... vals) {
    if(key != null && !key.trim().isEmpty()
        && vals != null && vals.length > 0) {
      StringBuilder sb = new StringBuilder();
      for(int i = 0; i < vals.length; i++) {
        pars.put(key.concat(COLON).concat(String.valueOf(i)), Objects.toString(vals[i]));
        sb.append(vals[i]);
        if(i < vals.length -1)
          sb.append(SEMICOLON);
      }
      pars.put(key, sb.toString());
    }
    return this;
  }
  
  
  public String queryGet(String key, int idx) {
    if(key == null) return null;
    String val = pars.get(key.concat(COLON).concat(String.valueOf(idx)));
    if(val == null && pars.containsKey(key))
      val = pars.get(key);
    return val;
  }
  
  
  public String queryGet(String key) {
    if(key == null) return null;
    return pars.get(key);
  }
  
  
  public boolean queryContains(String key) {
    return pars.containsKey(key);
  }
  
  
  public int queryKeySize(String key) {
    if(pars.isEmpty() || !queryContains(key))
      return 0;
    int size = (int) pars.keySet().stream()
        .filter(k->k.startsWith(key.concat(COLON))).count();
    if(size == 0) return 1;
    return size;
  }
  
  
  public List<String> queryGetList(String key) {
    List<String> ls = Collections.EMPTY_LIST;
    int size = queryKeySize(key);
    if(size < 1) return ls;
    ls = new ArrayList<>();
    for(int i = 0; i < size; i++) {
      ls.add(queryGet(key, i));
    }
    return ls;
  }
  
  
  public static GetRequest parse(String request) {
    GetRequest get = null;
    if(request != null && !request.trim().isEmpty()) {
      if(request.startsWith(GET))
        request = request.substring(3);
      if(request.contains(" HTTP/")) {
        int iv = request.indexOf(" HTTP/");
        request = request.substring(0, iv);
      }
      request = request.trim();
      int ie = request.indexOf(QUERY);
      if(ie < 0) {
        get = new GetRequest(request);
        return get;
      }
      get = new GetRequest(request.substring(0, ie));
      request = request.substring(ie + 1);
      get.queryMap().clear();
      String[] pairs = request.split(AMPERSAND);
      for(int i = 0; i < pairs.length; i++) {
        String pair = pairs[i];
        String k = dec(pair.split(EQ)[0]);
        String v = dec(pair.split(EQ)[1]);
        get.query(k, v);
        if(v.contains(SEMICOLON)) {
          get.queryMap().remove(k);
          String[] vals = v.split(SEMICOLON);
          get.queryList(k, vals);
        }
      }
    }
    return get;
  }
  
  
  public static String enc(String str) {
    if(str == null || str.trim().isEmpty())
      return str;
    try {
      return URLEncoder.encode(str, UTF8);
    } catch(UnsupportedEncodingException e) {
      return null;
    }
  }
  
  
  public static String dec(String str) {
    if(str == null || str.trim().isEmpty())
      return str;
    try {
      return URLDecoder.decode(str, UTF8);
    } catch(UnsupportedEncodingException e) {
      return null;
    }
  }
  
  
  /**
   * Retorna o endereço sem o protocolo e sem o caminho 
   * adicional (Ex: https://<b>localhost:8080</b>/post/).
   * @return endereço sem o protocolo e sem o caminho 
   * adicional (Ex: https://<b>localhost:8080</b>/post/).
   */
  public String getAddress() {
    return address;
  }
  
  
  /**
   * Define o endereço do servidor.
   * @param addr <code>String</code>.
   * @return Esta instância modificada de <code>RequestLine</code>.
   */
  public GetRequest setAddress(String addr) {
    if(addr == null) 
      throw new IllegalArgumentException("Invalid address ["+ addr+ "]");
    
    if(addr.startsWith("http")) {
      address = addr.substring(addr.indexOf(COLON)+3);
      proto = addr.substring(0, addr.indexOf(COLON)+3);
    }
    else {
      address = addr;
    }
    
    if(address.contains(SLASH)) {
      int is = address.indexOf(SLASH);
      path = address.substring(is);
      address = address.substring(0, is);
    } 
    else {
      path = SLASH;
    }
    return this;
  }
  
  
  /**
   * Define o endereço e porta do servidor.
   * @param address <code>String</code>
   * @param port <code>int</code>
   * @return Esta instância modificada de <code>RequestLine</code>.
   */
  public GetRequest setAddress(String address, int port) {
    if(port <= 0)
      return setAddress(address);
    else {
      if(address.endsWith(SLASH))
        address = address.substring(0, address.length() -1);
      return setAddress(address + COLON + String.valueOf(port));
    }
  }
  
  
  /**
   * Retorna o protocolo do endereço (Ex: <b>https://</b>localhost:8080/post/).
   * @return protocolo do endereço (Ex: <b>https://</b>localhost:8080/post/).
   */
  public String getProtocol() {
    return proto;
  }
  
  
  /**
   * Retorna o caminho do endereço (Ex: https://localhost:8080<b>/post/</b>).
   * @return caminho do endereço (Ex: https://localhost:8080<b>/post/</b>).
   */
  public String getPath() {
    return path;
  }
  
  
  public String getFullAddress() {
    if(address == null || address.trim().isEmpty()
        && path != null) 
      return path;
    else 
      return proto + address + path;
  }
  
  
  public String buildRequest() {
    if(pars.isEmpty()) 
      return getFullAddress();
    StringBuilder sb = new StringBuilder();
    sb.append(GET)
        .append(SP)
        .append(getFullAddress());
    
    boolean first = true;
    Iterator<String> keys = pars.keySet().iterator();
    while(keys.hasNext()) {
      String k = keys.next();
      if(k.contains(COLON)) continue;
      String v = pars.get(k);
      if(first) {
        first = false;
        sb.append(QUERY);
      } else {
        sb.append(AMPERSAND);
      }
      sb.append(enc(k)).append(EQ).append(enc(v));
    }
    sb.append(SP).append(HttpVersion.HTTP_1_1).append(CRLF);
    return sb.toString();
  }
  
  
  public String buildQueryAddress() {
    if(pars.isEmpty()) 
      return super.toString();
    StringBuilder sb = new StringBuilder();
    sb.append(getFullAddress());
    
    boolean first = true;
    Iterator<String> keys = pars.keySet().iterator();
    while(keys.hasNext()) {
      String k = keys.next();
      if(k.contains(COLON)) continue;
      String v = pars.get(k);
      if(first) {
        first = false;
        sb.append(QUERY);
      } else {
        sb.append(AMPERSAND);
      }
      sb.append(enc(k)).append(EQ).append(enc(v));
    }
    return sb.toString();
  }
  
  
  @Override
  public String toString() {
    return buildRequest();
  }

  
  public static void main(String[] args) {
    GetRequest req = new GetRequest("localhost", 8080)
        .query("obj", "a")
        .query("meth", "compute")
        .queryList("types", "int", "String")
        .queryList("args", 15, "some string");
    System.out.println("* request--> "+ req.buildRequest());
    req = GetRequest.parse(req.buildRequest());
    System.out.println("* parse--> "+ req.queryMap());
    System.out.println("* request--> "+ req.buildRequest());
    System.out.println("* uri--> "+ req.getUri());
  }


  @Override
  public String getMethod() {
    return GET;
  }


  @Override
  public ProtocolVersion getProtocolVersion() {
    return HttpVersion.HTTP_1_1;
  }


  @Override
  public String getUri() {
    return buildQueryAddress();
  }
  
}
