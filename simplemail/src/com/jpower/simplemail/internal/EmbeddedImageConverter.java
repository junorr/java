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
import com.jpower.simplemail.SMailException;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;


/**
 * <p style="font-size: medium;">
 * Conversor de <code>com.jpower.simplemail.EmbeddedImage</code>
 * para <code>javax.mail.BodyPart</code>.
 * </p>
 * 
 * @see com.jpower.simplemail.EmbeddedImage
 * @see javax.mail.BodyPart
 * @see com.jpower.simplemail.internal.Converter
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "EmbeddedImageConverter",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Conversor de objetos"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class EmbeddedImageConverter implements 
    Converter<EmbeddedImage, BodyPart> {


  @Override
  public BodyPart convert(EmbeddedImage x) throws SMailException {
    if(x == null) return null;
    BodyPart part = new MimeBodyPart();
    try {
      part.setFileName(x.getName());
      part.setDescription(x.getName());
      part.setDisposition(BodyPart.INLINE);
      part.setDataHandler(new DataHandler(x.getDataSource()));
      part.setHeader("Content-type", x.getType());
      part.setHeader("Content-ID", "<" + x.getName() + ">");
    } catch(MessagingException ex) {
      throw new SMailException("{EmbeddedImageConverter.convert(EmbeddedImage)}", ex);
    }
    return part;
  }
  
}
