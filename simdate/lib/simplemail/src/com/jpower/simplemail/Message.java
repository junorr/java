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
import com.jpower.simplemail.internal.MailMessageConverter;
import java.io.File;
import java.util.Date;
import javax.mail.Address;
import javax.mail.MessagingException;


/**
 * <p style="font-size: medium;">
 * Mensagem de email que encapsula todas as informações
 * necessárias, como remetente, destinatários, assunto,
 * conteúdo de texto ou html, anexos, objetos embarcados
 * e suporte a marcação de status (nova, lida ou removida).
 * </p>
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "Message",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Mensagem de email"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class Message {
  
  private Recipients to;
  
  private Recipients cc;
  
  private Recipients bcc;
  
  private String from;
  
  private String subject;
  
  private String content;
  
  private String contentHtml;
  
  private Date sent;
  
  private Attachments atts;
  
  private boolean deleted, seen, attachment;
  
  private int uid;
  
  
  /**
   * Construtor padrão sem argumentos, constrói
   * uma mensagem vazia.
   */
  public Message() {
    to = new Recipients();
    cc = new Recipients();
    bcc = new Recipients();
    from = null;
    subject = null;
    content = null;
    atts = new Attachments();
    contentHtml = null;
    deleted = seen = attachment = false;
    sent = null;
    uid = -1;
  }

  
  /**
   * Define todos os anexos da mensagem
   * @param atts Lista de anexos.
   * @return Esta instância modificada de Message.
   */
  public Message setAttachments(Attachments atts) {
    this.atts = atts;
    return this;
  }

  
  /**
   * Retorna uma coleção contendo todos os 
   * anexos da mensagem.
   * @return Lista de anexos da mensagem.
   */
  public Attachments getAttachments() {
    return atts;
  }


  /**
   * Anexa um arquivo à mensagem.
   * @param attachment Arquivo a ser anexado.
   * @param description Descrição do arquivo.
   * @return Esta instânacia modificada de Message.
   */
  public Message attach(File attachment, String description) {
    if(attachment == null) return this;
    Attachment emb = new Attachment(attachment);
    emb.setDescription(description);
    this.attachment = true;
    atts.add(emb);
    return this;
  }
  
  
  /**
   * Anexa um arquivo à mensagem.
   * @param attachment Arquivo a ser anexado.
   * @return Esta instânacia modificada de Message.
   */
  public Message attach(File attachment) {
    if(attachment == null) return this;
    Attachment emb = new Attachment(attachment);
    this.attachment = true;
    atts.add(emb);
    return this;
  }
  
  
  /**
   * Anexa um objeto <code>Attachment</code> à mensagem.
   * @param emb Objeto a ser anexado.
   * @return Esta instânacia modificada de Message.
   */
  public Message attach(Attachment emb) {
    if(emb == null) return this;
    this.attachment = true;
    atts.add(emb);
    return this;
  }
  
  
  /**
   * Embarca uma imagem ao corpo da mensagem,
   * que poderá ser referenciada pela tag html
   * retornada.
   * @param image Imagem a ser embarcada.
   * @return Tag HTML pela qual poderá ser
   * referenciada a imagem.
   */
  public String embedImage(File image) {
    if(image == null || !image.exists())
      return null;
    
    EmbeddedImage img = new EmbeddedImage();
    img.setImage(image);
    atts.add(img);
    return img.getImageHtmlTag();
  } 
  
  
  /**
   * Embarca uma imagem ao corpo da mensagem,
   * que poderá ser referenciada pela tag html
   * retornada.
   * @param image Imagem a ser embarcada.
   * @return Tag HTML pela qual poderá ser
   * referenciada a imagem.
   */
  public String embedImage(EmbeddedImage image) {
    if(image == null) return null;
    atts.add(image);
    return image.getImageHtmlTag();
  }


  /**
   * Retorna o conteúdo de texto da Mensagem
   * @return Contúdo de texto.
   */
  public String getContent() {
    return content;
  }


  /**
   * Define o conteúdo de texto da mensagem.
   * @param content Conteúdo de texto.
   * @return Esta instância modificada de Message.
   */
  public Message setContent(String content) {
    this.content = content;
    return this;
  }


  /**
   * Retorna o conteúdo HTML da mensagem.
   * @return Conteúdo HTML.
   */
  public String getContentHtml() {
    return contentHtml;
  }


  /**
   * Define o conteúdo HTML da mensagem.
   * @param contentHtml Conteúdo HTML
   * @return Esta instância modificada de Message.
   */
  public Message setContentHtml(String contentHtml) {
    this.contentHtml = contentHtml;
    return this;
  }


  /**
   * Retorna o remetente da mensagem.
   * @return Remetente da mensagem.
   */
  public String from() {
    return from;
  }


  /**
   * Define o remetente da mensagem.
   * @param from remetente da mensagem.
   * @return Esta instância modificada de Message.
   */
  public Message from(String from) {
    this.from = from;
    return this;
  }


  /**
   * Retorna o assunto da mensagem.
   * @return assunto da mensagem.
   */
  public String getSubject() {
    return subject;
  }


  /**
   * Define o assunto da mensagem.
   * @param subject assunto da mensagem.
   * @return Esta instância modificada de Message.
   */
  public Message setSubject(String subject) {
    this.subject = subject;
    return this;
  }


  /**
   * Retorna os destinatários diretos da mensagem.
   * @return destinatários diretos da mensagem.
   */
  public Recipients to() {
    return to;
  }


  /**
   * Define os destinatários diretos da mensagem.
   * @param tos destinatários diretos da mensagem.
   * @return Esta instância modificada de Message.
   */
  public Message to(Recipients tos) {
    this.to = tos;
    return this;
  }
  
  
  /**
   * Adiciona um destinatário direto à mensagem.
   * @param to destinatário direto.
   * @return Esta instância modificada de Message.
   */
  public Message to(String to) {
    this.to.add(to);
    return this;
  }


  /**
   * Retorna os destinatário ocultos da mensagem.
   * @return destinatários ocultos da mensagem.
   */
  public Recipients bcc() {
    return bcc;
  }


  /**
   * Adiciona um destinatário oculto à mensagem.
   * @param bcc destinatário oculto.
   * @return Esta instância modificada de Message.
   */
  public Message bcc(String bcc) {
    this.bcc.add(bcc);
    return this;
  }


  /**
   * Define os destinatários ocultos da mensagem.
   * @param bcc destinatários ocultos da mensagem.
   * @return Esta instância modificada de Message.
   */
  public Message bcc(Recipients bcc) {
    this.bcc = bcc;
    return this;
  }


  /**
   * Retorna os destinatários por cópia da mensagem.
   * @return destinatários por cópia.
   */
  public Recipients cc() {
    return cc;
  }


  /**
   * Adiciona um destinatário por cópia à mensagem.
   * @param cc destinatário por cópia.
   * @return Esta instância modificada de Message.
   */
  public Message cc(String cc) {
    this.cc.add(cc);
    return this;
  }


  /**
   * Define os destinatários por cópia da mensagem.
   * @param ccs destinatários por cópia.
   * @return Esta instância modificada de Message.
   */
  public Message cc(Recipients ccs) {
    this.cc = ccs;
    return this;
  }



  /**
   * Verifica se a mensagem está
   * marcada como apagada.
   * @return <code>true</code> se a mensagem
   * estiver macada como deletada, 
   * <code>false</code> caso contrário.
   */
  public boolean isDeleted() {
    return deleted;
  }


  /**
   * Marca a mensagem como deletada.
   */
  public void delete() {
    this.deleted = true;
  }


  /**
   * Verifica se a mensagem 
   * está marcada como já lida.
   * @return <code>true</code> se a mensagem
   * estiver marcada como lida, <code>false</code>
   * caso contrário.
   */
  public boolean isSeen() {
    return seen;
  }


  /**
   * Merca a mensagem como nova ou já vista.
   * @param seen <true> se a mensagem já foi 
   * vista, <code>false</code> se a mensagem é nova.
   */
  public void setSeen(boolean seen) {
    this.seen = seen;
  }
  
 
  /**
   * Verifica se a mensagem possui anexos.
   * @return <code>true</code> se a mensagem
   * possui um ou mais anexos, <code>false</code>
   * caso contrário.
   */
  public boolean hasAttachment() {
    return this.attachment;
  }


  /**
   * Retorna a data de envio da mensagem.
   * @return data de envio.
   */
  public Date getSentDate() {
    return sent;
  }


  /**
   * Define a data de envio da mensagem.
   * @param sent data de envio.
   * @return Esta instância modificada de Message.
   */
  public Message setSentDate(Date sent) {
    this.sent = sent;
    return this;
  }


  /**
   * Retorna o número de identificação da
   * mensagem na caixa de correio (MailBox).
   * @return Número de identificação da mensagem.
   */
  public int getUid() {
    return uid;
  }


  /**
   * Define o número de identificação da
   * mensagem na caixa de correio (MailBox).
   * @param uid Número de identificação da mensagem.
   */
  public void setUid(int uid) {
    this.uid = uid;
  }


  /**
   * Gera o código hash de identificação única da mensagem.
   * @return código hash único.
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 83 * hash + (this.from != null ? this.from.hashCode() : 0);
    hash = 83 * hash + (this.subject != null ? this.subject.hashCode() : 0);
    hash = 83 * hash + (this.sent != null ? this.sent.hashCode() : 0);
    hash = 83 * hash + (this.atts != null ? this.atts.hashCode() : 0);
    return hash;
  }


  /**
   * Compara um objeto com a mensagem, que deve
   * pertencer à classe com.jpower.simplemail.Message
   * ou javax.mail.Message.
   * @param o Objeto a ser comparado.
   * @return <code>true</code> se o objeto passado
   * representar a mesma mensagem de email, 
   * <code>false</code> caso contrário.
   */
  @Override
  public boolean equals(Object o) {
    if(o == null) return false;
    if(o instanceof Message)
      return this.equals((Message) o);
    else if(o instanceof javax.mail.Message)
      return this.equals((javax.mail.Message) o);
    else
      return false;
  }
  
  
  /**
   * Compara o objeto passado com esta mensagem,
   * verificando se são iguais, isto é, se possui 
   * os mesmo remetentes e destinatários, mesmo assunto,
   * mesmos anexos e data de envio.
   * @param m Objeto a ser comparado
   * @return <code>true</code> se as mensagens
   * forem iguais, <code>false</code> caso contrário.
   */
  private boolean equals(Message m) {
    return this.hashCode() == m.hashCode();
  }
  
  
  /**
   * Compara o objeto passado com esta mensagem,
   * verificando se são iguais, isto é, se possui 
   * os mesmo remetentes e destinatários, mesmo assunto,
   * mesmos anexos e data de envio.
   * @param m Objeto a ser comparado
   * @return <code>true</code> se as mensagens
   * forem iguais, <code>false</code> caso contrário.
   */
  private boolean equals(javax.mail.Message mx) {
    try {
      String mxsubject = mx.getSubject();
      String mxfrom = null;
      Address[] add = mx.getFrom();
      if(add != null && add.length > 0 && add[0] != null)
        mxfrom = add[0].toString();
      Date mxsent = mx.getSentDate();
      
      boolean equals = true;
      equals = equals && mxsubject.equals(this.subject);
      equals = equals && mxfrom.equals(this.from);
      equals = equals && mxsent.equals(this.sent);
      equals = equals && ( 
          ((MailMessageConverter.hasAttachments(mx)) 
             && !atts.isEmpty())
          || 
          ( !MailMessageConverter.hasAttachments(mx)
             && atts.isEmpty()) 
          );
      return equals;
    } catch(MessagingException ex) {}
    return false;
  }
  
  
  /**
   * Retorna a representação da mensagem em String.
   * @return String representando a mensagem.
   */
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(" * Message: ");
    s.append(this.getSubject());
    s.append("{");
    s.append("\n   <- from: ");
    s.append(this.from());
    s.append("\n   -> to: ");
    s.append(this.to().first());
    s.append("\n   -> cc: ");
    s.append(this.cc().first());
    s.append("\n   - attachs: ");
    s.append(this.getAttachments());
    s.append("\n }\n");
    return s.toString();
  }

}
