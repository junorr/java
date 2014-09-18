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

package us.pserver.tn3270.test;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 0.0 - 31/07/2013
 */
public class MiscTests {

  
  public static void main(String[] args) {
    char c = 'ç';
    byte b = (byte) c;
    System.out.println("c='"+ c+ "', b='"+ b+ "', i='"+ (short)c+ "'");
    byte[] bs = { b };
    System.out.println(new String(bs, Charset.forName("ISO-8859-1")));
    bs = new byte[] { -25 };
    System.out.println(new String(bs));
    bs = new byte[] { -25 };
    System.out.println(new String(bs, Charset.defaultCharset()));
  }
  
}
