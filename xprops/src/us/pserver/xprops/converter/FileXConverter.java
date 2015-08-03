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

package us.pserver.xprops.converter;

import java.io.File;
import us.pserver.xprops.XTag;
import us.pserver.xprops.XValue;
import us.pserver.xprops.util.FileTransformer;
import us.pserver.xprops.util.Validator;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 30/07/2015
 */
public class FileXConverter implements XConverter<File> {


  @Override
  public XTag toXml(File obj) {
    return new XValue(
        new FileTransformer().toString(
            Validator.off(obj).forNull().getOrFail(File.class))
    );
  }


  @Override
  public File fromXml(XTag tag) {
    return Validator.off(tag)
        .forNull().getOrFail(XTag.class)
        .xvalue().asFile();
  }

}
