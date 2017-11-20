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

package us.pserver.dbone.internal;

import us.pserver.tools.NotNull;
import us.pserver.dbone.OUID;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/10/2017
 */
public interface StoreUnit {

  public OUID getOUID();
  
  public Object getObject();
  
  
  public static StoreUnit of(OUID uid, Object obj) {
    return new DefStoreUnit(uid, obj);
  }
  
  
  
  
  
  public static class DefStoreUnit implements StoreUnit {
    
    private final Object obj;
    
    private final OUID ouid;
    
    public DefStoreUnit(OUID ouid, Object obj) {
      this.ouid = NotNull.of(ouid).getOrFail("Bad null ObjectUID");
      this.obj = NotNull.of(obj).getOrFail("Bad null Object");
    }

    @Override
    public OUID getOUID() {
      return ouid;
    }

    @Override
    public Object getObject() {
      return obj;
    }
  
  }
  
}
