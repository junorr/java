/*
 * Direitos Autorais Reservados (c) 2011 Juno Roesler
 * Contato: juno.rr@gmail.com
 * 
 * Esta biblioteca é software livre; você pode redistribuí-la e/ou modificá-la sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free
 * Software Foundation; tanto a versão 2.1 da Licença, ou (a seu critério) qualquer
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

package com.jpower.simplemail.internal;

import com.jpower.annotations.info.Contact;
import com.jpower.annotations.info.Copyright;
import com.jpower.annotations.info.License;
import com.jpower.annotations.info.Version;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


/**
 * <p style="font-size: medium;">
 * Implementa um buffer de dados (bytes), sem
 * tamanho máximo, limitado apenas pela
 * memória disponível.
 * </p>
 *
 * @author Juno Roesler - juno.rr@gmail.com
 * @version 1.0 - 2011.10.13
 */
@Version (
		value			= "1.0",
    name      = "InfiniteBuffer",
		date			= "2011.10.13",
		author		= "Juno Roesler",
		synopsis	= "Buffer de dados"
)
@Copyright("2011 Juno Roesler")
@Contact("juno.rr@gmail.com")
@License("GNU/LGPL v2.1")
public class InfiniteBuffer {
  
  private ArrayList<Byte> array;
  
  private ClosableOutputStream out;
  
  
  /**
   * OutputStream customizado para gravar os dados
   * em um array e que pode ser fechada, não aceitando
   * escrita de dados.
   */
  private class ClosableOutputStream extends OutputStream {
    private boolean closed = false;
    /**
     * Grava o inteiro como byte.
     * @param b inteiro a ser gravado como byte.
     * @throws IOException Caso o OutputStream esteja fechado.
     */
    @Override
    public void write(int b) throws IOException {
      if(closed) 
        throw new IOException("ClosableOutputStream is closed");
      array.add((byte) b);
    }
    /**
     * Fecha o OutputStream para escrita de dados.
     */
    @Override
    public void close() {
      closed = true;
    }
    /**
     * Verifica se o OutputStream está fechado
     * para escrita de dados.
     * @return <code>true</code> se estiver
     * fechado, <code>false</code> caso
     * contrário.
     */
    public boolean isClosed() {
      return closed;
    }
  }
  
  
  /**
   * Construtor padrão e sem argumentos.
   */
  public InfiniteBuffer() {
    array = new ArrayList<Byte>();
    out = new ClosableOutputStream();
  }
  
  
  /**
   * Construtor que recebe o tamanho inicial do buffer.
   * @param initialSize tamanho inicial do buffer.
   */
  public InfiniteBuffer(int initialSize) {
    array = new ArrayList<Byte>(initialSize);
    out = new ClosableOutputStream();
  }
  
  
  /**
   * Retorna o tamanho do buffer.
   * @return tamanho do buffer.
   */
  public int getSize() {
    return array.size();
  }
  
  
  /**
   * Retorna um InputStream dos dados do buffer.
   * @return InputStream dos dados.
   */
  public InputStream getInput() {
    return new ByteArrayInputStream(this.toArray());
  }
  
  
  /**
   * Retorna um OutputStream para gravação de dados
   * no buffer.
   * @return OutputStream para gravação dos dados.
   */
  public OutputStream getOutput() {
    if(out.isClosed()) out = new ClosableOutputStream();
    return out;
  }
  
  
  /**
   * Limpa todos os dados do buffer.
   */
  public void clear() {
    array = new ArrayList<Byte>();
  }
  
  
  /**
   * Retorna um array de bytes com todos os
   * dados do buffer.
   * @return array de bytes.
   */
  public byte[] toArray() {
    if(array.isEmpty()) return null;
    array.trimToSize();
    byte[] buf = new byte[array.size()];
    for(int i = 0; i < array.size(); i++) {
      buf[i] = array.get(i);
    }
    return buf;
  }
  
  
  public static void main(String[] args) throws IOException {
    InfiniteBuffer buf = new InfiniteBuffer(5);
    System.out.println("[Buffer Size:  "+buf.getSize()+"]");
    buf.getOutput().write("INFINITE BUFFER!".getBytes());
    System.out.println("[Buffer Size:  "+buf.getSize()+"]");
    System.out.println(new String(buf.toArray()));
  }

}
