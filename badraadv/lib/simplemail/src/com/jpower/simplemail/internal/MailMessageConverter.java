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
import com.jpower.simplemail.SMailException;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;


/**
 * <p style="font-size: medium;">
 * Conversor de <code>javax.mail.Message</code>
 * para <code>com.jpower.simplemail.Message</code>.
 * </p>
 * 
 * @see com.jpower.simplemail.Message
 * @see javax.mail.Message
 * @see com.jpower.simplemail.internal.Converter
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "MailMessageConverter",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Conversor de objetos"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class MailMessageConverter implements Converter<javax.mail.Message, Message> {


  @Override
  public Message convert(javax.mail.Message x) throws SMailException {
    if(x == null) return null;
    Message msg = new Message();
    
    try {
      
      msg.setSubject(x.getSubject());
    
      Address[] ads = x.getFrom();
      if(ads != null && ads.length > 0)
        msg.from(ads[0].toString());
    
      ads = x.getRecipients(RecipientType.TO);
      if(ads != null)
        for(Address a : ads)
          msg.to(a.toString());
    
      ads = x.getRecipients(RecipientType.CC);
      if(ads != null)
        for(Address a : ads)
          msg.cc(a.toString());
    
      ads = x.getRecipients(RecipientType.BCC);
      if(ads != null)
        for(Address a : ads)
          msg.bcc(a.toString());
    
      this.setContent(msg, x);
      this.setFlags(msg, x);
      
    } catch(Exception ex) {
      throw new SMailException("{MessageUnconverter"
          + ".convert(javax.mail.Message)", ex);
    }
    
    return msg;
  }
  
  
  /**
   * Verifica se a mensagem possui anexos.
   * @param mx Mensagem a ser verificada por anexos.
   * @return <code>true</code> se a mensagem possui
   * anexos, <code>false</code> caso contrário.
   */
  public static boolean hasAttachments(javax.mail.Message mx) {
    if(mx == null) return false;
    try {
      Object o = mx.getContent();
      if(!(o instanceof Multipart)) return false;
      Multipart mp = (Multipart) o;
      for(int i = 0; i < mp.getCount(); i++) {
        BodyPart p = mp.getBodyPart(i);
        if(isAttachment(p)) return true;
      }
    } catch(Exception ex) {}
    return false;
  }
  
  
  /**
   * Verifica se o <code>BodyPart</code> informado
   * possui um objeto anexo.
   * @param p BodyPart a ser verificado por anexos.
   * @return <code>true</code> se o BodyPart for um
   * anexo, <code>false</code> caso contrário.
   */
  public static boolean isAttachment(BodyPart p) {
    if(p == null) return false;
    try {
      if(p.getContentType().contains("IMAGE")
          || p.getContentType().contains("Image")
          || p.getContentType().contains("image")
          || p.getContentType().contains("APPLICATION")
          || p.getContentType().contains("Application")
          || p.getContentType().contains("application") 
          || p.getContentType().contains("VIDEO")
          || p.getContentType().contains("video")
          || p.getContentType().contains("Video") 
          || (p.getDisposition() != null 
          && p.getDisposition().equals(Part.ATTACHMENT)))
        return true;
    } catch(MessagingException ex) {}
    return false;
  }
  
  
  private void setContent(Message msg, javax.mail.Message x) throws Exception {
    if(msg == null || x == null) return;
    
    Object c = x.getContent();
    if(c instanceof String) {
      msg.setContent(c.toString());
      return;
    }
    
    if(c instanceof Multipart) {
      
      Multipart m = (Multipart) c;
      
      for(int i = 0; i < m.getCount(); i++) {
        
        BodyPart p = m.getBodyPart(i);
        
        if(p.getContentType() == null) continue;
        
        else if(p.getContentType().equalsIgnoreCase("text/html"))
          msg.setContentHtml(p.getContent().toString());
        
        else if(p.getContentType().equalsIgnoreCase("text/plain"))
          msg.setContent(p.getContent().toString());
        
        else if(isAttachment(p))
          this.extractEmbeddedObject(msg, p);
      }
    }
  }
  
  
  public void extractEmbeddedObject(Message msg, BodyPart part) throws Exception {
    if(msg == null || part == null) return;
    
    if(part.getContentType().contains("IMAGE") 
        || part.getContentType().contains("image")
        || part.getContentType().contains("Image")) {
      
      EmbeddedImage emb = new BodyPartImageConverter().convert(part);
      msg.embedImage(emb);     
    } else { 
      Attachment att = new BodyPartConverter().convert(part);
      msg.attach(att);
    }
  }
  
  
  private void setFlags(Message msg, javax.mail.Message x) throws Exception{
    Flags fs = x.getFlags();
    if(fs.contains(Flags.Flag.SEEN))
      msg.setSeen(true);
    if(fs.contains(Flags.Flag.DELETED))
      msg.delete();
  }
  
}
