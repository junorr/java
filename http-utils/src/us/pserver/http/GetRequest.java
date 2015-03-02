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

package us.pserver.http;

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

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 27/02/2015
 */
public class GetRequest extends RequestLine {
  
  public static final String 
      EQS = "=",
      COMMA = ";",
      SPACE = " ",
      DOTS = ":";
  
  
  private Map<String, String> pars;
  
  
  public GetRequest() {
    super();
    super.setMethod(Method.GET);
    pars = new HashMap<>();
  }
  
  
  public GetRequest(String address) {
    super(Method.GET, address);
    pars = new HashMap<>();
  }
  
  
  public GetRequest(String address, int port) {
    super(Method.GET, address, port);
    pars = new HashMap<>();
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
  
  
  public GetRequest query(String key, Object ... vals) {
    if(key != null && !key.trim().isEmpty()
        && vals != null && vals.length > 0) {
      StringBuilder sb = new StringBuilder();
      for(int i = 0; i < vals.length; i++) {
        pars.put(key.concat(DOTS).concat(String.valueOf(i)), Objects.toString(vals[i]));
        sb.append(vals[i]);
        if(i < vals.length -1)
          sb.append(COMMA);
      }
      pars.put(key, sb.toString());
    }
    return this;
  }
  
  
  public String queryGet(String key, int idx) {
    if(key == null) return null;
    String val = pars.get(key.concat(DOTS).concat(String.valueOf(idx)));
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
        .filter(k->k.startsWith(key.concat(DOTS))).count();
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
    GetRequest get = new GetRequest();
    if(request != null && !request.trim().isEmpty()) {
      if(request.startsWith("GET"))
        request = request.substring(3);
      if(request.contains(" HTTP/")) {
        int iv = request.indexOf(" HTTP/");
        request = request.substring(0, iv);
      }
      request = request.trim();
      int ie = request.indexOf(QUERY);
      if(ie < 0) {
        System.out.println("super.setAddress( "+ request+ " )");
        get.setAddress(request);
        return get;
      }
      get.setAddress(request.substring(0, ie));
      request = request.substring(ie + 1);
      get.queryMap().clear();
      String[] pairs = request.split(AMPERSAND);
      for(int i = 0; i < pairs.length; i++) {
        String pair = pairs[i];
        String k = dec(pair.split(EQS)[0]);
        String v = dec(pair.split(EQS)[1]);
        get.query(k, v);
        if(v.contains(COMMA)) {
          String[] vals = v.split(COMMA);
          for(int j = 0; j < vals.length; j++) {
            get.query(k.concat(DOTS).concat(String.valueOf(j)), vals[j]);
          }
        }
      }
    }
    return get;
  }
  
  
  public static String enc(String str) {
    if(str == null || str.trim().isEmpty())
      return str;
    try {
      return URLEncoder.encode(str, "UTF-8");
    } catch(UnsupportedEncodingException e) {
      return null;
    }
  }
  
  
  public static String dec(String str) {
    if(str == null || str.trim().isEmpty())
      return str;
    try {
      return URLDecoder.decode(str, "UTF-8");
    } catch(UnsupportedEncodingException e) {
      return null;
    }
  }
  
  
  public String build() {
    return toString();
  }
  
  
  @Override
  public String toString() {
    if(pars.isEmpty()) 
      return super.toString();
    StringBuilder sb = new StringBuilder();
    sb.append(super.getMethod())
        .append(BLANK)
        .append(super.getFullAddress());
    
    boolean first = true;
    Iterator<String> keys = pars.keySet().iterator();
    while(keys.hasNext()) {
      String k = keys.next();
      if(k.contains(DOTS)) continue;
      String v = pars.get(k);
      if(first) {
        first = false;
        sb.append(QUERY);
      } else {
        sb.append(AMPERSAND);
      }
      sb.append(enc(k)).append(EQS).append(enc(v));
    }
    sb.append(BLANK).append(httpVersion).append(CRLF);
    return sb.toString();
  }

  
  public static void main(String[] args) {
    GetRequest req = new GetRequest("localhost", 8080)
        .query("obj", "a")
        .query("meth", "compute")
        .query("types", "int", "String")
        .query("args", 15, "some string");
    System.out.println("* request--> "+ req.build());
    req = GetRequest.parse(req.build());
    System.out.println("* parse--> "+ req.queryMap());
    System.out.println("* request--> "+ req.build());
  }
  
}
