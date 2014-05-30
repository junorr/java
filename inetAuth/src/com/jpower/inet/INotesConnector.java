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
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
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
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import us.pserver.log.Log;
import us.pserver.log.LogProvider;
import us.pserver.log.SimpleLog;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 22/07/2013
 */
public class INotesConnector {
  
  public static final String HTTP_REFRESH = "http-equiv=\"refresh\"";
  
  public static final String HTTP_URL = "content=\"0;url=";
  
  public static final String HTTP_SET_COOKIE = "Set-Cookie";
  
  public static final String HTTP_COOKIE = "Cookie";
  
  public static final String HTTP_FORM = "<form";
  
  public static final String HTTP_FORM_ACTION = "action=";
  
  public static final String HTTP_INPUT_HIDDEN = "<input type=\"hidden\" name=\"%%ModDate\" value=\"";
  
  public static final String MOD_DATE = "%%ModDate";
  
  public static final String HTTP_JS_REDIRECT = "window.EFp='\"";
  
  public static final String HTTP_JS_INOTES = "AAA.s_StartupView='iNotes';";
  
  public static final String HTTP_ADDRESS_BASE = "https://correioweb.bb.com.br";
  
  public static final String HTTP_ADDRESS_DELETE = "/iNotes/Proxy/?EditDocument&Form=s_GetFormListJSON";
  
  public static final String HTTP_ADDRESS_DELETE2 = "/iNotes/Mail/?EditDocument&Form=l_HaikuErrorStatusJSON&ui=dwa_form";
  
  public static final String HTTP_ADDRESS_SEND = "/($Inbox)/$new/?EditDocument&Form=h_PageUI&PresetFields=h_EditAction;h_ShimmerEdit,s_ViewName;($Inbox),s_NotesForm;Memo&ui=dwa_form";
  
  public static final String HTTP_ADDRESS_GET_MAIL = "/($Inbox)/#MAILUID#/?OpenDocument&Form=l_JSVars&PresetFields=s_HandleAttachmentNames;1,s_HandleMime;1,s_OpenUI;1";
  
  public static final String HTTP_ADDRESS_GET_ATTACHMENT = "/0/#MAILUID#/$File/#FILENAME#?OpenElement&FileName=#FILENAME#";
  
  public static final String HTTP_ADDRESS_INBOX = "/iNotes/Proxy/?OpenDocument&Form=s_ReadViewEntries&PresetFields=DBQuotaInfo%3B1%2CFolderName%3B%28%24Inbox%29%2Cs_UsingHttps%3B1%2CnoPI%3B1%2Chc%3B%252498%257CSametimeInfo%2CUnreadCountInfo%3B1&TZType=UTC&Start=1&Count=20&resortdescending=1";
  
  public static final String HTTP_ADDRESS_AUTH = "/oamredirect.nsf/OAMAutoLogin";
  
  public static final String HTTP_COOKIE_INOTES = "COOKIE_CLDOMINO0901";
  
  public static final String HTTP_COOKIE_SESSION = "LtpaToken";
  
  public static final String HTTP_COOKIE_SHIMMER = "ShimmerS";
  
  public static final Header HEADER_USER_AGENT = new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0");
  
  public static final String POST_DELETE_MAIL = "h_SetDeleteList";
  
  public static final String TAG_MAIL_UID = "#MAILUID#";
  
  public static final String TAG_FILE_NAME = "#FILENAME#";
  
  public static final String HEADER_X_IBM_NOTES = "X-IBM-INOTES-NONCE";
  
  public static final String DELIM_COOKIE_X_IBM_NOTES = "M&N:";
  

  private String address;
  
  private HttpClient client;
  
  private String response;
  
  private HttpResponse httpResp;
  
  private List<NameValuePair> formparams;
  
  private Header cookies;
  
  private String sessionCookie, shimmerCookie;
  
  private String inotesCookie;
  
  private Log log;
  
  private String addressID;
  
  private String xIbmNotesCookie;
  
  private INotesXML ixml;
  
  private String user, passwd;
  
  private boolean autoDownloadAttachments;
  
  private Path tempDir;
  
  
  public INotesConnector(Log l) {
    address = HTTP_ADDRESS_BASE + HTTP_ADDRESS_AUTH;
    client = new DefaultHttpClient();
    try {
      SSLContext context = SSLContext.getInstance("TLS");
      context.init(null, new TrustManager[] {new CertificateManager()}, null);
      SSLSocketFactory fact = new SSLSocketFactory(context, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
      ClientConnectionManager cm = client.getConnectionManager();
      SchemeRegistry sr = cm.getSchemeRegistry();
      sr.register(new Scheme("https", fact, 443));
    } catch (Exception ex) {
      log.fatal(ex.toString());
    }
    response = null;
    formparams = new LinkedList<>();
    cookies = null;
    sessionCookie = null;
    inotesCookie = null;
    shimmerCookie = null;
    xIbmNotesCookie = null;
    addressID = null;
    user = null;
    passwd = null;
    autoDownloadAttachments = true;
    ixml = new INotesXML();
    log = l;
    try {
      tempDir = Files.createTempDirectory("INotesTemp");
    } catch (IOException ex) {}
  }
  
  
  public INotesConnector() {
    this(LogProvider.getSimpleLog());
  }
  
  
  public INotesConnector(String address) {
    this();
    this.address = address;
  }
  
  
  public INotesConnector(String address, Log l) {
    this(l);
    this.address = address;
  }
  
  
  public Log getLog() {
    return log;
  }
  
  
  public INotesConnector setLog(Log l) {
    if(l != null)
      log = l;
    return this;
  }
  
  
  public String getAddress() {
    return address;
  }
  
  
  public INotesConnector setAddress(String addr) {
    this.address = addr;
    return this;
  }
  
  
  public INotesConnector setAddress(URIBuilder uri) {
    if(uri == null) return this;
    try {
      address = uri.build().toString();
    } catch(URISyntaxException ex) {}
    return this;
  }
  
  
  public String getResponse() {
    return response;
  }
  
  
  public HttpResponse getHttpResponse() {
    return httpResp;
  }


  public boolean isAutoDownloadAttachments() {
    return autoDownloadAttachments;
  }


  public INotesConnector setAutoDownloadAttachments(boolean autoDownloadAttachments) {
    this.autoDownloadAttachments = autoDownloadAttachments;
    return this;
  }
  
  
  public INotesConnector setTempDir(String temp) {
    if(temp != null && !temp.trim().isEmpty()) {
      tempDir = Paths.get(temp);
      if(!Files.exists(tempDir)) {
        try {
          Files.createDirectories(tempDir);
        } catch (IOException ex) {}
      }
    }//if
    return this;
  }
  
  
  public void connect() {
    try {
      HttpGet get = new HttpGet(address);
      this.connect(get, true);
    } catch(IOException ex) {
      log.error(ex.toString());
    }
  }
  
  
  public void deleteMail(INotesHeaderMail header) {
    if(header == null || header.getUid() == null) 
      return;
    
    this.setCookies();
    
    formparams.clear();
    formparams.add(new BasicNameValuePair(
        POST_DELETE_MAIL, header.getUid()));
    address = HTTP_ADDRESS_BASE + addressID + HTTP_ADDRESS_DELETE;
    
    try {
      HttpPost post = new HttpPost(address);
      post.addHeader(HEADER_X_IBM_NOTES, xIbmNotesCookie);
      UrlEncodedFormEntity ent = new UrlEncodedFormEntity(formparams);
      post.setEntity(ent);
      
      this.connect(post, true);
      post = this.secondDeletePost(header.getUid());
      this.connect(post, true);
      
    } catch(IOException ex) {
      log.error(ex.toString());
    }
  }
  
  
  private HttpPost secondDeletePost(String listUids) throws IOException {
    if(listUids == null || listUids.trim().isEmpty()) 
      return null;
    
    formparams.clear();
    formparams.add(new BasicNameValuePair("h_EditAction", "h_Next"));
    formparams.add(new BasicNameValuePair("h_SetReturnURL", "%5B%5B.%2F%26Form%3Ds_CallBlankScript%5D%5D"));
    formparams.add(new BasicNameValuePair("s_ViewName", "(%24Inbox)"));
    formparams.add(new BasicNameValuePair("h_SetCommand", "h_DeletePages"));
    formparams.add(new BasicNameValuePair("h_SetEditNextScene", "l_HaikuErrorStatusJSON"));
    formparams.add(new BasicNameValuePair("h_SetDeleteList", listUids));
    
    address = HTTP_ADDRESS_BASE + addressID + HTTP_ADDRESS_DELETE2;
    HttpPost post = new HttpPost(address);
    post.addHeader(HEADER_X_IBM_NOTES, xIbmNotesCookie);
    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(formparams);
    post.setEntity(ent);
    return post;
  }
  
  
  public void sendMail(INotesMail mail) {
    if(mail == null) return;
    if(mail.attachmentsSize() > 0) {
      this.sendAttachMail(mail);
      return;
    }
    
    setCookies();
    mail.setxIbmCookie(xIbmNotesCookie);
    
    try {
      address = HTTP_ADDRESS_BASE + addressID + HTTP_ADDRESS_SEND;
      HttpPost post = new HttpPost(address);
      UrlEncodedFormEntity ent = new UrlEncodedFormEntity(mail.toFormParams());
      post.setEntity(ent);
      
      this.connect(post, true);
      
    } catch(IOException ex) {
      log.error(ex.toString());
    }
  }
  
  
  public void sendAttachMail(INotesMail mail) {
    if(mail == null) return;
    if(mail.attachmentsSize() == 0) {
      this.sendMail(mail);
      return;
    }
    
    setCookies();
    mail.setxIbmCookie(xIbmNotesCookie);
    
    try {
      address = HTTP_ADDRESS_BASE + addressID + HTTP_ADDRESS_SEND;
      HttpPost post = new HttpPost(address);
      MultipartEntity mult = new MultipartEntity(
          HttpMultipartMode.BROWSER_COMPATIBLE);
      
      List<NameValuePair> params = mail.toFormParams();
      for(NameValuePair p : params) {
        mult.addPart(p.getName(), new StringBody(p.getValue()));
      }
      
      int i = 0;
      for(; i < mail.attachmentsSize(); i++) {
        mult.addPart(INotesMail.ATTACHMENT_NAME+i, 
            new FileBody(mail.attachment(i).getFile()));
      }
      mult.addPart(INotesMail.ATTACHMENT_NAME+i, new ByteArrayBody(new byte[]{}, ""));
      
      post.setEntity(mult);
      this.connect(post, true);
      
    } catch(IOException ex) {
      log.error(ex.toString());
    }
  }
  
  
  public List<INotesHeaderMail> refreshInbox() {
    address = HTTP_ADDRESS_BASE + addressID + HTTP_ADDRESS_INBOX;
    this.connect();
    
    //Error, try to reconnect
    if(this.httpResp.getStatusLine().getStatusCode() != 200) {
      this.setAuthenticate();
      this.connect();
      return ixml.getMails();
    }
    
    return ixml.parseXML(response);
  }
  
  
  public List<INotesHeaderMail> getInbox() {
    return ixml.getMails();
  }
  
  
  private String setGetMailAddress(String uid) {
    if(uid == null) return null;
    int it = HTTP_ADDRESS_GET_MAIL.indexOf(TAG_MAIL_UID);
    return HTTP_ADDRESS_GET_MAIL.substring(0, it)
        + uid
        + HTTP_ADDRESS_GET_MAIL.substring(
        it + TAG_MAIL_UID.length());
  }
  
  
  public INotesMail getMail(INotesHeaderMail header) {
    if(header == null || header.getUid() == null)
      return null;
    
    address = HTTP_ADDRESS_BASE + addressID 
        + setGetMailAddress(header.getUid());
    
    try {
      HttpGet get = new HttpGet(address);
      this.connect(get, true);
      INotesMail mail = INotesMail.parse(response);
      if(mail != null) {
        mail.setxIbmCookie(xIbmNotesCookie);
        mail.setDate(header.getDate());
      }
      
      if(mail.attachmentsSize() > 0
          && autoDownloadAttachments)
        downloadAttachments(mail);
      
      mail.setHeader(header);
      return mail;
    } catch(IOException ex) {
      log.error(ex.toString());
      return null;
    }
  }
  
  
  private String setGetAttachmentAddress(String uid, String fname) {
    if(uid == null || fname == null) return null;
    int iuid = HTTP_ADDRESS_GET_ATTACHMENT.indexOf(TAG_MAIL_UID);
    int ifname1 = HTTP_ADDRESS_GET_ATTACHMENT.indexOf(TAG_FILE_NAME);
    int ifname2 = HTTP_ADDRESS_GET_ATTACHMENT.indexOf(
        TAG_FILE_NAME, ifname1 + TAG_FILE_NAME.length());
    
    return HTTP_ADDRESS_GET_ATTACHMENT.substring(0, iuid)
        + uid
        + HTTP_ADDRESS_GET_ATTACHMENT.substring(
        iuid + TAG_MAIL_UID.length(), ifname1)
        + fname
        + HTTP_ADDRESS_GET_ATTACHMENT.substring(
        ifname1 + TAG_FILE_NAME.length(), ifname2)
        + fname;
  }
  
  
  private Path getTempFile(String name) {
    if(name == null || name.isEmpty())
      return null;
    
    try {
      if(name.contains("."))
        return Files.createTempFile(tempDir, 
            name.substring(0, name.lastIndexOf("."))+ "_", 
            name.substring(name.lastIndexOf(".")));
      else
        return Files.createTempFile(
            tempDir, name, ".tmp");
    } catch(IOException e) {
      return null;
    }
  }
  
  
  public boolean downloadAttachments(INotesMail mail) {
    if(mail == null || mail.attachmentsSize() < 1)
      return false;
    
    try {
      boolean success = true;
      for(int i = 0; i < mail.attachmentsSize(); i++) {
        Path path = getTempFile(mail.attachment(i).getName());
        String attname = URLEncoder.encode(
            mail.attachment(i).getName(), "UTF-8");
        
        String url = HTTP_ADDRESS_BASE + addressID
            + setGetAttachmentAddress(mail.getUid(), attname);
        
        success = success && this.downloadFile(url, path);
        if(success) {
          mail.attachment(i).setPath(
              path.toAbsolutePath().toString());
        } else {
          Files.delete(path);
        }
      }
      return success;
      
    } catch(IOException ex) {
      return false;
    }
  }
  
  
  public boolean downloadFile(String url, Path dst) {
    if(url == null || url.isEmpty()
        || dst == null)
      return false;
    
    try(OutputStream out = Files.newOutputStream(
        dst, StandardOpenOption.WRITE)) {
      this.connect(new HttpGet(url), false);
      if(httpResp == null || httpResp.getEntity() == null)
        return false;
      
      httpResp.getEntity().writeTo(out);
      return true;
      
    } catch(IOException ex) {
      ex.printStackTrace();
      return false;
    }
  }
  
  
  public void connect(HttpUriRequest req, boolean evaluate) throws IOException {
    if(req == null) return;
    log.info(req.getRequestLine().toString());
    if(cookies != null) {
      req.addHeader(cookies);
      cookies = null;
    }
    req.setHeader(HEADER_USER_AGENT);
    httpResp = client.execute(req);
    log.info(httpResp.getStatusLine().toString());
    if(evaluate) this.evaluateResponse();
  }
  
  
  public void logRequest(HttpUriRequest req) {
    Header[] hds = req.getAllHeaders();
    log.info(" ");
    log.info("---- Request ----");
    log.info(address);
    for(Header h : hds) {
      log.info(h.toString());
    }
    if(req instanceof HttpPost) {
      log.info("..........");
      try { 
        HttpEntity ent = ((HttpPost)req).getEntity();
        if(ent instanceof MultipartEntity) {
          ((MultipartEntity)ent).writeTo(System.out);
        }
        else {
          log.info(EntityUtils.toString(
            ((HttpPost) req).getEntity())); 
        }
      } catch(IOException ex) {}
    }
    log.info("____________________");
  }
  
  
  public INotesConnector setCredentials(String user, String password) {
    if(user != null && password != null) {
      this.user = user;
      this.passwd = password;
      this.setAuthenticate();
    }
    return this;
  }
  
  
  private void setAuthenticate() {
    address = HTTP_ADDRESS_BASE + HTTP_ADDRESS_AUTH;
    formparams.add(new BasicNameValuePair("$PublicAccess", "1"));
    formparams.add(new BasicNameValuePair("username", user));
    formparams.add(new BasicNameValuePair("password", passwd));
    formparams.add(new BasicNameValuePair("RedirectTo", "/oamredirect.nsf/OAMAutoLogin"));
    formparams.add(new BasicNameValuePair("ReasonType", "0"));
    formparams.add(new BasicNameValuePair("ReasonText", ""));
    formparams.add(new BasicNameValuePair("REMOTE_USER", ""));
  }
  
  
  public void reconnect() {
    this.setAuthenticate();
    this.connect();
  }
  
  
  private void authenticate() {
    if(formparams.isEmpty()) return;
    HttpPost post = new HttpPost(address);
    try {
      UrlEncodedFormEntity ent = new UrlEncodedFormEntity(formparams);
      post.setEntity(ent);
      this.connect(post, true);
    } catch(IOException ex) {
      log.error(ex.toString());
    }
  }
  
  
  private void evaluateResponse() throws IOException {
    if(httpResp == null) return;
    response = EntityUtils.toString(httpResp.getEntity());
    if(response == null) return;
    
    this.setCookies();
    
    if(response.toLowerCase().contains(HTTP_REFRESH)
        && (response.toLowerCase().contains(HTTP_URL))) {
      this.metaRefresh();
    }
    
    else if(httpResp.getStatusLine().getStatusCode() == 302
        && httpResp.containsHeader("Location")) {
      this.redirect302();
    }
    
    else if(response.toLowerCase().contains(HTTP_FORM)
        && response.toLowerCase().contains(HTTP_FORM_ACTION)) {
      log.info("HTTP_FORM");
      this.actionFormAuthenticate();
    }
    
    else if(response.contains(HTTP_JS_INOTES) 
        && response.contains(HTTP_JS_REDIRECT)) {
      log.info("HTTP_JS_REDIRECT");
      address = HTTP_ADDRESS_BASE + addressID + HTTP_ADDRESS_INBOX;
      log.info(address);
      this.connect();
      ixml.parseXML(response);
    }
  }
  
  
  private void metaRefresh() throws IOException {
    address = this.getRefreshURL();
    if(address.startsWith("/"))
      address = HTTP_ADDRESS_BASE + address;
    log.info("Meta-refresh: "+ address);
    HttpGet get = new HttpGet(address);
    this.connect(get, true);
  }
  
  
  private void redirect302() {
    address = httpResp.getFirstHeader("Location").getValue();
    log.info("302-Redirect: "+ address);
    this.authenticate();
  }
  
  
  private String parseActionForm() {
    String addr = HTTP_ADDRESS_BASE;
    int iform = response.indexOf(HTTP_FORM);
    int iact = response.indexOf(HTTP_FORM_ACTION, iform) 
        + HTTP_FORM_ACTION.length();
    int iact2 = response.indexOf("\"", iact+1);
    if(iact2 < 0) iact2 = response.indexOf("'", iact+1);
    String action = response.substring(iact + 1, iact2);
    if(!action.startsWith("/")) action = "/"+ action;
    addr += action;
    return addr;
  }
  
  
  private String parseModDate() {
    int ivalue = response.indexOf(HTTP_INPUT_HIDDEN)
        + HTTP_INPUT_HIDDEN.length();
    int ivalue2 = response.indexOf("\"", ivalue);
    if(ivalue2 < 0) ivalue2 = response.indexOf("'", ivalue);
    return response.substring(ivalue, ivalue2);
  }
  
  
  private void actionFormAuthenticate() {
    //this.logResponseHeaders();
    address = this.parseActionForm();
    formparams.add(new BasicNameValuePair(MOD_DATE, this.parseModDate()));
    this.authenticate();
  }
  
  
  private String parseCookies() {
    String cookie = "";
    if(!httpResp.containsHeader(HTTP_SET_COOKIE)) return cookie;
    Header[] cks = httpResp.getHeaders(HTTP_SET_COOKIE);
    for(int i = 0; i < cks.length; i++) {
      String value = cks[i].getValue();
      value = value.substring(0, value.indexOf("; ") + 2);
      if(value.contains(HTTP_COOKIE_INOTES)
          || value.contains(HTTP_COOKIE_SESSION)
          || value.contains(HTTP_COOKIE_SHIMMER)) {
        if(value.contains(HTTP_COOKIE_INOTES))
          inotesCookie = value;
        if(value.contains(HTTP_COOKIE_SESSION))
          sessionCookie = value;
        if(value.contains(HTTP_COOKIE_SHIMMER))
          shimmerCookie = value;
      } else {
        cookie += value;
      }
    }
    
    if(shimmerCookie != null && !shimmerCookie.trim().isEmpty()) {
      int is = shimmerCookie.indexOf(DELIM_COOKIE_X_IBM_NOTES)
          + DELIM_COOKIE_X_IBM_NOTES.length();
      xIbmNotesCookie = shimmerCookie.substring(is).trim();
      if(xIbmNotesCookie.endsWith(";"))
        xIbmNotesCookie = xIbmNotesCookie.substring(
            0, xIbmNotesCookie.length() -1);
    }
    return cookie;
  }
  
  
  private void setCookies() {
    String cookie = this.parseCookies();
    if(inotesCookie != null && !inotesCookie.trim().isEmpty())
      cookie += inotesCookie;
    if(sessionCookie != null && !sessionCookie.trim().isEmpty())
      cookie += sessionCookie;
    if(shimmerCookie != null && !shimmerCookie.trim().isEmpty())
      cookie += shimmerCookie;
    if(cookie != null && !cookie.trim().isEmpty())
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
    String naddr = response.substring(iurl + HTTP_URL.length(), iend);
    if(!naddr.startsWith("http")) {
      if(!naddr.startsWith("/")) naddr = "/" + naddr;
      naddr = address.substring(0, address.indexOf("/", address.indexOf("://")+4)) + naddr;
    }
    addressID = naddr.substring(naddr.indexOf("/", naddr.indexOf("://")+4));
    return naddr;
  }
  
  
  public boolean isConnected() {
    return addressID != null;
  }
  
  
  public void logResponseHeaders() {
    log.info("\n");
    log.info("**** Response ****");
    if(httpResp == null) return;
    for(Header h : httpResp.getAllHeaders()) {
      log.info(h.toString());
    }
    log.info(response);
  }
  
  
  public static void main(String[] args) throws InterruptedException {
    INotesConnector con = new INotesConnector();
    con.setCredentials("f6036477", "65465488").connect();
    con.logResponseHeaders();
    
    SimpleLog l = LogProvider.getSimpleLog();
    /*
    l.info(" ");
    l.info(" ");
    l.info("INotesMail mail = new INotesMail()");
    l.info("    .setFrom(\"CN=F6036477 Juno Roesler\")");
    l.info("    .setTo(\"f6036477@bb.com.br\")");
    l.info("    .setSubject(\"Arquivo\")");
    l.info("    .setContent(\"ID arquivo:\");");
    l.info("                  ###161###");
    
    FileAttachment ath = new FileAttachment("D:/archive.txt");
    l.info("Attachment: "+ ath.getPath());
    
    INotesMail mail = new INotesMail()
        .setFrom("CN=F6036477 Juno Roesler")
        .setTo("f6036477@bb.com.br")
        .setSubject("Arquivo")
        .setContent("ID arquivo:<br>###161###")
        .attach(ath);
    con.sendMail(mail);
    con.logResponseHeaders();
    
    l.info("ATTACH MAIL SEND: [OK]");
    
    Thread.sleep(5000);
    
    l.info(" ");
    l.info(" ");
    l.info("Refreshing inbox...");
    List<INotesHeaderMail> mails = con.refreshInbox();
    System.out.println(mails.get(0));
    INotesMail mail = con.getMail(mails.get(0));
    l.info("First mail: ");
    l.info(mail.toString());*/
  }
  
}
