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

package us.pserver.coder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/08/2014
 */
public abstract class StringUtils {

  public static int reverseFind(String src, String find, int fromPos) {
    if(src == null || src.isEmpty()
        || find == null || find.isEmpty()
        || find.length() > src.length()
        || fromPos < 0 || fromPos > src.length())
      return -1;
    for(int i = fromPos; i > find.length()-1; i--) {
      int is = i-find.length();
      String sub = src.substring(is, i);
      boolean b = sub.equals(find);
      if(b)
        return i-1;
    }
    return -1;
  }
  
  
  public static boolean containsIgnoreCase(String src, String cont) {
    if(src == null || src.isEmpty()
        || cont == null || cont.isEmpty())
      return false;
    String s1 = src.toLowerCase();
    String s2 = cont.toLowerCase();
    return s1.contains(s2);
  }
  
  
  public static void main(String[] args) {
    //             0123456789012345678901234567890123
    String text = "O rato roeu a roupa do rei de roma";
    String find = "rato";
    int i = reverseFind(text, find, 12);
    System.out.println("* find 'rato' in: "+ i);
    System.out.println("* find ' ': in: "+ reverseFind(text, " ", i));
  }
  
}
