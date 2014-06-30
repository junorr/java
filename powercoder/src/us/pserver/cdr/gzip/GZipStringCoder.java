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

package us.pserver.cdr.gzip;

import us.pserver.cdr.Coder;
import us.pserver.cdr.StringByteConverter;
import us.pserver.cdr.b64.Base64ByteCoder;
import static us.pserver.chk.Checker.nullstr;

/**
 * Compactador/Descompactador de <code>String's</code> no formato GZIP.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 18/06/2014
 */
public class GZipStringCoder implements Coder<String> {

  private final GZipByteCoder gzip;
  
  private final Base64ByteCoder b64;
  
  private final StringByteConverter conv;
  
  
  public GZipStringCoder() {
    gzip = new GZipByteCoder();
    b64 = new Base64ByteCoder();
    conv = new StringByteConverter();
  }


  @Override
  public String apply(String t, boolean encode) {
    return (encode ? encode(t) : decode(t));
  }


  @Override
  public String encode(String str) {
    nullstr(str);
    byte[] bs = conv.convert(str);
    bs = gzip.encode(bs);
    bs = b64.encode(bs);
    return conv.reverse(bs);
  }


  @Override
  public String decode(String str) {
    nullstr(str);
    
    byte[] bs = conv.convert(str);
    bs = b64.decode(bs);
    bs = gzip.decode(bs);
    return conv.reverse(bs);
  }
  
  
  public static void main(String[] args) {
    String str = "Implements an input stream filter for compressing";
    System.out.println("* str='"+ str+ "'");
    System.out.println("* str.length="+ str.length());
    GZipStringCoder cdr = new GZipStringCoder();
    str = cdr.encode(str);
    System.out.println("* str='"+ str+ "'");
    System.out.println("* str.length="+ str.length());
    str = cdr.decode(str);
    System.out.println("* str='"+ str+ "'");
    System.out.println("* str.length="+ str.length());
  }
  
}
