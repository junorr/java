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

package us.pserver.streams;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * <code>InputStream</code> que retorna números inteiros 
 * aleatórios entre 0 e 255 gerados sob demanda.
 * 
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 30/07/2014
 */
public class RandomInputStream extends InputStream {
  
  /**
   * <code>MAX_VALUE = 255</code><br>
   * Limite máximo do número aleatório a ser gerado.
   */
  public static final int MAX_VALUE = 255;

  private long size;
  
  private long count;
  
  private Random rnd;
  
  
  /**
   * Construtor padrão sem argumentos,
   * gera números aleatórios infinitamente.
   */
  public RandomInputStream() {
    this(-1);
  }
  
  
  /**
   * Construtor que recebe a quantidade 
   * de números aleatórios gerados antes
   * de retornar o sinal de final de arquivo 
   * <code>(EOF = -1)</code>.
   * @param size Quantidade máxima de números
   * aleatórios gerados antes do sinal de fim
   * de arquivo. Número negativo indica um
   * tamanho infinito.
   */
  public RandomInputStream(int size) {
    this.size = size;
    count = 0;
    rnd = new Random(System.nanoTime());
  }
  
  
  /**
   * Retorna a quantidade máxima de números aleatórios 
   * gerados antes de retornar o sinal de final 
   * de arquivo ou <code>Integer.MAX_VALUE</code> 
   * para quantidade infinita. 
   * @return quantidade máxima de números aleatórios 
   * gerados antes de retornar o sinal de final 
   * de arquivo ou <code>Integer.MAX_VALUE</code> 
   * para quantidade infinita. 
   * @throws IOException Nunca é lançado.
   */
  @Override
  public int available() throws IOException {
    return (int) (size > 0 ? size : Integer.MAX_VALUE);
  }
  
  
  /**
   * Retorna <code>false</code>.
   * @return <code>false</code>.
   */
  @Override
  public boolean markSupported() { return false; }
  
  
  /**
   * Retorna a quantidade de números aleatórios
   * já gerados.
   * @return quantidade de números aleatórios
   * já gerados.
   */
  public long getCount() {
    return count;
  }
  
  
  /**
   * Gera um número inteiro aleatório entre 0 e 255.
   */
  private int next() {
    int i = rnd.nextInt(256);
    return (i >= 0 ? i : next());
  }


  /**
   * Gera um número inteiro aleatório entre 0 e 255.
   * @return um número inteiro aleatório entre 0 e 255.
   * @throws IOException Nunca é lançado.
   */
  @Override
  public int read() throws IOException {
    if(size > 0 && count >= size)
      return -1;
    count++;
    return next();
  }
  
}
