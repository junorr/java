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

package us.pserver.xprops;

import java.awt.Color;
import java.io.File;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.util.Date;
import us.pserver.xprops.util.XmlTransformer;
import us.pserver.xprops.util.BooleanTransformer;
import us.pserver.xprops.util.ClassTransformer;
import us.pserver.xprops.util.ColorTransformer;
import us.pserver.xprops.util.DateTransformer;
import us.pserver.xprops.util.FileTransformer;
import us.pserver.xprops.util.NumberTransformer;
import us.pserver.xprops.util.PathTransformer;
import us.pserver.xprops.util.SocketAddressTransformer;
import us.pserver.xprops.util.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/07/2015
 */
public class XValue extends XTag {

  
  public XValue(final String value) {
    super(value);
  }
  
  
  public <T> T as(XmlTransformer<T> transformer) throws IllegalArgumentException {
    return Valid.off(transformer)
        .getOrFail(XmlTransformer.class)
        .transform(value);
  }
  
  
  public Boolean asBoolean() throws IllegalArgumentException {
    return new BooleanTransformer().transform(value);
  }
  
  
  public Class asClass() throws IllegalArgumentException {
    return new ClassTransformer().transform(value);
  }
  
  
  public Color asColor() throws IllegalArgumentException {
    return new ColorTransformer().transform(value);
  }
  
  
  public Date asDate() throws IllegalArgumentException {
    return new DateTransformer().transform(value);
  }
  
  
  public File asFile() throws IllegalArgumentException {
    return new FileTransformer().transform(value);
  }
  
  
  public Number asNumber() throws IllegalArgumentException {
    return new NumberTransformer().transform(value);
  }
  
  
  public Path asPath() throws IllegalArgumentException {
    return new PathTransformer().transform(value);
  }
  
  
  public SocketAddress asSocketAddress() throws IllegalArgumentException {
    return new SocketAddressTransformer().transform(value);
  }
  
  
  @Override
  public String toXml() {
    if(!this.childs().isEmpty())
      return super.toXml();
    return (getXmlIdentation() != null 
        ? this.getIdent().concat(this.value) 
        : this.value
    );
  }
  
}
