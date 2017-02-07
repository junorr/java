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

package us.pserver.resrc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import us.pserver.dep.Hello;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 07/02/2017
 */
public class Main {
  
  
  public static String loadClassicWay(String resource) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream(resource)));
      return br.readLine();
    } catch(IOException e) {
      return e.toString();
    } finally {
      try { if(br != null) br.close(); }
      catch(IOException ee) {}
    }
  }

  
  public static void main(String[] args) {
    final String res = "/resources/hello.txt";
    System.out.println("* classic: "+ loadClassicWay(res));
    System.out.println("* caller.: "+ ResourceLoader.caller().loadStringContent(res));
    System.out.println("* Hello..: "+ ResourceLoader.of(Hello.class).loadStringContent(res));
  }
  
}
