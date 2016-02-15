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

package us.pserver.fxcr.test;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 15/02/2016
 */
public class TestParseStringCommand {

  private static List<String> parseStringCommand(String scmd) {
    boolean token = false;
    char TKS = ' ', TKQ = '"';
    byte itkq = 0;
    char[] chars = scmd.toCharArray();
    List<String> lcmd = new LinkedList<>();
    StringBuilder buf = new StringBuilder();
    for(char c : chars) {
      if(c == TKS && itkq == 0) token = true;
      else if(c == TKQ && ++itkq == 2) {
        itkq = 0;
        buf.append(c);
        token = true;
      }
      else {
        buf.append(c);
      }
      if(token && buf.length() > 0) {
        token = false;
        lcmd.add(buf.toString());
        buf = new StringBuilder();
      }
    }
    return lcmd;
  }
  
  
  public static void main(String[] args) {
    String scmd = "cmd /c \"dir c:\\\"";
    System.out.println("* scmd=["+scmd+ "]");
    List<String> lcmd = parseStringCommand(scmd);
    System.out.println("* parsed: "+ lcmd);
  }
  
  
}
