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

package us.pserver.smail.internal;

import us.pserver.smail.Attachment;
import us.pserver.smail.SMailException;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;


/**
 * <p style="font-size: medium;">
 * Conversor de <code>com.jpower.simplemail.Attachment</code> 
 * para <code>javax.mail.BodyPart</code>.
 * </p>
 * 
 * @see us.pserver.smail.Attachment
 * @see javax.mail.BodyPart
 * @see us.pserver.smail.internal.Converter
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
public class AttachmentConverter implements
    Converter<Attachment, BodyPart> {


  @Override
  public BodyPart convert(Attachment x) throws SMailException {
    if(x == null) return null;
    if(x.getFile() == null && x.getInput() == null)
      return null;
    
    MimeBodyPart part = new MimeBodyPart();
    try {
      if(x.getFile() == null) {
        InputDataSource is = new InputDataSource(x.getInput());
        is.setContentType(x.getType());
        is.setName(x.getName());
        part.setDataHandler(new DataHandler(is));
      } else {
        part.attachFile(x.getFile());
      }
      part.setDisposition(BodyPart.ATTACHMENT);
      part.setFileName(x.getName());
      part.setDescription(x.getDescription());
    } catch(Exception ex) {
      throw new SMailException("{AttachmentConverter"
          + ".convert( Attachment )}", ex);
    }
    return part;
  }
  
}
