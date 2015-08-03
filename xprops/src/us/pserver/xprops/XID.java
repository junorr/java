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

import us.pserver.xprops.util.Validator;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 13/07/2015
 */
public class XID {

  private final String id;
  
  private XID parent;
  
  
  public XID(String id) {
    this.id = Validator.off(id).forEmpty().getOrFail("Invalid String ID: ");
    parent = null;
  }
  
  
  public String getStringID() {
    return id;
  }
  
  
  public XID parent() {
    return parent;
  }
  
  
  public XID compose(XID parent) {
    this.parent = Validator.off(parent).forNull().getOrFail(XID.class);
    return parent;
  }
  
  
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
