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

import us.pserver.tools.Valid;

/**
 * An abstract implementation of XUnit 
 * with the default behavior.
 * 
 * @author Juno Roesler - juno@pserver.us
 */
public abstract class AbstractUnit implements XUnit {
  
  /**
   * The unit value.
   */
  final String value;
  
  /**
   * The unit id.
   */
  XID id;
  
  
  /**
   * Constructor which receives the unit value and id.
   * @param value Unit value.
   * @param id Unit id.
   */
  AbstractUnit(final String value, XID id) {
    this.value = Valid.off(value).forEmpty().getOrFail().toLowerCase();
    this.id = Valid.off(id).forNull().getOrFail(XID.class);
  }
  
  
  /**
   * Default Constructor which receives the unit value.
   * @param value Unit value.
   */
  AbstractUnit(final String value) {
    this.value = Valid.off(value).forEmpty().getOrFail();
    this.id = new XID(value);
  }
  
  
  @Override
  public XID id() {
    return id;
  }
  
  
  @Override
  public XUnit setID(XID id) {
    this.id = Valid.off(id).forNull().getOrFail(XID.class);
    return this;
  }
  
  
  @Override
  public String value() {
    return value;
  }
  
  
  @Override
  public XValue xvalue() {
    return new XValue(value);
  }
  
  
  @Override
  public String toXml() {
    return value;
  }
  
  
  @Override
  public String toString() {
    return value;
  }

}
