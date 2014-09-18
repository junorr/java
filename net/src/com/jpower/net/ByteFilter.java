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
 * Implementa um filtro de recebimento <code>ReceiveFilter</code>
 * para determinado conteúdo de bytes.
 * @see com.jpower.net.ReceiveFilter
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/05/2013
 */
public class ByteFilter implements ReceiveFilter {

  private byte[] filter;
  
  private ByteBuffer test;
  
  private int position;
  
  
  /**
   * Construtor padrão que recebe o conteúdo de
   * bytes equivalente ao filtro a ser aplicado.
   * @param filter 
   */
  public ByteFilter(byte[] filter) {
    if(filter == null || filter.length == 0)
      throw new IllegalArgumentException(
          "Invalid filter: "+ filter);
    
    this.filter = filter;
  }
  

  /**
   * Verifica se o buffer informado contém 
   * os bytes referentes ao filtro.
   * @param buffer <code>ByteBuffer</code>.
   * @return <code>true</code> se o buffer contém
   * os bytes referentes ao filtro, <code>false</code>
   * caso contrário.
   */
  @Override
  public boolean match(ByteBuffer buffer) {
    test = buffer;
    position = BufferUtils.contains(filter, buffer);
    return position >= 0;
  }
  
  
  /**
   * Extrai o conteúdo do buffer em um array de bytes.
   * @param buf <code>ByteBuffer</code>.
   * @return array de bytes com o conteúdo do buffer.
   */
  protected static byte[] extractBytes(ByteBuffer buf) {
    if(buf == null || buf.limit() == 0)
      return null;
    
    byte[] bs = new byte[buf.limit()];
    buf.get(bs);
    buf.rewind();
    return bs;
  }


  /**
   * Extrai os bytes referentes ao filtro do buffer informado,
   * retornando um novo <code>ByteBuffer</code> sem os bytes
   * referentes ao filtro.
   * @param buffer <code>ByteBuffer</code>.
   * @return Novo <code>ByteBuffer</code> sem os bytes
   * referentes ao filtro em seu conteúdo.
   */
  @Override
  public ByteBuffer filter(ByteBuffer buffer) {
    if(buffer == null || buffer.limit() == 0)
      return buffer;

    int pos = position;
    if(buffer != test) {
      pos = BufferUtils.contains(filter, buffer);
      position = pos;
      test = buffer;
    }
    
    if(pos < 0) return buffer;
    
    int size = buffer.limit() - filter.length;
    
    ByteBuffer buf;
    if(buffer.isDirect())
      buf = ByteBuffer.allocateDirect(size);
    else
      buf = ByteBuffer.allocate(size);
    
    byte[] bs = extractBytes(buffer);
    
    //filter is in the middle
    if(pos > 0 && (pos + filter.length) < bs.length) {
      buf.put(bs, 0, pos);
      buf.put(bs, pos + filter.length, size - pos);
      
    //filter is in the begining
    } else if(pos == 0) {
      buf.put(bs, filter.length, size);
      
    //filter is in the end
    } else {
      buf.put(bs, 0, size);
    }
    
    //buf.flip();
    return buf;
  }
  
  
  public static void main(String[] args) {
    System.out.println("orig  : 'Man in the middle'");
    System.out.println("filter: 'in the '");
    byte[] orig = "Man in the middle".getBytes();
    ByteBuffer buf = ByteBuffer.allocate(orig.length);
    buf.put(orig);
    buf.flip();
    
    byte[] filter = "in the ".getBytes();
    
    ByteFilter f = new ByteFilter(filter);
    
    System.out.println("orig contains filter: "+ f.match(buf));
    byte b[] = new byte[buf.limit()];
    buf.get(b);
    System.out.println("orig not filtered: '"
        + new String(b)+ "'");
    buf.rewind();
    System.out.println("orig filtered    : '"
        + new String(f.filter(buf).array())+ "'");
  }
  
}
