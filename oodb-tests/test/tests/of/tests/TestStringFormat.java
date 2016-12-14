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

package tests.of.tests;

import oodb.tests.beans.StringPad;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 14/12/2016
 */
public class TestStringFormat {

  
  public static void main(String[] args) {
    String prf = "13";
    System.out.println("* stri='"+ prf+ "'");
    System.out.println("* lpad='"+ StringPad.of(prf).lpad("000000", 4)+ "'");
    System.out.println("* rpad='"+ StringPad.of(prf).rpad("000000", 4)+ "'");
    System.out.println("* cpad='"+ StringPad.of(prf).cpad("hello", 4)+ "'");
    System.out.println();
    System.out.println("* pad='"+ StringPad.of("* Opening FileChannel...").rpad(" ", 40)+ "'");
    System.out.println("          1...5...10...15...20");
    System.out.println("* concat='"+ StringPad.of(prf).concat("-", 14, "El")+ "'");
    System.out.println("          1...5...10...15...20...25...30...35...40...45...50");
    System.out.println("* concat='"+ StringPad.of(prf).concat("-", 17, "14", "15", "16", "17")+ "'");
  }
  
}
