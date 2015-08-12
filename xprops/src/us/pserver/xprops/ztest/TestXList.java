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

package us.pserver.xprops.ztest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import us.pserver.xprops.XTag;
import us.pserver.xprops.converter.ListXConverter;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 12/08/2015
 */
public class TestXList {

  
  public static void main(String[] args) {
    List<Integer> list = new ArrayList<>();
    list.addAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    System.out.println("* list="+ list);
    ListXConverter xc = new ListXConverter(list);
    XTag xl = xc.createXTag();
    System.out.println("* xml="+ xl.toXml());
    list = xc.fromXml(xl);
    System.out.println("* list="+ list);
  }
  
}
