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
import us.pserver.xprops.util.TBoolean;
import us.pserver.xprops.util.TClass;
import us.pserver.xprops.util.TColor;
import us.pserver.xprops.util.TDate;
import us.pserver.xprops.util.TFile;
import us.pserver.xprops.util.TNumber;
import us.pserver.xprops.util.TPath;
import us.pserver.xprops.util.TSocketAddress;
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
        .apply(value);
  }
  
  
  public Boolean asBoolean() throws IllegalArgumentException {
    return new TBoolean().apply(value);
  }
  
  
  public Class asClass() throws IllegalArgumentException {
    return new TClass().apply(value);
  }
  
  
  public Color asColor() throws IllegalArgumentException {
    return new TColor().apply(value);
  }
  
  
  public Date asDate() throws IllegalArgumentException {
    return new TDate().apply(value);
  }
  
  
  public File asFile() throws IllegalArgumentException {
    return new TFile().apply(value);
  }
  
  
  public Number asNumber() throws IllegalArgumentException {
    return new TNumber().apply(value);
  }
  
  
  public Path asPath() throws IllegalArgumentException {
    return new TPath().apply(value);
  }
  
  
  public SocketAddress asSocketAddress() throws IllegalArgumentException {
    return new TSocketAddress().apply(value);
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
