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

package com.jpower.simplemail.internal;

import com.jpower.annotations.info.Contact;
import com.jpower.annotations.info.Copyright;
import com.jpower.annotations.info.License;
import com.jpower.annotations.info.Version;
import com.jpower.simplemail.EmbeddedImage;
import com.jpower.simplemail.Attachment;
import com.jpower.simplemail.Message;
import com.jpower.simplemail.Recipients;
import com.jpower.simplemail.SMailException;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * <p style="font-size: medium;">
 * Conversor de <code>com.jpower.simplemail.Message</code>
 * para <code>javax.mail.internet.MimeMessage</code>.
 * </p>
 * 
 * @see com.jpower.simplemail.Message
 * @see javax.mail.internet.MimeMessage
 * @see com.jpower.simplemail.internal.Converter
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "MessageConverter",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Conversor de objetos"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class MessageConverter implements
    Converter<Message, MimeMessage> {

  private Session session;

  
  public MessageConverter(Session s) {
    if(s == null) 
      throw new IllegalArgumentException(
          "{MessageConverter(Session)}: Session must be NOT NULL.");
    this.session = s;
  }
  

  @Override
  public MimeMessage convert(Message msg) throws SMailException {
    if(msg == null) return null;
    
    try {
      
      MimeMessage message = new MimeMessage(session);
      MimeMultipart mult = new MimeMultipart();
      
      this.addContent(msg, mult);
      this.addAttachments(msg, mult);
      this.addTo(msg, message);
      this.addCC(msg, message);
      this.addBCC(msg, message);
      this.setFlags(msg, message);
      message.setFrom(new InternetAddress(msg.from()));
      message.setSubject(msg.getSubject());
      message.setContent(mult);
      
      return message;
      
    } catch(Throwable ex) {
      throw new SMailException("{MessageConverter.convert(Message)}:", ex);
    }
  }
  
  
  private void addAttachments(Message msg, Multipart mult) throws Throwable {
    if(mult == null || msg == null) return;
    
    if(!msg.getAttachments().isEmpty()) {
      
      EmbeddedObject[] embs = msg.getAttachments().toArray();
      
      for(EmbeddedObject e: embs) {
        BodyPart p = null;
        if(e instanceof EmbeddedImage)
          p = new EmbeddedImageConverter()
              .convert((EmbeddedImage)e);
        else
          p = new AttachmentConverter()
              .convert((Attachment)e);
        
        mult.addBodyPart(p);
      }
    }
  }
  
  
  private void addContent(Message msg, Multipart mult) throws Throwable {
    if(mult == null || msg == null) return;
    
    if(msg.getContent() != null) {
      System.out.println(">> PLAIN!!!");
      MimeBodyPart part = 
          new ContentConverter()
          .contentType(ContentConverter.PLAIN_CONTENT)
          .convert(msg.getContent());
      mult.addBodyPart(part);
    }
      
    if(msg.getContentHtml() != null) {
      MimeBodyPart part = 
          new ContentConverter()
          .contentType(ContentConverter.HTML_CONTENT)
          .convert(msg.getContentHtml());
      mult.addBodyPart(part);
    }
  }
  
  
  private void addTo(Message msg, MimeMessage mime) throws Exception {
    if(msg == null || mime == null) return;
    
    this.addRecipients(msg, mime, msg.to(), RecipientType.TO);
  }
  
  
  private void addRecipients(Message msg, MimeMessage mime, 
      Recipients rec, RecipientType type) throws Exception {
    if(msg == null || mime == null 
        || rec == null || rec.isEmpty()) return;
    
    mime.addRecipients(type, 
        new RecipientConverter()
        .convert(rec));
  }
  
  
  private void addCC(Message msg, MimeMessage mime) throws Exception {
    if(msg == null || mime == null) return;
    
    this.addRecipients(msg, mime, msg.cc(), RecipientType.CC);
  }
  
  
  private void addBCC(Message msg, MimeMessage mime) throws Exception {
    if(msg == null || mime == null) return;
    
    this.addRecipients(msg, mime, msg.bcc(), RecipientType.BCC);
  }
  
  
  private void setFlags(Message msg, MimeMessage mime) throws Exception {
    if(msg == null || mime == null) return;
    
    Flags fs = mime.getFlags();
    if(msg.isDeleted())
      fs.add(Flags.Flag.DELETED);
    if(msg.isSeen())
      fs.add(Flags.Flag.SEEN);
    
    mime.setFlags(fs, true);
  }
  
}
