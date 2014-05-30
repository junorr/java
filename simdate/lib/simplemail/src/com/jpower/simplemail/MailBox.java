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

package com.jpower.simplemail;

import com.jpower.annotations.info.Contact;
import com.jpower.annotations.info.Copyright;
import com.jpower.annotations.info.License;
import com.jpower.annotations.info.Version;
import com.jpower.simplemail.event.MailBoxEvent;
import com.jpower.simplemail.event.MailBoxListener;
import com.jpower.simplemail.event.PrinterConnectionListener;
import com.jpower.simplemail.event.PrinterMailBoxListener;
import com.jpower.simplemail.event.PrinterMailListener;
import com.jpower.simplemail.filter.NewMessageFilter;
import com.jpower.simplemail.internal.TypeLinkedList;
import java.io.File;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.MessagingException;


/**
 * <p style="font-size: medium;">
 * Caixa de email onde são 
 * armazenadas as mensagens da conta.
 * Implementa métodos para recuperação, leitura e remoção 
 * de emails, além de funcionalidades para 
 * verificação automática e notificação de novos emails 
 * em intervalos de tempo pré-determinados.
 * </p>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "MailBox",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Caixa de recebimento e armazenamento de emails"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class MailBox extends TypeLinkedList<MailBoxListener> {
  
  private String name;
  
  private javax.mail.Message[] mailms;
  
  private Messages ms;
  
  private Folder folder;
  
  private SMail mail;
  
  private int reloadTime;
  
  private int msgcount, newcount;
  
  private Timer timer;
  
  private boolean autoclose;


  /**
   * Tarefa para verificar por novas mensagens 
   * na conta de email.
   */
  private class CheckMailTask extends TimerTask {
    @Override
    public void run() {
      if(!isBoxClosed() && isConnected()) {
        if(hasNewMessages())
          try {
            reload();
          } catch(SMailException ex) {}
      } else
        try {
          connect();
          if(isAutoClosingOnReload()) {
            closeBox();
            closeConnection();
          }
        } catch(SMailException ex) {}
    }
  }
  
  
  /**
   * Construtor padrão que recebe uma instância de 
   * <code>SMail</code> e o nome da pasta da qual 
   * serão recuperadas as mensagens.
   * @param provider <code>SMail</code> com as configurações
   * da conta de email.
   * @param folderName Nome da pasta da conta de email.
   */
  public MailBox(SMail provider, String folderName) {
    if(provider == null) throw 
        new IllegalArgumentException("{MailBox( SMail, String )}: "
        + "Invalid SMail <Provider>: " + provider);
    if(folderName == null || folderName.trim().equals(""))
        throw new IllegalArgumentException("{MailBox( SMail, String )}: "
        + "Invalid folder name: " + folderName);
    
    name = folderName;
    folder = null;
    ms = new Messages();
    mail = provider;
    mailms = null;
    reloadTime = -1;
    msgcount = newcount = 0;
    timer = new Timer(false);
    autoclose = false;
  }
  
  
  /**
   * Retona um array contendo todos os listeners anexados
   * à MailBox.
   * @return array de listeners.
   */
  @Override
  public MailBoxListener[] toArray() {
    if(this.isEmpty()) return null;
    MailBoxListener[] mls = new MailBoxListener[this.size()];
    return list.toArray(mls);
  }
  
  
  /**
   * Retorna <code>boolean</code> indicando se
   * existem novas mensagens (não lidas) na caixa de email.
   * @return <code>true</code> caso existam novas mensagens
   * não lidas, <code>false</code> caso contrário.
   */
  public boolean hasNewMessages() {
    if(this.isBoxClosed() || !this.isConnected()) 
      return false;
    try {
      int nc = folder.getUnreadMessageCount();
      int mc = folder.getMessageCount();
      boolean b = nc != newcount;
      msgcount = mc;
      newcount = nc;
      if(b) this.notifyNewMessages();
      return b;
    } catch(Exception ex) {
      return false;
    }
  }
  
  
  /**
   * Retorna o número de mensagens existentes 
   * na caixa.
   * @return Quantidade de mensagens na caixa de email.
   */
  public int getMessagesCount() {
    return msgcount;
  }
  
  
  /**
   * Retorna o número de novas mensagens (não lidas)
   * existentes na caixa.
   * @return Quantidade de novas mensagens na caixa de email.
   */
  public int getNewMessagesCount() {
    return newcount;
  }
  
  
  /**
   * Conecta com a conta de email.
   * @throws SMailException caso ocorra erro.
   */
  public void connect() throws SMailException {
    folder = mail.receiveFolder(name);
    this.reload();
  }
  
  
  /**
   * Recarrega as mensagens da caixa, caso existam
   * novas mensagens. Não é necessária a chamada
   * deste método na primeira conexão com a conta 
   * de email.
   * @throws SMailException caso ocorra erro.
   */
  public void reload() throws SMailException {
    if(this.isBoxClosed())
      throw new SMailException("{MailBox.reload()}: "
          + "Error in SMail.receiveFolder( String ). "
          + "Invalid Folder");
    if(!this.isConnected())
      throw new SMailException("{MailBox.reload()}: "
          + "MailBox is not connected");
    
    try {
      if(!this.hasNewMessages()) return;
      
      this.notifyReload();
      
      mailms = folder.getMessages();
      if(mailms.length == 0) return;
      ms.clear();
      for(int i = 0; i < mailms.length; i++) {
        Message m = ms.addNewMessage(mailms[i]);
        if(m != null) m.setUid(i);
      }
    } catch(MessagingException ex) {
      throw new SMailException("{SMail.connect()}", ex);
    }
  }
  
  
  /**
   * Notifica o recarregamento da caixa de email 
   * aos listeners registrados.
   */
  private void notifyReload() {
    MailBoxEvent e = new MailBoxEvent(this, msgcount, newcount);
    Iterator<MailBoxListener> it = this.iterator();
    while(it.hasNext())
      it.next().reloadingMailBox(e);
  }
  
  
  /**
   * Notifica novas mensagens na caixa de email 
   * aos listeners registrados.
   */
  private void notifyNewMessages() {
    MailBoxEvent e = new MailBoxEvent(this, msgcount, newcount);
    Iterator<MailBoxListener> it = this.iterator();
    while(it.hasNext())
      it.next().messageReceived(e);
  }
  
  
  /**
   * Define o tempo (em milisegundos) de recarregamento 
   * automático (AutoReload) da caixa de email (OFF < 100 >= ON). 
   * Um número menor do que 100 desliga o AutoReload.
   * @param millis tempo de recarregamento automático.
   */
  public void setAutoReload(int millis) {
    this.reloadTime = millis;
    if(millis < 100) {
      timer.cancel();
      timer = new Timer(false);
    } else {
      timer.scheduleAtFixedRate(new CheckMailTask(), 0, millis);
    }
  }
  
  
  /**
   * Indica se após o recarregamento da caixa de email,
   * a conexão com a conta será fechada.
   * @return <code>true</code> se a conexão será
   * fechada após o recarregamento, <code>false</code>
   * caso contrário.
   */
  public boolean isAutoClosingOnReload() {
    return autoclose;
  }
  
  
  /**
   * Configura se a conexão com a conta de email deverá 
   * ser fechada após o recarregamento da caixa 
   * (<code>DEFAULT = false</code>).
   * Útil quando o tempo de recarregamento automático
   * da caixa de emails for curto, evitando utilização
   * excessiva de recursos.
   * @param auto <code>true</code> para que a conexão seja
   * fechada após o recarregamento, <code>false</code>
   * para continuar ativa.
   */
  public void setAutoCloseOnReload(boolean auto) {
    autoclose = auto;
  }
  
  
  /**
   * Retorna o tempo (em milisegundos) de recarregamento
   * automático da caixa de email.
   * @return tempo em milisegundos.
   */
  public int getAutoReloadTime() {
    return reloadTime;
  }
  
  
  /**
   * Indica se o recarregamento automático da
   * caixa de emails está habilitado.
   * @return <code>true</code> se o recarregamento
   * automático estiver habilitado, <code>false</code>
   * caso contrário.
   */
  public boolean isAutoReloadEnabled() {
    return this.reloadTime > 100;
  }
  
  
  /**
   * Fecha a caixa e a pasta da conta de email.
   */
  public void closeBox() {
    try {
      folder.close(true);
    } catch(MessagingException ex) {}
  }
  
  
  /**
   * Indica se a caixa e a pasta da conta 
   * de email estão fechadas.
   * @return <code>true</code> se a caixa estiver fechada,
   * <code>false</code> caso contrário.
   */
  public boolean isBoxClosed() {
    return folder == null || !folder.isOpen();
  }
  
  /**
   * Fecha a conexão com a conta de email.
   */
  public void closeConnection() {
    mail.close();
  }
  
  
  /**
   * Indica se está conectado com a conta de email.
   * @return <code>true</code> se a conexao estiver 
   * ativa, <code>fase</code> caso contrário.
   */
  public boolean isConnected() {
    return mail.isConnected();
  }
  
  
  /**
   * Retorna todas os emails da caixa.
   * @return <code>Messages</code> contendo 
   * todas as mensagens da caixa.
   */
  public Messages getMessages() {
    return ms;
  }
  
  
  /**
   * Marca uma mensagem diretamente com flag informada.
   * @param m Mensagem a ser marcada.
   * @param f Flag.
   * @return <code>true</code>
   */
  private boolean mark(Message m, Flag f) {
    if(this.isBoxClosed() || !this.isConnected()) 
      return false;
    if(m == null || m.getUid() < 0
        || m.getUid() > mailms.length -1) return false;
    
    javax.mail.Message mx = mailms[(int) m.getUid()];
    if(mx == null) return false;
    
    javax.mail.Message[] mxs = {mx};
    Flags fs = new Flags();
    fs.add(f);
    
    try {
      folder.setFlags(mxs, fs, true);
    } catch(MessagingException ex) {
      return false;
    }
    
    return true;
  }
  
  
  /**
   * Apaga a mensagem da caixa de email.
   * @param m Mensagem a ser apagada.
   * @return <code>true</code> se a mensagem
   * foi apagada corretamente, <code>false</code>
   * caso contrário.
   */
  public boolean delete(Message m) {
    if(m == null) return false;
    m.delete();
    return this.mark(m, Flag.DELETED);
  }
  
  
  /**
   * Apaga as mensagens informadas da caixa de email.
   * @param mess Coleção de mensagens a ser apagada.
   * @return <code>true</code> se as mensagens forem
   * apagadas corretamente, <code>false</code> caso
   * contrário.
   */
  public boolean delete(Messages mess) {
    if(mess == null || mess.isEmpty()) return false;
    boolean success = false;
    Iterator<Message> it = mess.iterator();
    Message m = null;
    while(it.hasNext()) {
      m = it.next();
      boolean b = false;
      if(m.isDeleted()) b = this.delete(m);
      if(!success) success = b;
    }
    return success;
  }
  
  
  /**
   * Marca as mensagens informadas como lidas.
   * @param mess Coleção de mensagens a serem 
   * marcadas como lidas.
   * @return <code>true</code> se as mensagens forem
   * marcadas corretamente, <code>false</code>
   * caso contrário.
   */
  public boolean read(Messages mess) {
    if(mess == null || mess.isEmpty()) return false;
    boolean success = false;
    Iterator<Message> it = mess.iterator();
    Message m = null;
    while(it.hasNext()) {
      m = it.next();
      boolean b = false;
      if(!m.isSeen()) b = this.read(m);
      if(!success) success = b;
    }
    return success;
  }
  
  
  /**
   * Marca a mensagem informada como lida.
   * @param m Mensagem a ser marcada como lida.
   * @return <code>true</code> se a mensagem for
   * marcada corretamente, <code>false</code>
   * caso contrário.
   */
  public boolean read(Message m) {
    if(m == null) return false;
    m.setSeen(true);
    return this.mark(m, Flag.SEEN);
  }
  
  
  /**
   * Retorna uma coleção contendo as novas mensagens
   * (não lidas) de email.
   * @return Coleção de novas mensagens (não lidas).
   */
  public Messages getNewMessages() {
    return ms.filter(new NewMessageFilter());
  }
  
}
