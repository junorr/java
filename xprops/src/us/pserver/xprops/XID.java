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

import us.pserver.xprops.util.Valid;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/07/2015
 */
public class XID extends AbstractUnit {

  private final XID parent;
  
  
  public XID(String id) {
    super(id);
    parent = null;
  }
  
  
  public XID(String id, XID parent) {
    super(id);
    this.parent = Valid.off(parent).getOrFail(XID.class);
  }
  
  
  @Override
  public XID id(XID id) {
    return new XID(id.value(), id.parent());
  }
  
  
  public XID inheritOf(XID parent) {
    return new XID(value, parent);
  }
  
  
  @Override
  public XID off(String id) {
    return new XID(id, parent);
  }
  
  
  public XID parent() {
    return parent;
  }
  
  
  @Override
  public String toString() {
    return (parent != null 
        ? parent.toString().concat(".") 
        : ""
        ) + value;
  }
  
}
