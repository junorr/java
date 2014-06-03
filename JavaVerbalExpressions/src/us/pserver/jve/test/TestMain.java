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

package us.pserver.jve.test;

import ru.lanwen.verbalregex.VerbalExpression;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 03/06/2014
 */
public class TestMain {

  
  public static void main(String[] args) {
    VerbalExpression exp = VerbalExpression.regex()
        .startOfLine()
        .then("\"")
        .anything()
        .then("\"").build();
    System.out.println("* exp="+ exp);
    String str = "\"hello\"";
    System.out.println("* str="+ str);
    System.out.println("* exp.text="+ exp.testExact(str));
    
    str = "\"\"";
    System.out.println("* str="+ str);
    System.out.println("* exp.text="+ exp.testExact(str));
    
    str = "\"hello";
    System.out.println("* str="+ str);
    System.out.println("* exp.text="+ exp.testExact(str));
    
    exp = VerbalExpression.regex()
        .startOfLine()
        .then("#")
        .anything().build();
    
    System.out.println("* exp="+ exp);
    str = "# hello";
    System.out.println("* str="+ str);
    System.out.println("* exp.text="+ exp.testExact(str));
    
    str = "#hello";
    System.out.println("* str="+ str);
    System.out.println("* exp.text="+ exp.testExact(str));
    
  }
  
}
