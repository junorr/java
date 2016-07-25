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

package us.pserver.fpack.test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 01/11/2015
 */
public class TestLongLength {

  
  private static String long2str(long l) {
    StringBuilder sb = new StringBuilder()
        .append(l);
    int max = 19 - sb.length();
    System.out.println("* max: "+ max);
    for(int i = 0; i < max; i++) {
      sb.insert(0, "0");
    }
    return sb.toString();
  }
  
  
  private static long str2long(String str) {
    int idx = 0;
    for(int i = 0; i < str.length(); i++) {
      if(str.charAt(i) != '0') {
        idx = i;
        break;
      }
    }
    return Long.parseLong(str.substring(idx));
  }


  public static void main(String[] args) {
    System.out.println("* Long.MAX_VALUE.length: "+ String.valueOf(Long.MAX_VALUE).length());
    long l = 7;
    System.out.println("* long: "+ l);
    String str = long2str(l);
    System.out.println("* long2str("+ l+ "): "+ str);
    l = str2long(str);
    System.out.println("* str2long("+ str+ "): "+ l);
  }
  
}
