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

import us.pserver.dbone.bean.AObj;
import us.pserver.dbone.bean.BObj;
import com.google.gson.Gson;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/10/2017
 */
public class TestGsonInnerClass {

  
  public static void main(String[] args) {
    AObj a = new AObj("hello", 30, new int[]{1,2,3}, new char[]{'a','b','c'}, new Date());
    List<Integer> lst = new LinkedList<>();
    lst.add(3);
    lst.add(2);
    lst.add(1);
    BObj b = new BObj("world", a, lst);
    System.out.println(b);
    Gson gson = new Gson();
    String json = gson.toJson(b);
    System.out.println(json);
    b = gson.fromJson(json, BObj.class);
    System.out.println(b);
  }
  
}
