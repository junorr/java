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

package us.pserver.cdr.b64;

import us.pserver.cdr.Coder;
import us.pserver.cdr.StringByteConverter;

/**
 * Codificador/Decodificador de <code>String's</code> 
 * no formato Base64.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 21/08/2013
 */
public class Base64StringCoder implements Coder<String> {
  
  private StringByteConverter conv;
  
  private Base64ByteCoder coder;
  
  
  public Base64StringCoder() {
    conv = new StringByteConverter();
    coder = new Base64ByteCoder();
  }
  

  @Override
  public String apply(String str, boolean encode) {
    if(str == null || str.isEmpty())
      return str;
    return conv.reverse(coder.apply(conv.convert(str), encode));
  }
  
  
  @Override
  public String encode(String str) {
    return this.apply(str, true);
  }
  
  
  @Override
  public String decode(String str) {
    return this.apply(str, false);
  }
  
}
