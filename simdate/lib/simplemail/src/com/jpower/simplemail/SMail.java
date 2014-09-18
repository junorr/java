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
import com.jpower.simplemail.event.ConnectionEvent;
import com.jpower.simplemail.event.ConnectionListener;
import com.jpower.simplemail.event.ErrorMailListener;
import com.jpower.simplemail.event.Listener;
import com.jpower.simplemail.event.MailAdapter;
import com.jpower.simplemail.event.MailEvent;
import com.jpower.simplemail.event.MailListener;
import com.jpower.simplemail.event.ReceiveMailListener;
import com.jpower.simplemail.event.SendMailListener;
import com.jpower.simplemail.internal.MessageConverter;
import com.jpower.simplemail.internal.MailMessageConverter;
import com.jpower.simplemail.internal.TypeLinkedList;
import java.io.File;
import java.util.Iterator;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;


/**
 * <p style="font-size: medium;">
 * Motor de conexão, envio 
 * e recebimento de emails, configurado através
 * do objeto <code>MailServer</code>.
 * </p>
 * 
 * @see com.jpower.simplemail.MailServer
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "SMail",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Motor de conexão com o servidor de email"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class SMail extends TypeLinkedList<Listener> {
  
  
  private static final String
      KEY_SEND_PROTOCOL = "mail.transport.protocol",
      KEY_RECEIVE_PROTOCOL = "mail.store.protocol",
      SUFFIX = "mail.",
      KEY_HOST = ".host",
      KEY_PORT = ".port",
      KEY_AUTH = ".auth",
      KEY_CHARSET = "mail.mime.charset";
  
  private static final int 
      EVENT_CONNECTING = 0,
      EVENT_CONNECTED = 1,
      EVENT_ERROR_CONNECTING = 2,
      EVENT_SENDING = 3,
      EVENT_MESSAGE_SENT = 4,
      EVENT_ERROR_SENDING = 5,
      EVENT_RECEIVING = 6,
      EVENT_MESSAGE_RECEIVED = 7,
      EVENT_ERROR_RECEIVING = 8;
  

  private MailEvent event;
  
  private Session s;
  
  private Store store;
  
  private MailServer server;
  

  /**
   * Autenticador com usuário e senha.
   */
  private class SMailAuthenticator extends Authenticator {
    private String u, p;
    public SMailAuthenticator(String user, String passwd) {
      u = user;
      p = passwd;
    }
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(u, p);
    }
  }
  
  
  /**
   * Construtor padrão, recebe o objeto MailServer 
   * contendo as configurações do servidor e conta 
   * de email.
   * @param s MailServer contendo as configurações do
   * servidor e conta de email.
   */
  public SMail(MailServer s) {
    if(s == null)
      throw new IllegalArgumentException("Invalid MailServer: "+s);
    
    server = s;
    event = new MailEvent(this);
    store = null;
  }


  /**
   * Retorna o objeto MailServer, contendo as
   * configurações do servidor e da conta de email.
   * @return Servidor de email.
   */
  public MailServer getServer() {
    return server;
  }


  /**
   * Define o objeto MailServer, contendo as
   * configurações do servidor e conta de email.
   * @param server Servidor de email.
   * @return Esta instância modificada de SMail.
   */
  public SMail setServer(MailServer server) {
    this.server = server;
    return this;
  }


  /**
   * Cria uma nova instância de sessão para envio de emails.
   * @return Session configurada para envio de emails.
   */
  protected Session createSendSession() {
    Properties p = new Properties();
    p.setProperty(KEY_SEND_PROTOCOL, server.getProtocol());
    p.setProperty(SUFFIX + server.getProtocol() + KEY_HOST, server.getServer());
    p.setProperty(SUFFIX + server.getProtocol() + KEY_PORT, String.valueOf(server.getPort()));
    p.setProperty(SUFFIX + server.getProtocol() + KEY_AUTH, String.valueOf(server.isAuthenticated()));
    p.setProperty(KEY_CHARSET, server.getCharset().name());
    if(server.isAuthenticated())
      s = Session.getDefaultInstance(p, new SMailAuthenticator(server.getUser(), server.getPassword()));
    else
      s = Session.getDefaultInstance(p);
    return s;
  }
  
  
  /**
   * Cria uma nova instância de sessão para recebimento de emails.
   * @return Session configurada para recebimento de emails.
   */
  private Session createReceiveSession() {
    Properties p = new Properties();
    p.setProperty(KEY_RECEIVE_PROTOCOL, MailServer.PROTOCOL_IMAPS);
    p.setProperty(SUFFIX + server.getProtocol() + KEY_HOST, server.getServer());
    p.setProperty(SUFFIX + server.getProtocol() + KEY_PORT, String.valueOf(server.getPort()));
    p.setProperty(SUFFIX + server.getProtocol() + KEY_AUTH, String.valueOf(server.isAuthenticated()));
    p.setProperty(KEY_CHARSET, server.getCharset().name());
    if(server.isAuthenticated())
      s = Session.getDefaultInstance(p, new SMailAuthenticator(server.getUser(), server.getPassword()));
    else
      s = Session.getDefaultInstance(p);
    return s;
  }
  
  
  /**
   * Fecha a conexão com o servidor de email.
   */
  public void close() {
    if(store != null)
      try {
        store.close();
        store = null;
      } catch(MessagingException ex) {}
  }
  
  
  /**
   * Verifica se está conectado com o servidor de email.
   * @return <code>true</code> se a conexão estiver ativa,
   * <code>false</code> caso contrário.
   */
  public boolean isConnected() {
    return store != null && store.isConnected();
  }
  
  
  /**
   * Recebe a caixa de emails da pasta informada.
   * @param folder Pasta da conta de emails a ser recebida.
   * @return MailBox das mensagens da pasta informada.
   * @throws SMailException caso ocorra erro no
   * recebimento da caixa de emails.
   */
  public MailBox receive(String folder) throws SMailException {
    MailBox box = new MailBox(this, folder);
    box.connect();
    return box;
  }
  
  
  /**
   * Método utilitário para conversão de mensagens.
   * @param mx Mensagem a ser convertida.
   * @return Mensagem convertida.
   * @throws SMailException caso ocorra erro na conversão.
   */
  public Message convert(javax.mail.Message mx) throws SMailException {
    return new MailMessageConverter().convert(mx);
  }
  
  
  /**
   * Método utilitário para conversão de mensagens.
   * @param ms Mensagem a ser convertida.
   * @return Mensagem convertida.
   * @throws SMailException caso ocorra erro na conversão.
   */
  public javax.mail.Message convert(Message ms) throws SMailException {
    if(s == null) return null;
    return new MessageConverter(s).convert(ms);
  }
  
  
  /**
   * Recebe a pasta informada da conta de email.
   * @param folder Nome da pasta a ser recebida.
   * @return Pasta recebida.
   * @throws SMailException caso ocorra erro ao receber.
   */
  protected Folder receiveFolder(String folder) throws SMailException {
    if(folder == null || folder.trim().equals(""))
      throw new SMailException("{SMail.receive(String)}: "
          + "Invalid folder: " + folder);
    
    Folder f = null;
    
    try {
      s = this.createReceiveSession();
      store = s.getStore(MailServer.PROTOCOL_IMAPS);
    
      event.setMessage(null);
      this.notifyMailListeners(EVENT_CONNECTING, null);
      try {
        store.connect();
      } catch(Exception ex) {
        this.notifyMailListeners(EVENT_ERROR_CONNECTING, ex);
        throw ex;
      }
      this.notifyMailListeners(EVENT_CONNECTED, null);
      this.notifyMailListeners(EVENT_RECEIVING, null);
      
      f = store.getFolder(folder);
      f.open(Folder.READ_WRITE);
      
      this.notifyMailListeners(EVENT_MESSAGE_RECEIVED, null);
      
    } catch(Exception ex) {
      throw new SMailException(ex);
    }
    
    return f;
  }
  
  
  /**
   * Envia as mensagens informadas pelo email configurado.
   * @param msgs Mensagens a serem enviadas.
   * @throws SMailException caso ocorra erro durante o envio.
   */
  public void send(Messages msgs) throws SMailException {
    if(msgs == null || msgs.isEmpty()) throw 
        new SMailException("{SMail.send(Messages)}: Invalid Messages <msgs>: "
            + (msgs == null ? "<null>" : "size="+msgs.size()));
    
    this.createSendSession();
    
    for(Message msg : msgs.toArray()) {
    
      MimeMessage message = 
          new MessageConverter(s)
          .convert(msg);
    
      event.setMessage(msg);
    
      Transport t = null;
    
      try {
        t = s.getTransport();
    
        this.notifyMailListeners(EVENT_CONNECTING, null);
        try {
          //t.connect(server, port, user, password);
          t.connect();
        } catch(MessagingException ex) {
          this.notifyMailListeners(EVENT_ERROR_CONNECTING, ex);
          throw ex;
        }
        this.notifyMailListeners(EVENT_CONNECTED, null);
    
        this.notifyMailListeners(EVENT_SENDING, null);
        
        try {
          t.sendMessage(message, message.getRecipients(RecipientType.TO));
        } catch(MessagingException ex) {
          this.notifyMailListeners(EVENT_ERROR_SENDING, ex);
          throw ex;
        }
        this.notifyMailListeners(EVENT_MESSAGE_SENT, null);
    
        t.close();
      
      } catch(Exception ex) {
        throw new SMailException("{SMail.send(Message)}", ex);
      }
    }//for
  }
  
  
  protected void send(javax.mail.Message msg) throws SMailException {
    Transport t = null;
    
    try {
      t = s.getTransport();
    
      this.notifyMailListeners(EVENT_CONNECTING, null);
      try {
        //t.connect(server, port, user, password);
        t.connect();
      } catch(MessagingException ex) {
        throw ex;
      }
        
      try {
        t.sendMessage(msg, msg.getRecipients(RecipientType.TO));
      } catch(MessagingException ex) {
        throw ex;
      }
    
      t.close();
      
    } catch(Exception ex) {
      throw new SMailException("{SMail.send(Message)}", ex);
    }
  }
  
  
  /**
   * Envia a mensagem informada pelo email configurado.
   * @param msg Mensagem a ser enviada.
   * @throws SMailException caso ocorra erro durante o envio.
   */
  public void send(Message msg) throws SMailException {
    if(msg == null) throw 
        new SMailException("{SMail.send(Message)}: Invalid Messages <null>");
    
    this.send(new Messages().addMessage(msg));
  }
  
  
  /**
   * Notifica os listeners configurados do tipo
   * de evento especificado.
   * @param eventtype Tipo de evento a ser notificado.
   * @param t Exceção no caso de evento de erro.
   */
  private void notifyMailListeners(int eventtype, Throwable t) {
    if(list.isEmpty()) return;
    event.setWhen(System.currentTimeMillis());
    Iterator<Listener> listeners = list.iterator();
    Listener ls = null;
    
    while(listeners.hasNext()) {
      ls = listeners.next();
      
      switch(eventtype) {
        case 0: //CONNECTING
          if(ls instanceof ConnectionListener)
            ((ConnectionListener) ls).connecting(new ConnectionEvent(this));
          break;
        case 1: //CONNECTED
          if(ls instanceof ConnectionListener)
            ((ConnectionListener) ls).connected(new ConnectionEvent(this));
          break;
        case 2: //ERROR_CONNECTING
          if(ls instanceof ErrorMailListener)
            ((ErrorMailListener) ls).errorConnecting(new ConnectionEvent(this), t);
          break;
        case 3: //SENDING
          if(ls instanceof SendMailListener)
            ((SendMailListener) ls).sending(event);
          break;
        case 4: //MESSAGE_SENT
          if(ls instanceof SendMailListener)
            ((SendMailListener) ls).messageSent(event);
          break;
        case 5: //ERROR_SENDING
          if(ls instanceof ErrorMailListener)
            ((ErrorMailListener) ls).errorSending(event, t);
          break;
        case 6: //RECEIVING
          if(ls instanceof ReceiveMailListener)
            ((ReceiveMailListener) ls).receiving(event);
          break;
        case 7: //MESSAGE_RECEIVED
          if(ls instanceof ReceiveMailListener)
            ((ReceiveMailListener) ls).messageReceived(event);
          break;
        case 8: //ERROR_RECEIVING
          if(ls instanceof ErrorMailListener)
            ((ErrorMailListener) ls).errorReceiving(event, t);
          break;
        default:
          break;
      }
    }
  } 
  
  
  /**
   * Retorna um array de listeners registrados.
   * @return array de listeners.
   */
  @Override
  public MailListener[] toArray() {
    if(list.isEmpty()) return null;
    MailListener[] listeners = new MailListener[list.size()];
    return list.toArray(listeners);
  }
  
}
