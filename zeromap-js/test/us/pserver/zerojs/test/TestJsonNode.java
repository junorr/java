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

package us.pserver.zerojs.test;

import us.pserver.zerojs.func.JsonFunction;
import us.pserver.zerojs.func.NodeFunction;
import us.pserver.zerojs.jen.ObjectGenerator;
import us.pserver.zeromap.Node;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 28/04/2016
 */
public class TestJsonNode {

  
  public static void main(String[] args) {
    String json = new ObjectGenerator(220).generate();
    json = "{'a':1,'b':{'c':2},'d':[[{'e':3},{'f':false}],[{'g':4.5}, true]]}";
    //json = "{\"pida\":[{\"vah\":{\"huvuhe\":\"06/04/1986 17:07:44\"}},{\"qenefu\":\"iwfly\"}],\"cydure\":\"co\",\"dutaba\":{\"pec\":\"05/11/2016 05:47:41\"},\"gyvo\":[\"16/09/2016 21:43:38\",\"06/12/2005 18:41:12\"],\"zak\":false,\"qece\":{\"tucym\":\"25/09/2016 16:30:58\"}}";
    System.out.println("* json -> "+ json);
    Node node = new NodeFunction().apply(json);
    //System.out.println("* node -> \n");
    //NodePrinter.print(node, "");
    System.out.println(node);
    ///*
    json = new JsonFunction().apply(node);
    System.out.println("* json -> "+ json);
    //*/
  }
  
}
