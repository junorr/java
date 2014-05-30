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

package com.jpower.net;

import java.nio.ByteBuffer;

/**
 * Fornece funções utilitárias para manipulação de <code>ByteBuffer's</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/05/2013
 */
public class BufferUtils {
  
  
  /**
   * Obtém o limite disponível para o buffer fornecido.
   * @param bf <code>ByteBuffer</code>
   * @return limite disponível (<code>int</code>) do buffer.
   */
  public static int limit(ByteBuffer bf) {
    if(bf.limit() == 0)
      bf.flip();
    return bf.limit();
  }
  
  
  /**
   * Extrai e retorna o conteúdo do buffer em
   * um array de bytes.
   * @param bf <code>ByteBuffer</code>.
   * @return array de bytes com o conteúdo do buffer.
   */
  public static byte[] extract(ByteBuffer bf) {
    if(bf == null) return null;
    if(limit(bf) == 0) return null;
    byte[] bs = new byte[bf.limit()];
    bf.get(bs);
    bf.flip();
    return bs;
  }

  
  /**
   * Retorna uma representação string do
   * conteúdo do array de bytes informado, para fins de depuração.
   * @param bs array de bytes.
   * @param off índice de início.
   * @param length quantidade de bytes.
   * @return representação string do array de bytes.
   */
  private static String concat(byte[] bs, int off, int length) {
    if(bs == null || bs.length == 0 
        || off < 0 
        || length > (bs.length - off))
      return null;
    
    String s = "";
    for(int i = off; i < off + length; i++) {
      s += bs[i];
    }
    return s;
  }

  
  /**
   * Verifica se o buffer possui conteúdo do array de
   * bytes informado.
   * @param bs buffer <code>ByteBuffer</code>.
   * @param buf array de bytes.
   * @return O índice do início do array se o buffer contém
   * o conteúdo do array de bytes, <code>-1</code>
   * caso contrário.
   */
  public static int contains(byte[] bs, ByteBuffer buf) {
    if(bs == null || bs.length == 0
        || buf == null || buf.limit() == 0
        || bs.length > buf.limit()) 
      return -1;
    
    byte[] bufbytes = extract(buf);
    int max = bufbytes.length - bs.length;
    
    String sb = concat(bs, 0, bs.length);
    
    for(int i = 0; i < max; i++) {
      String s = concat(bufbytes, i, bs.length);
      if(s.equals(sb)) return i;
    }
    
    return -1;
  }
  
}
