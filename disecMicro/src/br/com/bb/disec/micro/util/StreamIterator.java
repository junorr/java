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

package br.com.bb.disec.micro.util;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 27/07/2016
 */
public class StreamIterator<T> {

  private final Iterator<T> iter;
  
  
  public StreamIterator(Iterator<T> i) {
    if(i == null) {
      throw new IllegalArgumentException("Bad Null Iterator");
    }
    this.iter = i;
  }
  
  
  public static <U> StreamIterator<U> of(Iterator<U> iter) {
    return new StreamIterator(iter);
  }
  
  
  public Iterator<T> iterator() {
    return iter;
  }
  
  
  public Iterable<T> iterable() {
    return ()->iter;
  }
  
  
  public Stream<T> stream() {
    return StreamSupport.stream(iterable().spliterator(), false);
  }
  
  
  public Stream<T> parallelStream() {
    return StreamSupport.stream(iterable().spliterator(), true);
  }
  
}
