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

package us.pserver.dbone.test;

import java.util.Date;
import org.h2.mvstore.MVMap;
import org.h2.mvstore.MVStore;
import us.pserver.tools.mapper.ObjectUID;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 11/09/2017
 */
public class TestMVStore {

  
  public static void main(String[] args) {
    MVStore store = new MVStore.Builder().fileName("/storage/java/store.mvs").open();
    MVMap map = store.openMap("aobj");
    AObj a = new AObj("hello", new Date());
    AObj b = new AObj("hello", 5, null, null, new Date());
    System.out.println("* a: "+ a);
    System.out.println("* b: "+ b);
    System.out.println("* ObjectUID.of(a): "+ ObjectUID.of(a).getUID());
    System.out.println("* ObjectUID.of(a): "+ ObjectUID.of(a).getUID());
    System.out.println("* ObjectUID.of(b): "+ ObjectUID.of(b).getUID());
  }
  
}
