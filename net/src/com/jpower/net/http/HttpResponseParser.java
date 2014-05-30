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

package com.jpower.net.http;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 19/07/2013
 */
public class HttpResponseParser {

  public static final String NL = "\n";
  
  public static final String COOKIE = "Cookie";
  
  public static final String SET_COOKIE = "Set-Cookie";
  
  
  private HttpStatus status;
  
  private String content;
  
  private LinkedList<HeaderElement> elts;
  
  
  public HttpResponseParser() {
    status = null;
    elts = new LinkedList<>();
  }
  
  
  public HeaderElement getHeader(String name) {
    if(name == null || elts.isEmpty())
      return null;
    for(HeaderElement he : elts) {
      if(he.getName().equals(name))
        return he;
    }
    return null;
  }
  
  
  public boolean containsHeader(String name) {
    return getHeader(name) != null;
  }
  
  
  public List<HeaderElement> getHeaders() {
    return elts;
  }
  
  
  public List<Cookie> getCookies() {
    LinkedList<Cookie> cks = new LinkedList<>();
    for(HeaderElement he : elts) {
      if(he instanceof CookieHeader) {
        CookieHeader ch = (CookieHeader) he;
        for(Cookie c : ch.getCookies())
          cks.add(c);
      }
    }
    return cks;
  }
  
  
  public HttpStatus getStatus() {
    return status;
  }
  
  
  public String getContent() {
    return content;
  }
  
  
  public void parse(ByteBuffer buf) {
    if(buf == null || buf.limit() < 1)
      return;
    byte[] bs = new byte[buf.limit()];
    buf.get(bs);
    this.parse(new String(bs));
  }
  
  
  public void parse(String resp) {
    if(resp == null || !resp.contains(NL))
      return;
    content = resp;
    String[] lns = content.split(NL);
    status = new HttpStatus(lns[0]);
    for(int i = 1; i < lns.length; i++) {
      if(lns[i].startsWith(COOKIE)
          || lns[i].startsWith(SET_COOKIE))
        elts.add(new CookieHeader(lns[i]));
      else if(lns[i].startsWith(HtmlTag.OPEN1)) {
        elts.add(new HtmlTag(resp.substring(
            resp.indexOf(lns[i]))));
        break;
      } else {
        HeaderElement he = new HeaderElement(lns[i]);
        if(he.getName() != null)
          elts.add(new HeaderElement(lns[i]));
      }
    }
    
  }
  
  
  public static void main(String[] args) {
    String resp = "HTTP/1.1 200 OK\n" +
        "Date: Wed, 17 Jul 2013 12:52:06 GMT\n" +
        "Server: IBM_HTTP_Server\n" +
        "Cache-Control: no-cache, no-store, must-revalidate\n" +
        "IBM-Web2-Location: /wps/myportal/intranet/Home/intranet/!ut/p/b1/04_SjzSxMDI3MTU1MdKP0I_KSyzLTE8syczPS8wB8aPM4o0M_A0MLPyCjSwtfUwMPM39fLz9DbyMDQxMgQoigQoMcABHA0L6w_WjwEpMDLwNDNw9XYxCzNzcDDyNLY1NfVwMDQw8DaAK8Fjh55Gfm6qfG5Vj6anrqAgAJTWtaA!!/dl4/d5/L2dBISEvZ0FBIS9nQSEh/\n" +
        "Content-Location: /wps/myportal/intranet/Home/intranet/!ut/p/b1/04_SjzSxMDI3MTU1MdKP0I_KSyzLTE8syczPS8wB8aPM4o0M_A0MLPyCjSwtfUwMPM39fLz9DbyMDQxMgQoigQoMcABHA0L6w_WjwEpMDLwNDNw9XYxCzNzcDDyNLY1NfVwMDQw8DaAK8Fjh55Gfm6qfG5Vj6anrqAgAJTWtaA!!/dl4/d5/L2dBISEvZ0FBIS9nQSEh/\n" +
        "Expires: Thu, 01 Jan 1970 00:00:00 GMT\n" +
        "Pragma: no-cache\n" +
        "Vary: User-Agent,Cookie\n" +
        "Keep-Alive: timeout=5\n" +
        "Connection: Keep-Alive\n" +
        "Transfer-Encoding: chunked\n" +
        "Content-Type: text/html; charset=UTF-8\n" +
        "Content-Language: pt-BR\n\n" +
        "<html>"
        + "<head>"
        + "<title>Hello</title>"
        + "</head>"
        + "<body>"
        + "<div width='100%' style='color: gray;'>"
        + "<span>Hello</span>"
        + "</div>"
        + "</body>"
        + "</html>";
    HttpResponseParser rp = new HttpResponseParser();
    rp.parse(resp);
    List<HeaderElement> elt = rp.getHeaders();
    System.out.println(rp.getStatus());
    for(HeaderElement he : elt)
      System.out.println(he);
  }
  
}
