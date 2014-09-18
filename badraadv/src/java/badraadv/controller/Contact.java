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
package badraadv.controller;

import badraadv.bean.User;
import com.jpower.simplemail.MailServer;
import com.jpower.simplemail.Message;
import com.jpower.simplemail.SMail;
import com.jpower.simplemail.SMailException;
import java.io.File;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 04/06/2012
 */
@ManagedBean
@RequestScoped
public class Contact {
  
  @ManagedProperty(value="#{user}")
  private User user;
  
  private String subject;
  
  private String message;
  
  private SMail smail;
  
  public static final String FROM = "Badra Advocacia <contato@badraadvocacia.adv.br>";
  
  public static final String SUCCESS_SEND_MAIL = "mail_send.xhtml";
  
  public static final String FAIL_SEND_MAIL = "mail_fail.xhtml";
  
  
  public Contact() {
    MailServer ms = MailServer.GMAIL_SMTP_SERVER;
    ms.setAuthentication("contato@badraadvocacia.adv.br", "inadonuj");
    smail = new SMail(ms);
  }


  public String getMessage() {
    return message;
  }


  public void setMessage(String message) {
    this.message = message;
  }


  public String getSubject() {
    return subject;
  }


  public void setSubject(String subject) {
    this.subject = subject;
  }


  public User getUser() {
    return user;
  }


  public void setUser(User user) {
    this.user = user;
  }
  
  
  private void setMessageContent(Message msg) {
    File img = new File("../badraadv/ROOT/resources/images/header-mail.png");
    msg.setContentHtml(
          "<div style='border: solid 2px gray; width: 500px; background-color: #FAFAFA;'>"
        + "  <a href='http://www.badraadvocacia.adv.br'>"
        + msg.embedImage(img)
        + "  </a>"
        + "  <h3 style='text-indent: 10px;'>Dados de contato:</h3>"
        + "  <table style='font-weight: bold;'>"
        + "    <tr>"
        + "      <td style='width: 20px;'></td>"
        + "      <td>Nome:</td>"
        + "      <td>"+ user.getFullName()+ "</td>"
        + "    </tr>"
        + "    <tr>"
        + "      <td style='width: 20px;'></td>"
        + "      <td>E-mail:</td>"
        + "      <td>"+ user.getEmail()+ "</td>"
        + "    </tr>"
        + (user.getPhone() != null ? 
          "    <tr>"
        + "      <td style='width: 20px;'></td>"
        + "      <td>Telefone:</td>"
        + "      <td>"+ user.getPhone()+ "</td>"
        + "    </tr>" : "")
        + "  </table>"
        + "  <h3 style='text-indent: 10px;'>Mensagem:</h3>"
        + "  <table>"
        + "    <tr>"
        + "      <td style='width: 20px;'></td>"
        + "      <td style='width: 470px; text-align: justify;'>"
        +          this.message
        + "      </td>"
        + "      <td style='width: 10px;'></td>"
        + "    </tr>"
        + "  </table>"
        + "</div>");
  }
  
  
  public String sendMail() {
    if(subject == null || message == null 
        || user == null)
      return null;
    
    Message msg = new Message();
    msg.from(FROM);
    msg.to("talitah@badraadvocacia.adv.br");
    msg.setSubject("Contato Portal Badra: "+subject);
    this.setMessageContent(msg);
    
    try {
      smail.send(msg);
    } catch(SMailException ex) {
      ex.printStackTrace();
      return FAIL_SEND_MAIL;
    } finally {
      smail.close();
    }
    return SUCCESS_SEND_MAIL;
  }
  
}
