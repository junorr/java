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
import us.pserver.valid.Valid;
import us.pserver.xprops.transformer.StringTransformer;
import us.pserver.xprops.transformer.BooleanTransformer;
import us.pserver.xprops.transformer.CharTransformer;
import us.pserver.xprops.transformer.ClassTransformer;
import us.pserver.xprops.transformer.ColorTransformer;
import us.pserver.xprops.transformer.DateTransformer;
import us.pserver.xprops.transformer.FileTransformer;
import us.pserver.xprops.transformer.NumberTransformer;
import us.pserver.xprops.transformer.PathTransformer;
import us.pserver.xprops.transformer.SocketAddressTransformer;

/**
 * Represents a XML unit value with some
 * util converting methods.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class XValue extends XTag {

  
  /**
   * Default constructor which receives the String value.
   * @param value Xml unit value.
   */
  public XValue(final String value) {
    super(value);
  }
  
  
  /**
   * Return the value converted from String 
   * using the specified Transformer.
   * @param <T> The type of the converted value.
   * @param transformer The Transformer for 
   * convert the String value.
   * @return The value converted from String.
   * @throws IllegalArgumentException In case of 
   * error converting the String value.
   */
  public <T> T as(StringTransformer<T> transformer) throws IllegalArgumentException {
    return Valid.off(transformer)
        .forNull().getOrFail(StringTransformer.class)
        .fromString(value);
  }
  
  
  /**
   * Convert the String value to Boolean.
   * @return Boolean
   * @throws IllegalArgumentException In case of
   * error converting to Boolean.
   */
  public Boolean asBoolean() throws IllegalArgumentException {
    return new BooleanTransformer().fromString(value);
  }
  
  
  /**
   * Convert the String value to Character.
   * @return Character
   * @throws IllegalArgumentException In case of
   * error converting to Character.
   */
  public Character asChar() throws IllegalArgumentException {
    return new CharTransformer().fromString(value);
  }
  
  
  /**
   * Convert the String value to Class.
   * @return Class
   * @throws IllegalArgumentException In case of
   * error converting to Class.
   */
  public Class asClass() throws IllegalArgumentException {
    return new ClassTransformer().fromString(value);
  }
  
  
  /**
   * Convert the String value to Color.
   * @return Color
   * @throws IllegalArgumentException In case of
   * error converting to Color.
   */
  public Color asColor() throws IllegalArgumentException {
    return new ColorTransformer().fromString(value);
  }
  
  
  /**
   * Convert the String value to Date.
   * @return Date
   * @throws IllegalArgumentException In case of
   * error converting to Date.
   */
  public Date asDate() throws IllegalArgumentException {
    return new DateTransformer().fromString(value);
  }
  
  
  /**
   * Convert the String value to File.
   * @return File
   * @throws IllegalArgumentException In case of
   * error converting to File.
   */
  public File asFile() throws IllegalArgumentException {
    return new FileTransformer().fromString(value);
  }
  
  
  /**
   * Convert the String value to Number.
   * @return Number
   * @throws IllegalArgumentException In case of
   * error converting to Number.
   */
  public Number asNumber() throws IllegalArgumentException {
    return new NumberTransformer().fromString(value);
  }
  
  
  /**
   * Convert the String value to Path.
   * @return Path
   * @throws IllegalArgumentException In case of
   * error converting to Path.
   */
  public Path asPath() throws IllegalArgumentException {
    return new PathTransformer().fromString(value);
  }
  
  
  /**
   * Convert the String value to SocketAddress.
   * @return SocketAddress
   * @throws IllegalArgumentException In case of
   * error converting to SocketAddress.
   */
  public SocketAddress asSocketAddress() throws IllegalArgumentException {
    return new SocketAddressTransformer().fromString(value);
  }
  
  
  @Override
  public String toXml() {
    return value();
  }
  
}
