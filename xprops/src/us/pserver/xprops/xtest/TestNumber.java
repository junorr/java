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

import us.pserver.xprops.util.SObject;
import us.pserver.xprops.util.TNumber;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 12/07/2015
 */
public class TestNumber {

  
  public static void main(String[] args) {
    Number n = 11.712345;
    System.out.println("n ===> "+ n);
    SObject sn = new SObject();
    TNumber tn = new TNumber();
    String str = sn.apply(n);
    System.out.println("str => "+ str);
    n = tn.apply(str);
    System.out.println("n ===> "+ n);
    
    System.out.println();
    str = "11,712345";
    System.out.println("str => "+ str);
    n = tn.apply(str);
    System.out.println("n ===> "+ n);
    
    System.out.println();
    str = "5,000,000.01";
    System.out.println("str => "+ str);
    n = tn.apply(str);
    System.out.println("n ===> "+ n);
    
    System.out.println();
    str = "5.000.000,01";
    System.out.println("str => "+ str);
    n = tn.apply(str);
    System.out.println("n ===> "+ n);
    
    System.out.println();
    str = sn.apply(5000000);
    System.out.println("str => "+ str);
    n = tn.apply(str);
    System.out.println("n ===> "+ n);
  }
  
}
