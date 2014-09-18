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
import com.jpower.simplemail.SMailException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;


/**
 * <p style="font-size: medium;">
 * Conversor de <code>java.lang.String</code>
 * para <code>javax.mail.BodyPart</code>.
 * </p>
 * 
 * @see javax.mail.internet.MimeBodyPart
 * @see com.jpower.simplemail.internal.Converter
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "ContentConverter",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Conversor de objetos"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class ContentConverter implements
    Converter<String, MimeBodyPart> {

  public static final String 
      HTML_CONTENT = "TEXT/HTML",
      PLAIN_CONTENT = "TEXT/PLAIN";
  
  private String contentType = PLAIN_CONTENT;

  
  @Override
  public MimeBodyPart convert(String content) throws SMailException {
    if(content == null) return null;
      
    MimeBodyPart part = new MimeBodyPart();
    try {
      part.setContent(content, this.contentType);
    } catch(MessagingException ex) {
      throw new SMailException("{ContentConverter.convert(String)}", ex);
    }
    return part;
  }
  
  
  /**
   * Define o tipo mime (Mime-Type) do conteúdo.
   * @param content Mime-Type
   * @return Esta instância modificada de ContentConverter.
   */
  public ContentConverter contentType(String content) {
    if(content != null && !content.equals(""))
      this.contentType = content;
    return this;
  }
  
}
