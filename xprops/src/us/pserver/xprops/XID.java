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
 * Represent a tag ID.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 201508
 */
public class XID {

  private final String id;
  
  private XID parent;
  
  
  /**
   * Default constructor which receives the String ID.
   * @param id The String id.
   */
  public XID(String id) {
    this.id = Valid.off(id).forEmpty().getOrFail("Invalid String ID: ");
    parent = null;
  }
  
  
  /**
   * Return the String ID.
   * @return The String ID.
   */
  public String getStringID() {
    return id;
  }
  
  
  /**
   * Return the parend ID.
   * @return The parend ID.
   */
  public XID parent() {
    return parent;
  }
  
  
  /**
   * Compose this ID with a parent ID.
   * @param parent The parent ID that will compose this ID.
   * @return The parent ID.
   */
  public XID compose(XID parent) {
    this.parent = Valid.off(parent).forNull().getOrFail(XID.class);
    return parent;
  }
  
  
  /**
   * Create a XID with the specified String ID.
   * @param id String id
   * @return XID
   */
  public XID off(String id) {
    return new XID(id);
  }
  
  
  @Override
  public String toString() {
    return (parent != null
        ? parent.toString().concat(".") : "")
        .concat(id);
  }
  
}
