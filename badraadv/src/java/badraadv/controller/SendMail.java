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
import com.jpower.mongo4j.QueryBuilder;
import com.jpower.simplemail.MailServer;
import com.jpower.simplemail.Message;
import com.jpower.simplemail.SMail;
import com.jpower.simplemail.SMailException;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 14/06/2012
 */
@ManagedBean
@SessionScoped
public class SendMail implements Serializable {
  
  public static final String 
      FROM = "Badra Advocacia <contato@badraadvocacia.adv.br>",
      SEND_SUCCESS = "E-mail enviado com sucesso!",
      SEND_FAIL = "E-mail Não enviado! Ocorreu um erro com o serviço de envio. Tente novamente daqui alguns minutos.",
      SEND_ERROR = "Dados para envio incompletos. Verifique se todos os campos foram preenchidos.";
  
  
  @ManagedProperty(value="#{dao}")
  private DAOProvider dao;
  
  private String subject;
  
  private String message;
  
  private List<User> users;
  
  private User[] selected;
  
  private boolean showPreview;
  
  private SMail smail;
  
  private String sendMailResult;
  
  
  public SendMail() {
    showPreview = false;
    MailServer server = MailServer.GMAIL_SMTP_SERVER;
    server.setAuthentication("contato@badraadvocacia.adv.br", "inadonuj");
    smail = new SMail(server);
    sendMailResult = "";
  }


  public String getMessage() {
    return message;
  }


  public void setMessage(String message) {
    this.message = message;
  }


  public User[] getSelected() {
    return selected;
  }


  public void setSelected(User[] selected) {
    if(selected == null) return;
    this.selected = selected;
  }
  
  
  public String selectText() {
    if(selected == null || selected.length == 0)
      return "Selecione";
    return selected.length+ " Destinatário(s)";
  }
  
  
  public String getSubject() {
    return subject;
  }


  public void setSubject(String subject) {
    this.subject = subject;
  }


  public List<User> getUsers() {
    QueryBuilder q = new QueryBuilder()
        .byClass(User.class);
    users = dao.get().find(q);
    return users;
  }
  
  
  public void setShowPreview(boolean b) {
    System.out.println("# SHOW_PREVIEW: "+ b);
    showPreview = b;
  }
  
  
  public boolean getShowPreview() {
    if(showPreview) {
      showPreview = false;
      return true;
    }
    return showPreview;
  }


  public void setUsers(List<User> users) {
    this.users = users;
  }


  public DAOProvider getDao() {
    return dao;
  }


  public void setDao(DAOProvider dao) {
    this.dao = dao;
  }
  
  
  private void setMessageContent(Message msg) {
    File img = new File("../badraadv/ROOT/resources/images/header-mail.png");
    msg.setContentHtml(
        "<div style='border: solid 2px gray; width: 500px; background-color: #F5F5F5;'>"
      + "  <a href='http://www.badraadvocacia.adv.br'>"
      + msg.embedImage(img)
      + "  </a>"
      + "  <h4 style='text-indent: 10px;'>"+ subject+ "</h4>"
      + "  <table>"
      + "    <tr>"
      + "      <td style='width: 20px;'></td>"
      + "      <td style='width: 470px;'>"
      +          message
      + "      </td>"
      + "      <td style='width: 10px;'></td>"
      + "    </tr>"
      + "  </table>"
      + "  <h4 style='text-indent: 10px;'>Badra Advocacia.</h4>"
      + "</div>");
  }
  
  
  public void sendMail() {
    sendMailResult = "";
    
    if(subject == null || message == null 
        || selected == null 
        || selected.length < 1) {
      sendMailResult = SEND_ERROR;
      return;
    }
    
    Message msg = new Message();
    msg.from(FROM);
    
    for(int i = 0; i < selected.length; i++) {
      msg.to(selected[i].getEmail());
    }
    
    msg.setSubject(subject);
    this.setMessageContent(msg);
    
    try {
      smail.send(msg);
      sendMailResult = SEND_SUCCESS;
    } catch(SMailException ex) {
      sendMailResult = SEND_FAIL;
    } finally {
      smail.close();
    }
  }
  
  
  public String getSendMailResult() {
    return sendMailResult;
  }
  
}
