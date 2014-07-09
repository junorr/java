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

package us.pserver.http;

import us.pserver.cdr.b64.Base64StringCoder;
import us.pserver.cdr.crypt.CryptKey;
import static us.pserver.chk.Checker.nullarg;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 09/07/2014
 */
public class HeaderXCryptKey extends Header {

  private CryptKey key;
  
  
  public HeaderXCryptKey() {
    super();
    setName(HD_X_CRYPT_KEY);
  }
  
  
  public HeaderXCryptKey(CryptKey k) {
    this();
    setCryptKey(k);
  }
  
  
  public HeaderXCryptKey setCryptKey(CryptKey k) {
    nullarg(CryptKey.class, k);
    key = k;
    Base64StringCoder cdr = new Base64StringCoder();
    setValue(cdr.encode(key.toString()));
    return this;
  }
  
  
  public static HeaderXCryptKey from(Header hd) {
    nullarg(Header.class, hd);
    if(hd instanceof HeaderXCryptKey)
      return (HeaderXCryptKey) hd;
    Base64StringCoder cdr = new Base64StringCoder();
    HeaderXCryptKey hx = new HeaderXCryptKey();
    hx.setValue(hd.getValue());
    hx.key = CryptKey.fromString(cdr.decode(hd.getValue()));
    return hx;
  }
  
}
