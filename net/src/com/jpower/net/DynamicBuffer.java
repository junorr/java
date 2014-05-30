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

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Buffer de bytes, com capacidade expansível, 
 * suportado internamente por um <code>ArrayList&lt;Byte&gt;</code>.
 * 
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 28/05/2013
 */
public class DynamicBuffer {
  
  private ArrayList<Byte> buffer;
  
  private int index;
  
  
  /**
   * Classe interna que implementa OutputStream
   * sobre o buffer interno de DynamicBuffer.
   */
  private class Output extends OutputStream {
    @Override
    public void write(int b) throws IOException {
      buffer.add((byte) b);
    }
  }
  
  
  /**
   * Construtor sem argumentos.
   */
  public DynamicBuffer() {
    buffer = new ArrayList<Byte>();
    index = 0;
  }
  
  
  /**
   * Constrói um buffer com a capacidade inicial informada.
   * @param size Capacidade inicial do buffer.
   */
  public DynamicBuffer(int size) {
    if(size <= 0) size = 1024;
    buffer = new ArrayList<>(size);
    index = 0;
  }
  
  
  /**
   * Reinicia o marcador interno de posição.
   * @see #read() 
   * @see #hasNext() 
   * @return Esta instância modificada de DynamicBuffer.
   */
  public DynamicBuffer reset() {
    index = 0;
    return this;
  }
  
  
  /**
   * Retorna o tamanho ocupado no buffer.
   * @return Tamanho ocupado em bytes.
   */
  public int size() {
    return buffer.size();
  }
  
  
  /**
   * Verifica se o buffer está vazio.
   * @return <code>true</code> se o buffer estiver vazio,
   * <code>false</code> caso contrário.
   */
  public boolean isEmpty() {
    return buffer.isEmpty();
  }
  
  
  /**
   * Retorna um <code>Iterator&lt;Byte&gt;</code>
   * com o conteúdo do buffer.
   * @return <code>java.util.Iterator&lt;Byte&gt;</code>.
   */
  public Iterator<Byte> iterator() {
    return buffer.iterator();
  }
  
  
  /**
   * Retorna um <code>InputStream</code> do conteúdo do buffer.
   * @return <code>java.io.InputStream</code>.
   */
  public InputStream input() {
    if(buffer.isEmpty())
      return null;
    return new ByteArrayInputStream(this.toArray());
  }
  
  
  /**
   * Retorna um <code>OutputStream</code> do conteúdo do buffer.
   * @return <code>java.io.OutputStream</code>.
   */
  public OutputStream output() {
    return new Output();
  }
  
  
  /**
   * Retorna um array de bytes com o conteúdo do buffer.
   * @return <code>byte[]</code>.
   */
  public byte[] toArray() {
    if(buffer.isEmpty())
      return null;
    byte[] bs = new byte[this.size()];
    this.read(bs);
    return bs;
  }
  
  
  /**
   * Cria um buffer <code>ByteBuffer</code> com
   * o conteúdo deste <code>DynamicBuffer</code>.
   * @return buffer <code>ByteBuffer</code> com
   * o conteúdo deste <code>DynamicBuffer</code>.
   */
  public ByteBuffer toByteBuffer() {
    if(this.isEmpty()) return null;
    ByteBuffer buf = ByteBuffer.allocate(this.size());
    buf.put(this.toArray());
    return buf;
  }
  
  
  /**
   * Limpa o conteúdo do buffer, sem desalocar memória.
   * @return Esta instância modificada de DynamicBuffer.
   */
  public DynamicBuffer clear() {
    buffer.clear();
    return this.reset();
  }
  
  
  /**
   * {@inheritDoc }
   * @return {@inheritDoc }
   */
  @Override
  public DynamicBuffer clone() {
    return this.clone(0, this.size());
  }
  
  
  /**
   * Clona o buffer quanto a porção de
   * conteúdo informada.
   * @param start Index inicial a ser clonado.
   * @param length Quandtidade de bytes a serem copiados.
   * @return Novo DynamicBuffer com o conteúdo copiado.
   */
  public DynamicBuffer clone(int start, int length) {
    if(start < 0 || length < 1
        || start + length > this.size())
      return null;
    
    DynamicBuffer clone = new DynamicBuffer(length);
    for(int i = 0; i < length; i++) {
      clone.write(buffer.get((i + start)));
    }
    return clone;
  }
  
  
  /**
   * Remove a porção de conteúdo informada deste <code>DynamicBuffer</code>.
   * @param start Index inicial do conteúdo a ser removido.
   * @param length Quandtidade de bytes a serem removidos.
   * @return Esta instância modificada de <code>DynamicBuffer</code>.
   */
  public DynamicBuffer remove(int start, int length) {
    if(this.isEmpty() || start < 0 
        || start >= this.size()
        || length < 1
        || length > (this.size() - start))
      return this;
    
    
    for(int i = start; i < (start+length); i++) {
      buffer.remove(start);
    }
    return this;
  }
  
  
  /**
   * Lê e retorna 1 (um) byte do buffer, 
   * movendo o marcador de posição para frente.
   * @return <code>byte</code>.
   */
  public byte read() {
    if(index >= this.size())
      index = 0;
    return buffer.get(index++);
  }
  
  
  /**
   * Verifica se há bytes disponíveis para leitura no buffer.
   * @return <code>true</code> se existirem mais bytes para
   * leitura no buffer, <code>false</code> caso contrário.
   */
  public boolean hasNext() {
    return index +1 < this.size();
  }
  
  
  /**
   * Lê e escreve no array a quantidade de bytes relativo ao 
   * tamanho do array informado.
   * @param bs Array de bytes onde serão escritos os bytes lidos.
   * @return A quantidade de bytes lidos, ou -1 caso o array 
   * informado seja inválido.
   */
  public int read(byte[] bs) {
    if(bs == null || bs.length == 0) return -1;
    return this.read(bs, 0, bs.length);
  }
  
  
  /**
   * Lê o conteúdo do buffer para o array informado, 
   * começando na posição do buffer indicada.
   * @param start posição inicial da leitura no buffer.
   * @param bs Array de bytes onde será lido o conteúdo do buffer.
   * @return A quantidade de bytes lida, ou -1 caso os 
   * argumentos seja inválidos.
   */
  public int read(int start, byte[] bs) {
    if(bs == null || bs.length == 0
        || start >= this.size()
        || start < 0)
      return -1;
    
    int max = Math.min((this.size() - start), bs.length);
    int idx = 0;
    for(int i = start; i < (start + max); i++) {
      bs[idx++] = buffer.get(i);
    }
    return max;
  }
  
  
  /**
   * Lê a quantidade de bytes informada, do conteúdo do 
   * buffer para o array informado, 
   * começando na posição do array indicada.
   * @param bs Array de bytes onde será escrito o conteúdo do buffer.
   * @param start posição inicial do array.
   * @param length quantidade de bytes a serem lidos.
   * @return quantidade de bytes lidos, ou -1 caso 
   * os argumentos sejam inválidos.
   */
  public int read(byte[] bs, int start, int length) {
    if(bs == null || bs.length == 0 
        || start < 0 
        || start >= bs.length 
        || start >= length
        || this.isEmpty()
        || length > (bs.length - start))
      return -1;
    
    int max = length;
    for(int i = 0; i < max; i++) {
      bs[start++] = buffer.get(i);
    }
    return max;
  }
  
  
  /**
   * Lê o conteúdo do buffer para o <code>ByteBuffer</code>
   * informado.
   * @param buf <code>ByteBuffer</code> onde será escrito
   * o conteúdo do buffer.
   * @return quantidade de bytes lidos, ou -1 caso
   * o argumento seja inválido.
   */
  public int read(ByteBuffer buf) {
    if(buf == null || BufferUtils.limit(buf) == 0) 
      return -1;
    byte[] b = this.toArray();
    buf.put(b);
    return b.length;
  }
  
  
  /**
   * Lê e retorna um objeto serializado do buffer.
   * @return o objeto desserializado lido do buffer,
   * ou <code>null</code> caso não exista um objeto
   * serializado no buffer.
   */
  public Object readObject() {
    if(this.isEmpty()) return null;
    try {
      
      ObjectInputStream ois = 
          new ObjectInputStream(this.input());
      return ois.readObject();
      
    } catch(Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
  
  
  /**
   * Lê uma String a partir do conteúdo do buffer.
   * @return <code>String</code>
   */
  public String readString() {
    if(this.isEmpty()) return null;
    return new String(this.toArray());
  }
  
  
  /**
   * Escreve uma String no buffer.
   * @param s <code>String</code> a ser escrita.
   * @return Número de bytes escritos no buffer.
   */
  public int writeString(String s) {
    if(s == null || s.isEmpty())
      return -1;
    byte[] sb = s.getBytes();
    this.write(sb);
    return sb.length;
  }
  
  
  /**
   * Escreve o byte informado para o buffer
   * @param b byte a ser escrito no buffer.
   * @return Esta instância modificada de DynamicBuffer.
   */
  public DynamicBuffer write(byte b) {
    buffer.add(b);
    return this;
  }
  
  
  /**
   * Escreve o byte informado para o buffer
   * @param b byte a ser escrito no buffer.
   * @return Esta instância modificada de DynamicBuffer.
   */
  public DynamicBuffer write(int b) {
    buffer.add((byte) b);
    return this;
  }
  
  
  /**
   * Escreve o conteúdo do <code>ByteBuffer</code>
   * informado no buffer.
   * @param buf <code>ByteBuffer</code> cujo conteúdo
   * será escrito no buffer.
   * @return quantidade de bytes escritos no buffer, 
   * ou -1 caso o argumento seja inválido.
   */
  public int write(ByteBuffer buf) {
    if(buf == null || BufferUtils.limit(buf) == 0) 
      return -1;
    
    byte[] bs = BufferUtils.extract(buf);
    return this.write(bs);
  }
  
  
  /**
   * Serializa e escreve o objeto serializado no buffer.
   * @param o Objeto a ser serializado e escrito no buffer.
   * @return quantidade de bytes escritos no buffer,
   * ou -1 caso o argumento seja inválido.
   */
  public int writeObject(Object o) {
    if(o == null || !(o instanceof Serializable))
      return -1;
    
    try {
      
      int size = this.size();
      
      ObjectOutputStream oos = 
          new ObjectOutputStream(this.output());
      oos.writeObject(o);
      oos.flush();
      
      return this.size() - size;
      
    } catch(Exception ex) {
      return -1;
    }
  }
  
  
  /**
   * Escreve no buffer o conteúdo do array de bytes informado.
   * @param bs array de bytes cujo conteúdo será escrito no buffer.
   * @return quantidade de bytes escritos no buffer, ou -1
   * caso o argumento seja inválido.
   */
  public int write(byte[] bs) {
    if(bs == null) return -1;
    return this.write(bs, 0, bs.length);
  }
  
  
  /**
   * Escreve no buffer <code>length</code> bytes
   * do array de bytes informado, 
   * começando na posição do array indicada por <code>start</code>.
   * @param bs array de bytes cujo conteúdo será escrito no buffer.
   * @param start posição inicial do array onde será lido o conteúdo.
   * @param length quantidade de bytes a serem lidos do array.
   * @return quantidade de bytes escritos no buffer, ou -1
   * caso os argumentos sejam inválidos.
   */
  public int write(byte[] bs, int start, int length) {
    if(bs == null || bs.length == 0
        || start < 0
        || start >= bs.length
        || start > length
        || length > (bs.length - start))
      return -1;
    
    int max = length;
    for(int i = 0; i < max; i++) {
      buffer.add(bs[i]);
    }
    return max;
  }
  
  
  /** 
   * Vrifica se o buffer contém o array de bytes indicado,
   * a partir da posição informada.
   * @param frame array de bytes a ser verificada a 
   * existência no buffer.
   * @param startIndex Índice a partir do qual será verificada
   * a existência dos bytes informados.
   * @return <code>true</code> se o buffer possui o mesmo
   * conteúdo do array de bytes informado, <code>false</code>
   * caso contrário.
   */
  public boolean contains(byte[] frame, int startIndex) {
    if(frame == null || frame.length == 0
        || startIndex < 0 
        || (startIndex + frame.length)
        > buffer.size())
      return false;
    
    int iframe = 0;
    boolean b = true;
    for(int i = startIndex; i < frame.length + startIndex; i++) {
      b = b && (frame[iframe++] == buffer.get(i));
    }
    
    return b;
  }
  
  
  /**
   * Imprime o conteúdo do buffer na saída
   * padrão, com comprimento de linha conforme 
   * especificado.
   * @param lineLength comprimento das linhas impressas.
   */
  public void printBytes(int lineLength) {
    if(this.isEmpty() || lineLength < 1) return;
    
    System.out.println("* DynamicBuffer.content = "+ this.size());
    System.out.print("[ ");
    for(int i = 0; i < buffer.size(); i++) {
      if(i > 0 && (i % lineLength) == 0) {
        System.out.println(" ]");
        System.out.print("[ ");
      }
      System.out.print(buffer.get(i));
      if(i < (buffer.size()-1) && ((i+1) % lineLength != 0))
        System.out.print(", ");
    }
    System.out.println(" ]");
  }
  
}
