/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

package console_apps;


/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 24/05/2013
 */
public class Console_apps {
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    int i = 0;
    for(i = 0; i < 6; i++) {
      System.out.printf("**********************\n");
    }
    System.out.printf("\033[2J");
    System.out.printf("\033[41m");
    System.out.printf("\033[37m");
    System.out.printf("\033[20;10H");
    System.out.printf("int=%d - hex=%x\n", i, i);
  }
  
}
