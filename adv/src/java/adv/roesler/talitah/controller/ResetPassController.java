/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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
package adv.roesler.talitah.controller;

import badraadv.User;
import badraadv.Password;
import com.jpower.date.SimpleDate;
import com.jpower.mongo4j.GenericDAO;
import com.jpower.mongo4j.QueryBuilder;
import com.jpower.simplemail.MailServer;
import com.jpower.simplemail.Message;
import com.jpower.simplemail.SMail;
import com.jpower.simplemail.SMailException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 26/12/2011
 */
@ManagedBean
@RequestScoped
public class ResetPassController {
  
  public static final String CLIENT_ID = "pass-email";
  
  public static final String 
      MESSAGES_FILE = "../work/adv/resetpass_messages.properties",
      KEY_EMAIL_CONTENT = "EMAIL_CONTENT",
      KEY_EMAIL_SUBJECT = "EMAIL_SUBJECT",
      KEY_SUCCESS_MESSAGE = "SUCCESS_MESSAGE",
      KEY_ERROR_MESSAGE = "ERROR_MESSAGE",
      KEY_EMAIL_FROM = "EMAIL_FROM",
      KEY_EMAIL_LOGIN = "EMAIL_LOGIN",
      KEY_EMAIL_PASS = "EMAIL_PASS";
  
  public static final String 
      DEFAULT_EMAIL_CONTENT = "@name,\n conforme sua solicitação (@date), geramos uma nova senha de acesso ao portal Badra Advocacia.\n Sugerimos que você acesse o site e altere-a assim que possível.\n\n Sua nova senha é: @pass.\n\n Atenciosamente.\n Badra Advocacia.",
      DEFAULT_EMAIL_SUBJECT = "Nova Senha - Badra Advocacia",
      DEFAULT_SUCCESS_MESSAGE = "Uma nova senha foi gerada e enviada para o seu email.</br> Verifique sua caixa de entrada.",
      DEFAULT_ERROR_MESSAGE = "Ocorreu um erro enviando a nova senha.</br> Tente novamente mais tarde.",
      DEFAULT_EMAIL_FROM = "Badra Advocacia <talitahmelobadra@gmail.com>",
      DEFAULT_EMAIL_LOGIN = "talitahmelobadra";
  
  public static final String 
      TAG_NAME = "@name",
      TAG_SURNAME = "@surname",
      TAG_PASS = "@pass",
      TAG_DATE = "@date";
  
  private static final String 
      EMAIL_PASS = "passwd=$0988";
  
  
  private GenericDAO dao;
  
  private String plainPass;
  
  private Password pass;
  
  private String doneMessage;
  
  private Properties props;
  
  private String emailLogin;
  
  private String emailFrom;
  
  
  public ResetPassController() {
    dao = new GenericDAO("adv");
    this.createRandomPassword();
    doneMessage = this.retrieveSuccessMessage();
    this.createStandardMessagesFile();
  }
  
  
  public void createStandardMessagesFile() {
    File messages = new File(MESSAGES_FILE);
    if(messages.exists()) {
      props = this.loadMessages();
      return;
    }
    
    props = new Properties();
    props.setProperty(KEY_EMAIL_CONTENT, DEFAULT_EMAIL_CONTENT);
    props.setProperty(KEY_EMAIL_SUBJECT, DEFAULT_EMAIL_SUBJECT);
    props.setProperty(KEY_SUCCESS_MESSAGE, DEFAULT_SUCCESS_MESSAGE);
    props.setProperty(KEY_ERROR_MESSAGE, DEFAULT_ERROR_MESSAGE);
    props.setProperty(KEY_EMAIL_FROM, DEFAULT_EMAIL_FROM);
    props.setProperty(KEY_EMAIL_LOGIN, DEFAULT_EMAIL_LOGIN);
    props.setProperty(KEY_EMAIL_PASS, EMAIL_PASS);
    
    try {
      FileOutputStream fos = 
          new FileOutputStream(messages);
      props.store(fos, null);
      fos.close();
    } catch(IOException ex) {}
  }
  
  
  public Properties loadMessages() {
    try {
      Properties p = new Properties();
      FileInputStream fis = 
          new FileInputStream(MESSAGES_FILE);
      p.load(fis);
      fis.close();
      return p;
    } catch(IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }
  
  
  public void createRandomPassword() {
    int intPass = (int) ((Math.random() + 1.0) * 80000);
    plainPass = String.valueOf(intPass);
    pass = new Password(plainPass);
  }
  
  
  public User findUser(String email) {
    if(email == null) return null;
    
    QueryBuilder q = new QueryBuilder();
    q.byClass(User.class).equal("email", email);
    
    return dao.findOne(q);
  }
  
  
  public void setMessage(String message) {
    FacesContext c = FacesContext.getCurrentInstance();
    FacesMessage msg = new FacesMessage(
        FacesMessage.SEVERITY_ERROR, message, null);
    c.addMessage(CLIENT_ID, msg);
  }
  
  
  public String retrieveEmailContent() {
    return props.getProperty(KEY_EMAIL_CONTENT);
  }
  
  
  public String retrieveEmailSubject() {
    return props.getProperty(KEY_EMAIL_SUBJECT);
  }
  
  
  public String retrieveSuccessMessage() {
    return props.getProperty(KEY_SUCCESS_MESSAGE);
  }
  
  
  public String retrieveErrorMessage() {
    return props.getProperty(KEY_ERROR_MESSAGE);
  }
  
  
  public String retrieveEmailLogin() {
    return props.getProperty(KEY_EMAIL_LOGIN);
  }
  
  
  public String retrieveEmailFrom() {
    return props.getProperty(KEY_EMAIL_FROM);
  }
  
  
  public String retrieveEmailPass() {
    return props.getProperty(KEY_EMAIL_PASS);
  }
  
  
  public void sendEmail(User u) {
    if(u == null || u.getEmail() == null
        || pass == null || plainPass == null) {
      return;
    }
    
    MailServer server = MailServer.GMAIL_SMTP_SERVER;
    server.setAuthentication(retrieveEmailLogin(), retrieveEmailPass());
    
    SMail mail = new SMail(server);
    
    Message m = new Message();
    m.from(retrieveEmailFrom());
    m.to(u.getName() + " <" + u.getEmail() + ">");
    
    String nameTag = (u.getGender().startsWith("M") ? "Sr. " : "Sra. ");
    nameTag += u.getName();
    String surnameTag = u.getSurname();
    String dateTag = new SimpleDate().format(SimpleDate.DDMMYYYY_SLASH);
    
    String text = this.retrieveEmailContent();
    text = text.replaceAll(TAG_NAME, nameTag);
    text = text.replaceAll(TAG_SURNAME, surnameTag);
    text = text.replaceAll(TAG_DATE, dateTag);
    text = text.replaceAll(TAG_PASS, plainPass);
    
    m.setContent(text);
    m.setSubject(this.retrieveEmailSubject());
    
    try {
      mail.send(m);
    } catch(SMailException ex) {
      doneMessage = this.retrieveErrorMessage();
    } finally {
      mail.close();
    }
  }


  public String getDoneMessage() {
    return doneMessage;
  }
  
  
  public void resetPassword(User u) {
    if(u == null) return;
    
    User found = this.findUser(u.getEmail());
    
    if(found == null || !found.validate()) {
      this.setMessage("Este E-mail não está cadastrado.");
      return;
    }
    
    this.createRandomPassword();
    u.setPassword(this.pass);
    dao.update(found, u);
    this.sendEmail(u);
  }
  
}
