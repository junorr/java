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

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.lanwen.verbalregex.VerbalExpression;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 03/06/2014
 */
public class TestMain {

  
  public static void main(String[] args) {
    VerbalExpression exp = VerbalExpression.regex()
        .then("public").build();
    
    Class c = Color.class;
    System.out.println("c.equals(Color.class): "+c.equals(Color.class));
    System.out.println("c.equals(String.class): "+c.equals(String.class));
    
    int white = 255;
    System.out.println("white.toHex: "+ Integer.toHexString(white));
    
    System.out.println("pattern: "+ exp.toString());
    String str = "public, public, public";
    System.out.println("string: "+ str);
    
    Pattern p = Pattern.compile(exp.toString());
    Matcher m = p.matcher(str);
    
    int count = 0;
    while(m.find()) {
      count++;
      System.out.println("found "+ count+ ": "+ m.start()+ ", "+ m.end());
    }
  }
  
}
