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
import java.util.Objects;

/**
 *
 * @author Juno Roesler - juno@pserver.us
 * @version 0.0 - 06/06/2018
 */
public class CargoShip {
  
  public static final int DEFAULT_DECK_SIZE = 2;
  
  private final String name;
  
  private final double maxWeight;

  private final Deck deck;
  
  @JsonCreator
  public CargoShip(String name, double maxWeight, Deck deck) {
    this.name = Objects.requireNonNull(name, "Bad null name");
    this.deck = Objects.requireNonNull(deck, "Bad null Deck");
    double weight = deck.weight();
    if(weight > maxWeight) {
      throw new IllegalArgumentException(
          String.format("Max weight exceeded: %f.2 > maxWeight(%f.2)", weight, maxWeight)
      );
    }
    this.maxWeight = maxWeight;
  }
  
  public CargoShip(String name, Deck deck) {
    this(name, Objects.requireNonNull(deck, "Bad null Deck").weight(), deck);
  }
  
  public CargoShip(String name, double maxWeight) {
    this(name, maxWeight, new Deck(DEFAULT_DECK_SIZE));
  }
  
  public String getName() {
    return name;
  }
  
  public double getMaxWeight() {
    return maxWeight;
  }
  
  public Deck getDeck() {
    return deck;
  }
  
  @Override
  public int hashCode() {
    int hash = 3;
    hash = 23 * hash + Objects.hashCode(this.name);
    hash = 23 * hash + (int) (Double.doubleToLongBits(this.maxWeight) ^ (Double.doubleToLongBits(this.maxWeight) >>> 32));
    hash = 23 * hash + Objects.hashCode(this.deck);
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
    final CargoShip other = (CargoShip) obj;
    if (Double.doubleToLongBits(this.maxWeight) != Double.doubleToLongBits(other.maxWeight)) {
      return false;
    }
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    return Objects.equals(this.deck, other.deck);
  }
  
  @Override
  public String toString() {
    return "CargoShip{" + "name=" + name + ", maxWeight=" + maxWeight + ", deck=" + deck + '}';
  }
  
}
