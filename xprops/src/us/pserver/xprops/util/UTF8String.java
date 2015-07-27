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

package us.pserver.xprops.util;

import java.nio.charset.Charset;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 24/07/2015
 */
public class UTF8String {

  private static final String utf8 = "UTF-8";
  
  private final String string;
  
  
  public UTF8String(String str) {
    string = Valid.off(str).getOrFail(String.class);
  }
  
  
  public UTF8String(byte[] bs) {
    Valid.off(bs)
        .testNull("Invalid Byte Array: ")
        .test(bs.length < 1, "Invalid Length (bs.length < 1): ");
    string = new String(bs, getCharset());
  }
  
  
  public UTF8String(byte[] bs, int off, int len) {
    Valid.off(bs)
        .testNull("Invalid Byte Array: ")
        .test(bs.length < 1, "Invalid Length (bs.length < 1): ")
        .test(off < 0 || off+len > bs.length, 
            String.format("Invalid Parameters: off=%d, len=%d, bs.length=%d", off, len, bs.length));
    string = new String(bs, off, len, getCharset());
  }
  
  
  public Charset getCharset() {
    return Charset.forName(utf8);
  }
  
  
  public String getCharsetString() {
    return utf8;
  }
  
  
  @Override
  public String toString() {
    return string;
  }
  
  
  public byte[] getBytes() {
    return string.getBytes(getCharset());
  }
  
}
