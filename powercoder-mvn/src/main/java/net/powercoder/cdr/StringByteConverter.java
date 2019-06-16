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
 *
*/

package net.powercoder.cdr;

import java.io.UnsupportedEncodingException;


/**
 * Conversor de <code>String</code> em byte array 
 * <code>(byte[])</code>, utilizando a codificação
 * de caracteres UTF-8.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 20/06/2014
 */
public class StringByteConverter implements Converter<String, byte[]> {

  /**
   * <code>UTF8 = "UTF-8"</code><br>
   * Padrão de caracteres.
   */
  public static final String UTF8 = "UTF-8";
  

  @Override
  public byte[] convert(String a) {
    if(a == null || a.isEmpty())
      return null;
    try {
      return a.getBytes("UTF-8");
    } catch(UnsupportedEncodingException e) {
      return null;
    }
  }


  @Override
  public String reverse(byte[] b) {
    if(b == null || b.length == 0)
      return null;
    try {
      return new String(b, "UTF-8");
    } catch(UnsupportedEncodingException e) {
      return null;
    }
  }
  
  
  /**
   * Converte parte do byte array <code>b</code> em 
   * uma <code>String</code>.
   * @param b byte array a ser convertido em <code>String</code>.
   * @param offset Índice inicial dos dados.
   * @param length Tamanho dos dados.
   * @return <code>String</code> criada a partir da
   * parte do byte array informada.
   */
  public String reverse(byte[] b, int offset, int length) {
    if(b == null || b.length == 0)
      return null;
    try {
      return new String(b, offset, length, "UTF-8");
    } catch(UnsupportedEncodingException e) {
      return null;
    }
  }
  
}
