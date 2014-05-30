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

package biz.source_code.base64Coder;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 22/07/2013
 */
public class Test {

  
  public static void main(String[] args) {
    String s = "http%3A%2F%2Fportal.intranet.bb.com.br%2Fwps%2Fmyportal%2Fintranet";
    System.out.println("* string: "+ s);
    s = new String(Base64Coder.encode(s.getBytes()));
    System.out.println("* base64: "+ s);
  }
  
}
