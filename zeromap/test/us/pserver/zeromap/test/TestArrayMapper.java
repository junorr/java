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

package us.pserver.zeromap.test;

import java.util.Arrays;
import java.util.Date;
import us.pserver.date.SimpleDate;
import us.pserver.zeromap.Mapper;
import us.pserver.zeromap.Node;
import us.pserver.zeromap.mapper.ArrayMapper;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/03/2016
 */
public class TestArrayMapper {

  
  public static void main(String[] args) {
    Integer[] is = new Integer[10];
    for(int i = 0; i < 10; i++) {
      is[i] = i;
    }
    System.out.println("* array: "+ Arrays.toString(is));
    Mapper<Integer[]> map = new ArrayMapper<Integer>();
    Node na = map.map(is);
    System.out.println("* map  : "+ na);
    is = map.unmap(na);
    System.out.println("* unmap: "+ Arrays.toString(is));
    System.out.println();
    
    Date[] ds = new Date[10];
    for(int i = 0; i < 10; i++) {
      ds[i] = new SimpleDate().month(i+1).hour(i);
    }
    Mapper<Date[]> mapd = new ArrayMapper<Date>();
    System.out.println("* array: "+ Arrays.toString(ds));
    Node nd = mapd.map(ds);
    System.out.println("* map  : "+ nd);
    ds = mapd.unmap(nd);
    System.out.println("* unmap: "+ Arrays.toString(ds));
  }
  
}
