/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca � software livre; voc� pode redistribu�-la e/ou modific�-la sob os
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a vers�o 2.1 da Licen�a, ou qualquer
 * vers�o posterior.
 * 
 * Esta biblioteca � distribu�da na expectativa de que seja �til, por�m, SEM
 * NENHUMA GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE
 * OU ADEQUA��O A UMA FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica
 * Geral Menor do GNU para mais detalhes.
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto
 * com esta biblioteca; se n�o, acesse 
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html, 
 * ou escreva para a Free Software Foundation, Inc., no
 * endere�o 59 Temple Street, Suite 330, Boston, MA 02111-1307 USA.
 */

package us.pserver.jom.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import us.pserver.jom.MappedValue;
import us.pserver.jom.def.ObjectMapper;

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
