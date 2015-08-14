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

import us.pserver.xprops.XTag;

/**
 * A converter class for tag-objects conversion.
 * @author Juno Roesler - juno@pserver.us
 */
public interface XConverter<T> {

  /**
   * Convert the object to a xml tag.
   * @param obj The object to convert.
   * @return The converted tag.
   */
  public XTag toXml(T obj);
  
  /**
   * Convert the xml tag to a object.
   * @param tag The xml tag to convert
   * @return The converted object.
   */
  public T fromXml(XTag tag);
  
  /**
   * Verify if this converter should
   * convert objects to tag attributes or not.
   * @return <code>true</code> if this converter
   * converts objects to tag attributes, <code>false</code>
   * otherwise.
   */
  public boolean isAttributeByDefault();
  
  /**
   * Set if this converter should convert objects to 
   * tag attributes or xml tags.
   * @param attr <code>true</code> for this converter
   * convert objects to tag attributes, <code>false</code>
   * otherwise.
   */
  public void setAttributeByDefault(boolean attr);
  
}
