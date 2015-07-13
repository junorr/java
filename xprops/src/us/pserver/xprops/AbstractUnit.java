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
public abstract class AbstractUnit implements XUnit {
  
  final String value;
  
  final XID id;
  
  
  private AbstractUnit(String value, XID id) {
    this.value = Valid.off(value).getOrFail("Invalid Null Value");
    this.id = Valid.off(id).getOrFail(XID.class);
  }
  
  
  protected AbstractUnit(String value) {
    this.value = Valid.off(value).getOrFail("Invalid Null Value");
    id = new XID(this.value);
  }
  
  
  @Override
  public XID id() {
    return id;
  }


  @Override
  public XUnit id(XID id) {
    return new AbstractUnit(value, id){};
  }


  @Override
  public XUnit off(String value) {
    return new AbstractUnit(value){};
  }


  @Override
  public String value() {
    return value;
  }


  @Override
  public XValue xvalue() {
    return new XValue();//value);
  }

}
