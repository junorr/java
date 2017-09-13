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

package us.pserver.tools.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import us.pserver.tools.mapper.MappedValue;
import us.pserver.tools.mapper.ObjectMapper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 13/09/2017
 */
public class TestListMapper {

  public static List<Long> randoms() {
    List<Long> rds = new ArrayList<>();
    for(int i = 0; i < 10; i++) {
      rds.add((long) (Math.random() * 1000L));
    }
    return rds;
  }
  
  public static void main(String[] args) {
    List<Long> ls = randoms();
    System.out.println(ls);
    ObjectMapper mp = new ObjectMapper();
    MappedValue val = mp.map(ls);
    System.out.println(val);
    System.out.println(val.getClass().getSimpleName());
    System.out.println(Arrays.toString(val.asArray()));
    ls = (List<Long>) mp.unmap(ArrayList.class, val);
    System.out.println(ls);
  }
  
}
