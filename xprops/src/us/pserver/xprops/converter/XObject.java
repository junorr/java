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

import us.pserver.xprops.XBean;
import us.pserver.xprops.XTag;
import us.pserver.xprops.util.Valid;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 31/07/2015
 */
public class XObject implements XConverter {
  
  private Class type;
  
  
  public XObject(Class type) {
    this.type = Valid.off(type).getOrFail(Class.class);
  }
  

  @Override
  public XTag toXml(Object obj) {
    return new XBean(
        Valid.off(obj).getOrFail(Object.class)
    ).bindAll().scanObject();
  }
  
  
  private Object createInstance() {
    try {
      return type.newInstance();
    } catch(Exception e) {
      return null;
    }
  }


  @Override
  public Object fromXml(XTag tag) {
    XBean bean = new XBean(
        Valid.off(tag).getOrFail(XTag.class), 
        createInstance()
    );
    return bean.bindAll().scanXml();
  }

}
