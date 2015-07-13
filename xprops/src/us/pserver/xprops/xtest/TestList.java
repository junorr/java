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

package us.pserver.xprops.xtest;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import us.pserver.xprops.util.TList;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/07/2015
 */
public class TestList {

  public static void main(String[] args) {
    List<Integer> lsi = new ArrayList<Integer>();
    for(int i = 1; i <= 10; i++) {
      lsi.add(i);
    }
    TList tlist = new TList();
    System.out.println("ls ===> "+ Objects.toString(lsi));
    String str = tlist.back(lsi);
    System.out.println("str ==> "+ str);
    lsi = tlist.apply(str);
    System.out.println("list => "+ Objects.toString(lsi));
    
    System.out.println();
    List<Date> lsd = new ArrayList<Date>();
    lsd.add(new Date());
    System.out.println("ls ===> "+ Objects.toString(lsd));
    str = tlist.back(lsd);
    System.out.println("str ==> "+ str);
    lsd = tlist.apply(str);
    System.out.println("list => "+ Objects.toString(lsd));
    
    System.out.println();
    List<SocketAddress> lsa = new ArrayList<SocketAddress>();
    lsa.add(new InetSocketAddress("10.100.0.105", 1080));
    System.out.println("ls ===> "+ Objects.toString(lsa));
    str = tlist.back(lsa);
    System.out.println("str ==> "+ str);
    lsa = tlist.apply(str);
    System.out.println("list => "+ Objects.toString(lsa));
  }
  
}
