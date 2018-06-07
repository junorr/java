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

package us.pserver.dbone.bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/06/2018
 */
public class Deck {

  private final int maxSize;
  
  private final List<Container> load;
  
  @JsonCreator
  public Deck(int maxSize, List<Container> load) {
    Objects.requireNonNull(load, "Bad null Container List");
    if(load.size() > maxSize) {
      String msg = String.format("Bad load size: %d > maxSize(%d)", load.size(), maxSize);
      throw new IllegalStateException(msg);
    }
    this.maxSize = maxSize;
    this.load = load;
  }
  
  public Deck(int maxSize) {
    this(maxSize, new LinkedList<>());
  }
  
  public Deck(List<Container> load) {
    this(load.size(), load);
  }
  
  public int getMaxSize() {
    return maxSize;
  }
  
  public List<Container> getLoad() {
    return Collections.unmodifiableList(load);
  }
  
  public int size() {
    return load.size();
  }
  
  public double weight() {
    return load.stream().reduce(0.0, (w,c)->w + c.weight(), (t,w)->t + w);
  }
  
  public void put(Container ctn) {
    Objects.requireNonNull(ctn, "Bad null Container");
    if(load.size() >= maxSize) {
      String msg = String.format("Max load size exceeded: %d > maxSize(%d)", (load.size() + 1), maxSize);
      throw new IllegalStateException(msg);
    }
    load.add(ctn);
  }
  
  public boolean tryPut(Container ctn) {
    if(ctn == null) return false;
    if(load.size() >= maxSize) return false;
    load.add(ctn);
    return true;
  }
  
  public Container get(int idx) {
    if(idx < 0 || idx >= load.size()) {
      throw new IllegalArgumentException(
          String.format("Bad index: 0 <= %d <= %d", idx, load.size())
      );
    }
    return load.get(idx);
  }
  
  public boolean contains(Container ctn) {
    return load.contains(ctn);
  }
  
  public int indexOf(Container ctn) {
    return load.indexOf(ctn);
  }
  
  public void forEach(Consumer<Container> cs) {
    load.forEach(cs);
  }
  
  public Stream<Container> stream() {
    return load.stream();
  }
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 47 * hash + this.maxSize;
    hash = 47 * hash + Objects.hashCode(this.load);
    return hash;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Deck other = (Deck) obj;
    if (this.maxSize != other.maxSize) {
      return false;
    }
    return Objects.equals(this.load, other.load);
  }
  
  @Override
  public String toString() {
    return "Deck{" + "maxSize=" + maxSize + ", load=" + load + '}';
  }
  
}
