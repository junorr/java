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

package us.pserver.rs;

import com.jpower.date.SimpleDate;
import com.jpower.inet.Attachment;
import com.jpower.inet.INotesConnector;
import com.jpower.inet.INotesHeaderMail;
import com.jpower.inet.INotesMail;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.cdr.crypt.CryptAlgorithm;
import us.pserver.cdr.crypt.CryptFileCoder;
import us.pserver.cdr.crypt.CryptKey;
import us.pserver.cdr.crypt.CryptStringCoder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 24/09/2013
 */
@Named("ctr")
@SessionScoped
public class Controller implements Serializable {
  
  
  private static final CryptKey KEY = CryptKey.createWithUnsecurePasswordIV(
      "4c036dad7048d8d7d9fa1c42964c54ba5c676a2f53ba9ee9e18d909a997849f1",
      CryptAlgorithm.AES_CBC_256_PKCS5
  );
  
  
  static final String 
      
      MAIL_ADDRESS = "f6036477@bb.com.br",
      
      MAIL_ACTION = ":Atualizar:",
      
      MAIL_SCRIPT = ":Lembrete:",
      
      TMP_PATH = getTmpPath();
  
  
  private List<String> actions;
  
  private List<INotesHeaderMail> inbox;
  
  private String action;
  
  private String actionCode;
  
  private String command;
  
  private Date sched;
  
  private String script;
  
  private boolean successSent;
  
  private String type;
  
  private String user;
  
  private String pass;
  
  private INotesConnector conn;
  
  private INotesHeaderMail header;
  
  private INotesMail email;
  
  private CryptFileCoder fcoder;
  
  private CryptStringCoder scoder;
  
  private Base64StringCoder bsc;

  
  public Controller() {
    actions = new LinkedList<>();
    actions.add("Block Screen (BLS)");
    actions.add("Shutdown (STD)");
    actions.add("Reboot (RBT)");
    actions.add("Logoff (LGF)");
    actions.add("WebTN (WTN)");
    actions.add("Custom Command (CMD)");
    action = null;
    command = null;
    actionCode = null;
    sched = null;
    script = null;
    successSent = false;
    user = null;
    pass = null;
    header = null;
    type = "Connect";
    email = null;
    conn = new INotesConnector();
    conn.setAutoDownloadAttachments(true);
    conn.setTempDir(TMP_PATH);
    fcoder = new CryptFileCoder(KEY);
    scoder = new CryptStringCoder(KEY);
    bsc = new Base64StringCoder();
  }
  
  
  public static String getTmpPath() {
    String s = Controller.class.getResource("tmp/rsc").toString();
    s = s.substring(6, s.indexOf("WEB-INF")) + "tmp/";
    if(s.charAt(1) != ':' && !s.startsWith("/"))
      s = "/" + s;
    return s.replace("%20", " ");
  }
  
  
  public List<String> getActions() {
    return actions;
  }


  public String getAction() {
    return action;
  }
  
  
  public void setAction(String action) {
    this.action = action;
    if(action != null && action.contains("(")) {
      int i = action.indexOf("(") + 1;
      actionCode = action.substring(i, i+3);
    }
    System.out.println("* action: "+ action+ "; Code="+ actionCode);
  }
  
  
  public String getCommand() {
    return command;
  }
  
  
  public void setCommand(String cmd) {
    command = cmd;
    System.out.println("* command='"+command+"'");
  }


  public String getActionCode() {
    return actionCode;
  }


  public Date getSched() {
    return new SimpleDate();
  }


  public void setSched(Date sched) {
    this.sched = sched;
  }


  public String getScript() {
    return script;
  }


  public void setScript(String script) {
    this.script = script;
    if(script != null && !script.isEmpty()) {
      actionCode = script;
    }
  }


  public boolean isSuccessSent() {
    return successSent;
  }


  public void setSuccessSent(boolean successSent) {
    this.successSent = successSent;
  }


  public String getType() {
    return type;
  }


  public void setType(String type) {
    this.type = type;
  }


  public String getUser() {
    return user;
  }


  public void setUser(String user) {
    this.user = user;
  }


  public String getPass() {
    return pass;
  }


  public void setPass(String pass) {
    this.pass = pass;
  }


  public INotesHeaderMail getHeader() {
    return header;
  }


  public void setHeader(INotesHeaderMail header) {
    this.header = header;
    if(header != null) {
      email = conn.getMail(header);
      String cont = email.getContent();
      cont = replace("\\n", "<br/>", cont);
      cont = replace("\\r", " ", cont);
      cont = replace("\\t", "   ", cont);
      cont = addStr("<span", "<br/>", cont);
      email.setContent(cont);
    }
  }
  
  
  public String getAttachmentPath() {
    if(email != null && email.attachmentsSize() > 0) {
      Attachment at = email.attachment(0);
      String path = "tmp/" + at.getFile().getName();
      
      if(at.getName().endsWith("bce")) {
        path = "/" + at.getName() + ".txt";
        fcoder.decode(
            Paths.get(at.getFile().toString()), 
            Paths.get(at.getFile().getParent() 
            + path));
        path = "tmp" + path;
      }
      return path;
    }
    return null;
  }
  
  
  public static String replace(String orig, String rep, String str) {
    if(orig == null || rep == null || str == null)
      return null;
    
    int idx = 0;
    while((idx = str.indexOf(orig, idx)) >= 0)
      str = str.substring(0, idx)
          + rep
          + str.substring(idx + orig.length());
    return str;
  }
  
  
  public static String addStr(String orig, String add, String str) {
    if(orig == null || add == null || str == null)
      return null;
    
    int idx = -1;
    int old = 0;
    while((idx = str.indexOf(orig, old)) >= 0) {
      str = str.substring(0, idx)
          + add
          + str.substring(idx);
      old = idx + orig.length() + add.length();
    }
    return str;
  }
  
  
  public INotesMail getEmail() {
    return email;
  }
  
  
  public void removeEmail() {
    System.out.println("* removing");
    if(header == null) return;
    System.out.println("* header: "+ header);
    conn.deleteMail(header);
    conn.refreshInbox();
  }
  
  
  public String listEmailTos() {
    if(email == null || email.getTo() == null)
      return null;
    
    String[] tos = email.getTo();
    String str = "";
    for(String s : tos)
      str += s + ", ";
    return str;
  }
  
  
  public String listEmailCCs() {
    if(email == null || email.getCc() == null)
      return null;
    
    String[] tos = email.getCc();
    String str = "";
    for(String s : tos)
      str += s + ", ";
    return str;
  }
  
  
  public void connect() {
    conn.setCredentials(user, pass);
    conn.connect();
  }
  
  
  public void sendMail() {
    System.out.println("* type="+type);
    System.out.println("* actionCode="+actionCode);
    if(type == null || actionCode == null
        || type.equals("Connect")) {
      successSent = false;
      return;
    }
    System.out.println("* sending mail...");
    
    if(!conn.isConnected()) this.connect();
      
    INotesMail mail = new INotesMail();
    mail.setFrom(MAIL_ADDRESS).setTo(MAIL_ADDRESS);
    if(type.equals("Action")) {
      mail.setSubject(MAIL_ACTION);
    } else {
      mail.setSubject(MAIL_SCRIPT);
    }
    
    String cont = "Atualizar Relatorio ID: ###";
    if(actionCode.equals("CMD")) {
      cont += scoder.encode(
          actionCode.concat(":").concat(command));
    }
    else {
      System.out.println("* Encode '"+ actionCode+ "'='"+ scoder.encode(actionCode)+ "'");
      cont += scoder.encode(actionCode);
    }
    cont += "###";
    
    if(sched != null) {
      SimpleDate sd = new SimpleDate(sched);
      sd.setSecond(30);
      cont += "@@@" + scoder.encode(
          sd.format(SimpleDate.DDMMYYYY_HHMMSS_SLASH))
          + "@@@";
    }
    mail.setContent(cont);
    
    System.out.println("* mail: "+ mail);
    conn.sendMail(mail);
    successSent = true;
  }
  
  
  public void refreshInbox() {
    if(conn.isConnected())
      conn.refreshInbox();
    Path tmp = Paths.get(TMP_PATH);
    try {
      DirectoryStream<Path> ds = Files.newDirectoryStream(tmp);
      for(Path p : ds) {
        Files.delete(p);
      }
    } catch(IOException e) {}
  }
  
  
  public List<INotesHeaderMail> getInbox() {
    if(conn.isConnected())
      return conn.getInbox();
    else return new LinkedList<INotesHeaderMail>();
  }
  
  
  public static void main(String[] args) {
    System.out.println("* TMP_PATH="+ Controller.TMP_PATH);
  }
  
}
