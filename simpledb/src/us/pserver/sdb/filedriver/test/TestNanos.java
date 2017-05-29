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

package us.pserver.sdb.filedriver.test;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 24/01/2017
 */
public class TestNanos {

  
  public static void main(String[] args) throws InterruptedException {
    long start = System.nanoTime();
    Thread.sleep(800);
    long end = System.nanoTime();
    System.out.println("* dif: "+ (end - start));
    System.out.println("* dif/1000: "+ (end - start) / 1000);
    System.out.println("* dif/1000000: "+ (end - start) / 1000000);
    System.out.println("* dif/1000000000: "+ (end - start) / 1000000000.0);
  }
  
}
