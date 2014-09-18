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

package com.jpower.inet;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.log.Log;
import us.pserver.log.LogProvider;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 22/07/2013
 */
public class Connector {
  
  public static final String HTTP_REFRESH = "http-equiv=\"refresh\"";
  
  public static final String HTTP_URL = "content=\"0;url=";
  
  public static final String HTTP_SET_COOKIE = "Set-Cookie";
  
  public static final String HTTP_COOKIE = "Cookie";
  

  private String address;
  
  private HttpClient client;
  
  private String response;
  
  private HttpResponse httpResp;
  
  private String gotoUrl, encGoto;
  
  private List<NameValuePair> formparams;
  
  private Header cookies;
  
  private Log log;
  
  
  public Connector(Log l) {
    address = null;
    client = new DefaultHttpClient();
    try {
      SSLContext context = SSLContext.getInstance("TLS");
      context.init(null, new TrustManager[] {
        new CertificateManager()}, null);
      SSLSocketFactory fact = new SSLSocketFactory(context, 
          SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
      ClientConnectionManager cm = client.getConnectionManager();
      SchemeRegistry sr = cm.getSchemeRegistry();
      sr.register(new Scheme("https", fact, 443));
    } catch (Exception ex) {
      log.fatal(ex.toString());
    }
    response = null;
    formparams = new LinkedList<>();
    cookies = null;
    gotoUrl = null;
    encGoto = null;
    log = l;
  }
  
  
  public Connector() {
    this(LogProvider.getSimpleLog());
  }
  
  
  public Connector(String address) {
    this();
    this.address = address;
  }
  
  
  public Connector(String address, Log l) {
    this(l);
    this.address = address;
  }
  
  
  public Log getLog() {
    return log;
  }
  
  
  public Connector setLog(Log l) {
    if(l != null)
      log = l;
    return this;
  }
  
  
  public String getAddress() {
    return address;
  }
  
  
  public Connector setAddress(String addr) {
    this.address = addr;
    return this;
  }
  
  
  public Connector setAddress(URIBuilder uri) {
    if(uri == null) return this;
    try {
      address = uri.build().toString();
    } catch(URISyntaxException ex) {}
    return this;
  }
  
  
  public Connector setGotoURL(String url) {
    if(url != null) {
      try {
        gotoUrl = url;
        URIBuilder ub = new URIBuilder(address)
            .setParameter("goto", url)
            .setParameter("gx_charset", "UTF-8");
        address = ub.build().toString();
      } catch(URISyntaxException ex) {}
      encGoto = new Base64StringCoder().encode(url);
    }
    return this;
  }
  
  
  public String getResponse() {
    return response;
  }
  
  
  public HttpResponse getHttpResponse() {
    return httpResp;
  }
  
  
  private void connect(HttpUriRequest req) throws IOException {
    if(req == null) return;
    log.info(req.getRequestLine().toString());
    if(cookies != null) {
      req.addHeader(cookies);
      cookies = null;
    }
    httpResp = client.execute(req);
    log.info(httpResp.getStatusLine().toString());
    this.evaluateResponse();
  }
  
  
  public Connector setCredentials(String user, String password) {
    if(user != null && password != null) {
      formparams.add(new BasicNameValuePair("IDToken0", ""));
      formparams.add(new BasicNameValuePair("IDToken1", user));
      formparams.add(new BasicNameValuePair("IDToken2", password));
      formparams.add(new BasicNameValuePair("IDButton", "ENTRAR"));
      formparams.add(new BasicNameValuePair("encoded", "true"));
      formparams.add(new BasicNameValuePair("goto", encGoto));
      formparams.add(new BasicNameValuePair("gotoOnFail", ""));
      formparams.add(new BasicNameValuePair("gx_charset", "UTF-8"));
    }
    return this;
  }
  
  
  public void authenticate() {
    if(formparams.isEmpty()) return;
    HttpPost post = new HttpPost(address);
    log.info("Authenticating...");
    try {
      UrlEncodedFormEntity ent = new UrlEncodedFormEntity(formparams);
      post.setEntity(ent);
      this.connect(post);
    } catch(IOException ex) {
      log.error(ex.toString());
    }
  }
  
  
  public void evaluateResponse() throws IOException {
    if(httpResp == null) return;
    response = EntityUtils.toString(httpResp.getEntity());
    if(response == null) return;
    
    this.setCookies();
    
    if(response.toLowerCase().contains(HTTP_REFRESH)
        && response.toLowerCase().contains(HTTP_URL)) {
      address = this.getRefreshURL();
      if(address.startsWith("/"))
        address = "http://intranet.bb.com.br" + address;
      log.info("Meta-refresh: "+ address);
      HttpGet get = new HttpGet(address);
      this.connect(get);
    }
    else if(httpResp.getStatusLine().getStatusCode() == 302
        && httpResp.containsHeader("Location")) {
      address = httpResp.getFirstHeader("Location").getValue();
      log.info("302-Redirect: "+ address);
      if(address.equals(gotoUrl)) {
        HttpGet get = new HttpGet(address);
        this.connect(get);
      } else 
        this.authenticate();
    }
  }
  
  
  private void setCookies() {
    if(!httpResp.containsHeader(HTTP_SET_COOKIE)) return;
    Header[] cks = httpResp.getHeaders(HTTP_SET_COOKIE);
    String cookie = "";
    for(int i = 0; i < cks.length; i++) {
      String value = cks[i].getValue();
      value = value.substring(0, value.indexOf("; ") + 2);
      cookie += value;
    }
    cookies = new BasicHeader(HTTP_COOKIE, cookie);
  }
  
  
  private String getRefreshURL() {
    if(response == null) return null;
    String lresp = response.toLowerCase();
    int iurl = lresp.indexOf(HTTP_URL);
    if(iurl < 0) return null;
    int iend = lresp.indexOf("\"", iurl + HTTP_URL.length() + 2);
    if(iend < 0) iend = lresp.indexOf("'", 
          iurl + HTTP_URL.length() + 2);
    return response.substring(iurl + HTTP_URL.length(), iend);
  }
  
  
  public static void main(String[] args) {
    Connector con = new Connector();
    
    con.setAddress("https://login.intranet.bb.com.br/distAuth/UI/Login")
        .setGotoURL("http://portal.intranet.bb.com.br/wps/myportal/intranet")
        //.setGotoURL("http://172.24.75.19/dineg-wilson")
        .setCredentials("f6036477", "98765498")
        .authenticate();
    System.out.println(con.getResponse());
  }
  
}
