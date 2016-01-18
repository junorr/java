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

package us.pserver.streams.deprecated;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Buffer de bytes com tamanho limitado e
 * comportamento diferenciado quanto à inserção
 * de dados no buffer. Quando o buffer estiver cheio,
 * ou seja, atingiu o limite de tamanho definido no
 * construtor, novos dados são aceitos (anexados
 * no final do buffer) e os dados mais antigos 
 * são descartados.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 1.0 - 15/06/2014
 */
public class LimitedBuffer {
  
  /**
   * <code>UTF8 = "UTF-8"</code><br>
   * Padrão de codificação de caracteres.
   */
  public static final String UTF8 = "UTF-8";
  

  private byte[] buffer;
  
  private int index;
  
  
  /**
   * Construtor padrão que recebe o tamanho 
   * do buffer.
   * @param size tamanho do buffer em bytes.
   */
  public LimitedBuffer(int size) {
    if(size < 1)
      throw new IllegalArgumentException(
          "Invalid buffer size ["+ size+ "]");
    buffer = new byte[size];
    index = 0;
  }
  
  
  /**
   * Retorna o buffer interno.
   * @return buffer interno.
   */
  public byte[] buffer() {
    return buffer;
  }
  
  
  /**
   * Retorna a quantidade de bytes inseridos no buffer.
   * @return quantidade de bytes inseridos no buffer.
   */
  public int size() {
    return index;
  }
  
  
  /**
   * Retorna o tamanho do buffer.
   * @return tamanho do buffer em bytes.
   */
  public int length() {
    return buffer.length;
  }
  
  
  /**
   * Descarta os dados já inseridos no buffer.
   * @return Esta instância modificada de <code>LimitedBuffer</code>.
   */
  public LimitedBuffer reset() {
    index = 0;
    return this;
  }
  
  
  /**
   * Verifica se o índice informado está 
   * entre zero e o tamanho do buffer.
   * @param idx Índice.
   * @return <code>true</code> se o índice
   * está entre zero e o tamanho do buffer,
   * <code>false</code> caso contrário.
   */
  public boolean checkIndex(int idx) {
    return idx >= 0 && idx < buffer.length;
  }
  
  
  private void throwIndexException(int idx) {
    throw new IllegalArgumentException(
        "Invalid index ["+ idx+ "] (bounds=0,"
        + (buffer.length-1)+ ")");
  }
  
  
  /**
   * Retorna o byte armazenado sob o índice informado.
   * @param idx índice do byte a ser recuperado.
   * @return byte armazenado sob o índice informado.
   */
  public byte get(int idx) {
    if(!checkIndex(idx))
      throwIndexException(idx);
    return buffer[idx];
  }
  
  
  /**
   * Define um byte no índice informado.
   * @param idx índico do byte definido.
   * @param b byte a ser armazenado.
   * @return Esta instância modificada de <code>LimitedBuffer</code>.
   */
  public LimitedBuffer set(int idx, byte b) {
    if(!checkIndex(idx))
      throwIndexException(idx);
    buffer[idx] = b;
    return this;
  }
  
  
  /**
   * Adiciona um byte após o último dado inserido,
   * descartando os dados mais antigos se o buffer
   * estiver cheio.
   * @param b byte a ser adicionado.
   * @return Esta instância modificada de <code>LimitedBuffer</code>.
   */
  public LimitedBuffer put(byte b) {
    if(index >= buffer.length) {
      for(int i = 0; i < buffer.length -1; i++) {
        buffer[i] = buffer[i+1];
      }
      index = buffer.length -1;
    }
    buffer[index++] = b;
    return this;
  }
  
  
  /**
   * Adiciona um byte após o último dado inserido,
   * descartando os dados mais antigos se o buffer
   * estiver cheio.
   * @param b byte a ser adicionado.
   * @return Esta instância modificada de <code>LimitedBuffer</code>.
   */
  public LimitedBuffer put(int b) {
    return put((byte) b);
  }
  
  
  /**
   * Cria uma <code>String</code> a partir dos bytes
   * armazenados no buffer.
   * @param charset Padrão de caracteres da 
   * <code>String</code> criada.
   * @return <code>String</code> a partir dos bytes
   * armazenados no buffer.
   */
  public String toString(String charset) {
    try {
      return new String(buffer, 0, index, charset);
    } catch(UnsupportedEncodingException e) {
      return new String(buffer);
    }
  }
  
  
  /**
   * Cria uma <code>String</code> a partir dos bytes
   * armazenados no buffer, com caracteres no formato
   * UTF-8.
   * @return <code>String</code> a partir dos bytes
   * armazenados no buffer no formato UTF-8.
   */
  public String toUTF8() {
    return toString(UTF8);
  }
  
  
  /**
   * Retorna uma <code>String</code> representando
   * textualmente o array de bytes interno do buffer.
   * @return <code>String</code> representando
   * textualmente o array de bytes interno do buffer.
   */
  public String toStringArray() {
    return Arrays.toString(Arrays.copyOfRange(buffer, 0, index));
  }
  
  
  /**
   * Escreve o conteúdo do buffer no 
   * stream de saída informado.
   * @param out stream de saída onde os dados
   * serão escritos.
   * @return Esta instância modificada de <code>LimitedBuffer</code>.
   * @throws IOException caso ocorra erro na escrita.
   */
  public LimitedBuffer writeTo(OutputStream out) throws IOException {
    out.write(buffer, 0, index);
    return this;
  }
  
  
  public static void main(String[] args) {
    LimitedBuffer lb = new LimitedBuffer(10);
    for(int i = 0; i < 15; i++) {
      lb.put(i);
      System.out.println(lb.toStringArray());
    }
  }
  
}
